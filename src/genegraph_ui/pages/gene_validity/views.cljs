(ns genegraph-ui.pages.gene-validity.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [genegraph-ui.common.views :as common-views]
            [genegraph-ui.pages.gene-validity.subs :as subs]
            [reitit.frontend.easy :as rfe]))


(defn title [assertion]
  [:div
   [:h1.title (get-in assertion [:gene :label])
    " / "
    (get-in assertion [:disease :label])]
   [:div.subtitle
    (get-in assertion [:mode_of_inheritance :label])]])

(defn details [assertion]
  [:div
   [:div.columns
    [:div.column.is-one-fifth "score:"]
    [:div.column.has-text-weight-semibold
     (get-in assertion [:classification :label])]]
   [:div.columns
    [:div.column.is-one-fifth "working group:"]
    [:div.column
     (get-in assertion [:attributed_to :label]) ]]
   [:div.columns
    [:div.column.is-one-fifth "report date:"]
    [:div.column
     (get-in assertion [:report_date])]]])

(defn variant-level-evidence [assertion]
  [:div
   [:h5.title.is-5 "Variant-level evidence"]
   (for [el (:variant_evidence assertion)]
     ^{:key el}[:div.columns
                [:div.column.is-one-fifth.has-text-weight-semibold (:score el) " points"]
                [:div.column.is-one-fifth (get-in el [:type 0 :label])]
                [:div.column.is-three-fifths (:description el)]])])

(defn gene-validity-assertion []
  (let [assertion @(subscribe [::subs/assertion])]
    [:section.section.home-search-main
     ;; (common-views/navbar)
     [:div.columns
      [:div.column.is-one-third
       (common-views/side-panel)]
      [:div.column.is-two-thirds
       [:div.box
        (title assertion)]
       [:div.box
        (details assertion)]
       [:div.box
        (variant-level-evidence assertion)]]]]))

(defn gene-link [gene]
  (when gene
    [:a {:href (rfe/href :gene gene)}
     (:label gene)]))

(defn gene-validity-assertions []
  (let [assertion-list @(subscribe [::subs/assertion-list])]
    [:section.section
     ;; (common-views/navbar)
     [:div.box
      [:h1.title (:count assertion-list)  " Gene Validity Assertions"]]
     [:div.box 
      (for [a (:curation_list assertion-list)]
        ^{:key a}
        [:div.columns
         [:div.column (gene-link (:gene a))]
         [:div.column (get-in a [:disease :label])]
         [:div.column (get-in a [:mode_of_inheritance :label])]
         [:div.column
          [:a {:href (rfe/href :gene-validity {:id (:curie a)})}
                       (get-in a [:classification :label])]]])]]))
