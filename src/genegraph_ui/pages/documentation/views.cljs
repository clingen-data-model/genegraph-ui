(ns genegraph-ui.pages.documentation.views
  (:require [genegraph-ui.common.views :as common-views]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [clojure.string :as s]
            [reitit.frontend.easy :as rfe]))

(defn side-menu []
     [:aside.menu
      [:p.menu-label "get started"]
      [:ul.menu-list
       [:li [:a "introduction"]]
       [:li [:a "SEPIO overview"]]
       [:li [:a "query examples"]]]
      [:p.menu-label "concepts"]
      [:ul.menu-list
       [:li [:a
             {:href "https://github.com/monarch-initiative/SEPIO-ontology"}
             "SEPIO" [:span.icon [:i.fas.fa-link]]]]
       [:li [:a
             {:href "https://graphql.org/learn/"}
             "GraphQL" [:span.icon [:i.fas.fa-link]]]]]
      [:p.menu-label "clingen sepio profiles"]
      [:ul.menu-list
       [:li [:a "gene validity"]]]
      [:p.menu-label "explore"]
      [:ul.menu-list
       [:li [:a "Genegraph GraphiQL" [:span.icon [:i.fas.fa-link]]]]]])

(defn home []
  [:section.section
   (common-views/navbar)
   [:div.columns
    [:div.column.is-one-third
     (side-menu)]
    [:div.column.is-two-thirds
     [:h2.title.is-2 "Introduction"]
     [:p "Genegraph provides access to ClinGen data modeled in "
      [:a "SEPIO"]
      " presented using a GraphQL API. There are a few different paths to understanding the data and how to find what you're looking for; this site offers the ability to view concrete examples for queries, as well as "
      ]]]])
