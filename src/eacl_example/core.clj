(ns eacl-example.core
  (:require [datomic.api :as d]
            [eacl.core :as eacl :refer [->Relationship spice-object]]
            [eacl.datomic.core]
            [eacl.datomic.schema :as schema]
            [eacl.datomic.impl :refer [Relation Permission]]))

; Connect to an in-memory Datomic database:
(def datomic-uri "datomic:mem://eacl")
(d/create-database datomic-uri)
(def conn (d/connect datomic-uri))

; Install the latest EACL Datomic Schema:
@(d/transact conn schema/v4-schema)

; Transact your permission schema (details below).
@(d/transact conn
             [; Account:
              (Relation :account :owner :user)              ; relation owner: user
              (Permission :account :owner :admin)
              (Permission :account :owner :update_billing_details)

              ; Product:
              (Relation :product :account :account)         ; relation account: account
              (Permission :product :account :admin :update_sku)]) ; permission update_sku = account->admin

; Transact some Datomic test entities with `:eacl/type` & `:eacl/id`:
@(d/transact conn
             [{:eacl/type :user, :eacl/id "user-1"}
              {:eacl/type :user, :eacl/id "user-2"}

              {:eacl/type :account, :eacl/id "account-1"}

              {:eacl/type :product, :eacl/id "product-1"}
              {:eacl/type :product, :eacl/id "product-2"}])

;  Make an EACL client that satisfies the `IAuthorization` protocol:
(def acl (eacl.datomic.core/make-client conn))

; Define some convenience methods over spice-object:
(def ->user (partial spice-object :user))
(def ->account (partial spice-object :account))
(def ->product (partial spice-object :product))

; Write some Relationships to EACL (you can also transact this with your entities):
(eacl/create-relationships! acl
                           [(eacl/->Relationship (->user "user-1") :owner (->account "account-1"))
                            (eacl/->Relationship (->account "account-1") :account (->product "product-1"))])

; Run some Permission Checks with `can?`:
(eacl/can? acl (->user "user-1") :update_billing_details (->account "account-1"))
; => true
(eacl/can? acl (->user "user-2") :update_billing_details (->account "account-1"))
; => false

(eacl/can? acl (->user "user-1") :update_sku (->product "product-1"))
; => true
(eacl/can? acl (->user "user-2") :update_sku (->product "product-1"))
; => false

; Enumerate resources via `lookup-resources`:
(eacl/lookup-resources acl
                       {:subject       (->user "user-1")
                        :permission    :update_sku
                        :resource/type :product
                        :limit         1000
                        :cursor        nil})
; => {:data [{:type :product, :id "product-1"}]
;     :cursor 'cursor}