(ns genegraph-ui.pages.home.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [genegraph-ui.common.views :as common-views]
            [genegraph-ui.pages.home.subs :as subs]
            [genegraph-ui.common.subs :as common-subs]
            [genegraph-ui.pages.home.events]
            [reitit.frontend.easy :as rfe]))

(defn home []
  [:div
   (common-views/navbar)
   [:section.hero.is-link
    [:div.hero-body
     [:p.title "Genegraph"]]]])


