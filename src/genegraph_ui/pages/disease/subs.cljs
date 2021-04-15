(ns genegraph-ui.pages.disease.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::disease
 (fn [db]
   (:disease db)))
