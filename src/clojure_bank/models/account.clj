(ns clojure-bank.models.account
  (:require [schema.core :as s]))

(s/defschema Account {:account/number s/Uuid
                      :account/agency s/Str
                      :account/balance s/Num})
