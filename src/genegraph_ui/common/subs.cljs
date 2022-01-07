(ns genegraph-ui.common.subs
  (:require [re-frame.core :as re-frame]
            [genegraph-ui.common.names :as names]))

(re-frame/reg-sub
 ::navbar-burger-active
 (fn [db]
   (:common/navbar-burger-active db)))

(re-frame/reg-sub
 ::show-query
 (fn [db]
   (:common/show-query db)))

(re-frame/reg-sub
 ::user
 (fn [db]
   (:user db)))

(re-frame/reg-sub
 ::user-is-registered
 (fn [db]
   (:user-is-registered db)))

(re-frame/reg-sub
 ::user-authorization
 (fn [db]
   (:user-authorization db)))

(re-frame/reg-sub
 ::user-is-admin
 (fn [db]
   (:admin-user db)))

(re-frame/reg-sub
 ::gene-list
 (fn [db]
   (get-in db [:genes :gene_list])))

(re-frame/reg-sub
 ::affiliation-list
 (fn [db]
   (get-in db [:affiliations :agent_list])))

(re-frame/reg-sub
 ::current-search-option
 (fn [db]
   (:common/search-option db)))

(re-frame/reg-sub
 ::search-result
 (fn [db]
   (:find db)))

(re-frame/reg-sub
 ::current-page
 (fn [db]
   (or (:common/page db) 1)))

(re-frame/reg-sub
 ::current-value-object
 (fn [db]
   (:value-object db)))

(re-frame/reg-sub
 ::value-object-type
 (fn [db]
   (let [types (get-in db [:value-object :type])]
     (->> types
          (map :iri)
          (map names/iri->kw)
          set))))

(re-frame/reg-sub
 ::current-query
 (fn [db]
   (:common/last-query db)))

(re-frame/reg-sub
 ::current-params
 (fn [db]
   (.stringify js/JSON (clj->js    (:common/last-params db)) nil 2)))

(re-frame/reg-sub
 ::history
 (fn [db]
   (:history db)))

(re-frame/reg-sub
 ::query-response
 (fn [db]
   (.stringify js/JSON (clj->js (:common/query-response db)) nil 2)
   ;; (with-out-str (cljs.pprint/pprint
   ;;                (:common/query-response db)))
   ))

(re-frame/reg-sub
 ::is-loading
 (fn [db]
   (:common/is-loading db)))

(re-frame/reg-sub
 ::menu-hidden
 (fn [db]
   (:common/menu-hidden db)))

(re-frame/reg-sub
 ::params
 (fn [db]
   (:common/params db)))
