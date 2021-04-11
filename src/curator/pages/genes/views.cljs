(ns curator.pages.genes.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [curator.common.views :as common-views]
            [curator.pages.genes.subs :as subs]
            [reitit.frontend.easy :as rfe]
            ))

(defn gene-validity-assertions [gene-validity-assertions]
  [:div
   [:div.columns
    [:div.column [:p.subtitle "gene validity"]]]
   (for [assertion gene-validity-assertions]
     ^{:key assertion}
     [:div.columns
      [:div.column (get-in assertion [:disease :label])]
      [:div.column (get-in assertion [:mode_of_inheritance :label])]
      [:div.column
       [:a {:href (rfe/href :gene-validity {:id (:curie assertion)})}
        (get-in assertion [:classification :label])]]
      [:div.column (:report_date assertion)]
      [:div.column (get-in assertion [:attributed_to :label])]])])

(defn genes [] 
  (let [gene @(subscribe [::subs/gene])]
    [:div
     [:section.section (common-views/navbar)]
     [:section.section
      [:div.columns
       [:div.column
        [:p.title (:label gene)]]]
      (gene-validity-assertions (:gene_validity_assertions gene))]]))
