(ns clojure-bank.adapters.account
  (:require [schema.core :as s]
            [clojure-bank.models.account    :as models.account]
            [clojure-bank.wire.out.account  :as wire.out.account]
            [clojure-bank.wire.in.account   :as wire.in.account])
  (:import (java.util UUID)))

(s/defn account-creation-request->account :- models.account/Account
  [request :- wire.in.account/AccountCreationRequest]
  {:account/number (UUID/randomUUID)
   :account/agency (:agency request)
   :account/balance 0M})

(s/defn account->wire-account :- wire.out.account/Account
  [{:account/keys [number agency balance]} :- models.account/Account]
  {:account number :agency agency :balance balance})
