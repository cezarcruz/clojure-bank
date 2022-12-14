(ns clojure-bank.adapters.account-test
  (:require [clojure.test :refer :all]
            [schema.core :as s])
  (:require [clojure-bank.adapters.account :refer :all])
  (:import (java.util UUID)))

(def account-in {:agency "001"})
(def account-in-1 {:account/number  (UUID/randomUUID)
                   :account/agency  "001"
                   :account/balance 0M})
(def account-in-invalid {:agency ""})

(deftest creating-account-request
  (s/with-fn-validation
    (testing account-creation-request->account
      (let [account-out (account-creation-request->account account-in)]
        (is (= "001" (:account/agency account-out)))
        (is (uuid? (:account/number account-out)))))
    (testing "account-creation-request->account with invalid parameter"
      (account-creation-request->account account-in-invalid))))

(deftest creating-account-response
  (s/with-fn-validation
    (testing account->wire-account
      (let [account-out (account->wire-account account-in-1)]
        (is (= (:account/number account-in-1) (:account account-out)))
        (is (= (:account/agency account-in-1) (:agency account-out)))))))
