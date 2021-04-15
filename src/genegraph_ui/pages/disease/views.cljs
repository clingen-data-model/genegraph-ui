(ns genegraph-ui.pages.disease.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [genegraph-ui.common.views :as common-views]
            [genegraph-ui.pages.disease.subs :as subs]
            [genegraph-ui.pages.disease.events]
            [reitit.frontend.easy :as rfe]))

(defn propositions [proposition-list]
  [:div.box
   [:h5.title.is-5 "propositions"]
   (for [proposition proposition-list]
     ^{:key proposition}
     [:div.columns
      [:div.column
       (get-in proposition [:subject :label])]])])

(defn disease [] 
  (let [disease @(subscribe [::subs/disease])]
    [:section.section
     [:div.columns
      [:div.column.is-one-third
       (common-views/side-panel)]
      [:div.column.is-two-thirds
       [:div.box
        [:p.title (:label disease)]]
       (propositions (:propositions disease))]]]))
