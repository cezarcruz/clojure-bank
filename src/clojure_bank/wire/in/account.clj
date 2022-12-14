(ns clojure-bank.wire.in.account
  (:require [schema.core :as s]))

(s/defschema AccountCreationRequest {(s/optional-key :account-number) s/Uuid
                                     (s/required-key :agency)         s/Str})
