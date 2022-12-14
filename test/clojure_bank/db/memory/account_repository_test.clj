(ns clojure-bank.db.memory.account-repository-test
  (:require [clojure.test :refer :all])
  (:require [clojure-bank.db.memory.account-repository :refer :all])
  (:import (java.util UUID)))

(def account-in-1 {:account/number (UUID/randomUUID)
                   :account/agency "001"})

(deftest save-account
  (testing "can i save this account?"
    (save account-in-1)
    (is (= account-in-1 (get-by (:account/number account-in-1))))))