(ns genegraph-ui.pages.resource.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::resources
 (fn [db]
   (:genegraph/resources db)))

(re-frame/reg-sub
 ::page-resource
 (fn [db]
   (:resource db)
   ;;(get-in db [:query :resource])
   ))

(re-frame/reg-sub
 ::resource
 (fn [db id]
   (get-in db [:resources id])))

(re-frame/reg-sub
 ::element-state
 (fn [db id]
   (get-in db [:elements id])))

(re-frame/reg-sub
 ::errors
 (fn [db]
   (:common/query-error db)))
