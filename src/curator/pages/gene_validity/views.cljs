(ns curator.pages.gene-validity.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [curator.common.views :as common-views]
            [curator.pages.gene-validity.subs :as subs]))


(defn title [assertion]
  [:section.hero
   [:div.hero-body
    [:h1.title (get-in assertion [:gene :label])
     " / "
     (get-in assertion [:disease :label])]
    [:div.subtitle
     (get-in assertion [:mode_of_inheritance :label])]]])

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
    [:div
     [:section.section (common-views/navbar)]
     (title assertion)
     [:section.section
      (details assertion)
      [:hr]
      (variant-level-evidence assertion)]]))
