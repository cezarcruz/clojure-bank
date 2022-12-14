(ns clojure-bank.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]
            [schema.core :as s]
            [clojure-bank.diplomat.http-in.account :as http-in.account]
            [io.pedestal.interceptor :as i])
  (:import [java.util UUID]))

(defn about-page
  [_]
  (ring-resp/response (format "Clojure %s - served from %s"
                              (clojure-version)
                              (route/url-for ::about-page))))

(s/set-fn-validation! true)

(defn home-page
  [_]
  (ring-resp/response "Hello World! XXX"))

;; Defines "/" and "/about" routes with their associated :get handlers.
;; The interceptors defined after the verb map (e.g., {:get home-page}
;; apply to / and its children (/about).
(def common-interceptors [(body-params/body-params) http/json-body])


;temp

(defn str->uuid [uuid] (UUID/fromString uuid))

(defn path-id->uuid
  ([] (path-id->uuid :id :uuid))
  ([src-name dest-name]
   (i/interceptor
     {:name ::path-id->uuid
      :enter (fn [context]
               (if-let [id (-> context :request :path-params src-name)]
                 (update-in context [:request dest-name] #(or % (str->uuid id)))
                 context))})))

;; Tabular routes
(def routes #{["/"
               :get   (conj common-interceptors
                           `home-page)
               :route-name :get-home]
              ["/about"
               :get   (conj common-interceptors
                           `about-page)
               :route-name :get-about]
              ["/account/:account-id"
               :get   (conj common-interceptors
                            (path-id->uuid :account-id :account-id)
                            http-in.account/get-account)
               :route-name :get-account]
              ["/account/:account/agency/:agency/balance"
               :get   (conj common-interceptors
                            (path-id->uuid :account :account)
                            http-in.account/get-balance)
               :route-name :get-balance]
              ["/account"
               :post  (conj common-interceptors
                            http-in.account/create-account!)
               :route-name :create-account]
              ["/account/:account/deposit"
               :post  (conj common-interceptors
                            (path-id->uuid :account :account)
                            http-in.account/deposit!)]})

;; Consumed by clojure-bank.server/create-server
;; See http/default-interceptors for additional options you can configure
(def service {:env                     :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; ::http/interceptors []
              ::http/routes            routes

              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::http/allowed-origins ["scheme://host:port"]

              ;; Tune the Secure Headers
              ;; and specifically the Content Security Policy appropriate to your service/application
              ;; For more information, see: https://content-security-policy.com/
              ;;   See also: https://github.com/pedestal/pedestal/issues/499
              ;;::http/secure-headers {:content-security-policy-settings {:object-src "'none'"
              ;;                                                          :script-src "'unsafe-inline' 'unsafe-eval' 'strict-dynamic' https: http:"
              ;;                                                          :frame-ancestors "'none'"}}

              ;; Root for resource interceptor that is available by default.
              ::http/resource-path     "/public"

              ;; Either :jetty, :immutant or :tomcat (see comments in project.clj)
              ;;  This can also be your own chain provider/server-fn -- http://pedestal.io/reference/architecture-overview#_chain_provider
              ::http/type              :jetty
              ;;::http/host "localhost"
              ::http/port              8080
              ;; Options to pass to the container (Jetty)
              ::http/container-options {:h2c? true
                                        :h2?  false
                                        ;:keystore "test/hp/keystore.jks"
                                        ;:key-password "password"
                                        ;:ssl-port 8443
                                        :ssl? false
                                        ;; Alternatively, You can specify you're own Jetty HTTPConfiguration
                                        ;; via the `:io.pedestal.http.jetty/http-configuration` container option.
                                        ;:io.pedestal.http.jetty/http-configuration (org.eclipse.jetty.server.HttpConfiguration.)
                                        }})
