(ns clojure-bank.db.memory.account-repository)

(def database (atom '()))

(defn save                                                  ;:- models.account/Account
  [account]
  (swap! database conj account)
  account)

(defn get-by [id]
  (let [accounts @database]
    (first (filter #(= (% :account/number) id) accounts))))
