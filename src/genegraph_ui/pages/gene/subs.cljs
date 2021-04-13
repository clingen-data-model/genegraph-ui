(ns genegraph-ui.pages.gene.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::gene
 (fn [db]
   (:gene db)))

