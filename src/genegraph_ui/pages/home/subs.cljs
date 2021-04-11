(ns genegraph-ui.pages.home.subs
  (:require [re-frame.core :as re-frame]
            [clojure.string :as s]))

(re-frame/reg-sub
 ::search-topic
 (fn [{:keys [:home/search-topic]}]
   (or search-topic :gene)))

(re-frame/reg-sub
 ::suggested-genes
 (fn [{:keys [:home/suggested-genes]}]
   (map #(assoc % :id (s/replace (:curie %) ":" "_")) suggested-genes)))

(re-frame/reg-sub
 ::errors
 (fn [db]
   (:errors db)))

(re-frame/reg-sub
  ::search-results
  (fn [db]
    (:home/search-results db)))
