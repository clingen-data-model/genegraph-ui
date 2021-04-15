(ns genegraph-ui.common.subs
  (:require [re-frame.core :as re-frame]))

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
