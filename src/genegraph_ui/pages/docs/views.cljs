(ns genegraph-ui.pages.docs.views
  (:require [re-frame.core :as re-frame]
            [genegraph-ui.pages.docs.events :as events]
            [genegraph-ui.pages.docs.subs :as subs]
            [reitit.frontend.easy :as rfe]
            [clojure.string :as s]
            [genegraph-ui.common.views :as common-views]))

(defn side-menu []
     [:aside.menu
      [:p.menu-label "get started"]
      [:ul.menu-list
       [:li [:a {:href (rfe/href :documentation)}"introduction"]]
       [:li [:a {:href (rfe/href :sepio-overview)}"SEPIO overview"]]
       [:li [:a {:href (rfe/href :graphql-mapping)}"GraphQL mapping"]]
       [:li [:a {:href (rfe/href :examples)}"query examples"]]]
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
       [:li [:a {:href (rfe/href :gene-validity)}"gene validity"]]]
      [:p.menu-label "explore"]
      [:ul.menu-list
       [:li [:a {:href (s/replace genegraph-ui.BACKEND_HTTP "api" "ide")  } "Genegraph GraphiQL" [:span.icon [:i.fas.fa-link]]]]]])

(defn page []
  (let [content @(re-frame/subscribe [::subs/markdown])]
    [:section.section
     (common-views/navbar)
     [:div.columns
      [:div.column.is-narrow
       (side-menu)]
      [:div.column.is-two-thirds
       content
       #_[:div {:dangerouslySetInnerHTML
              {:__html content}}]]]]))
