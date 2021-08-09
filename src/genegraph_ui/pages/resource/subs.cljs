(ns genegraph-ui.pages.resource.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::resource
 (fn [db]
   (:resource db)
   ;;(get-in db [:query :resource])
   ))
