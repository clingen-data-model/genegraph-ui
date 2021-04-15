(ns genegraph-ui.pages.gene.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [genegraph-ui.common.views :as common-views]
            [genegraph-ui.pages.gene.subs :as subs]
            [reitit.frontend.easy :as rfe]))

(defn gene-validity-assertions [gene-validity-assertions]
  [:div
   [:h5.block.title.is-5 "gene validity"]
   (for [assertion gene-validity-assertions]
     ^{:key assertion}
     [:div.columns
      [:div.column (get-in assertion [:disease :label])]
      [:div.column (get-in assertion [:mode_of_inheritance :label])]
      [:div.column
       [:a {:href (rfe/href :gene-validity assertion)}
        (get-in assertion [:classification :label])]]
      [:div.column (:report_date assertion)]
      [:div.column (get-in assertion [:attributed_to :label])]])])

(defn gene [] 
  (let [gene @(subscribe [::subs/gene])]
    [:section.section
     [:div.columns
      [:div.column.is-one-third
       (common-views/side-panel)]
      [:div.column.is-two-thirds
       [:div.box
        [:p.title (:label gene)]]
       [:div.box
        (gene-validity-assertions (:gene_validity_assertions gene))]]]]))
