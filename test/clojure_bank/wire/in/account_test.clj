(ns clojure-bank.wire.in.account-test
  (:require [clojure.test :refer :all]
            [schema.core :as s]
            [clojure-bank.wire.in.account :as wire.in.account]
            [clojure-bank.utils.schema-test :refer :all])
  (:import (java.util UUID)))

(deftest creating-account-request
  (s/with-fn-validation
    (testing "creating a new request to validate schema"
      (is (valid! wire.in.account/AccountCreationRequest {:account-number (UUID/randomUUID)
                                                          :agency "001"}))
      (is (invalid! wire.in.account/AccountCreationRequest {:account-number 123
                                                            :agency "001"} "{:account-number (not (instance? java.util.UUID 123))}")))))
