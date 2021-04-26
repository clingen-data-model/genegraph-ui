(ns genegraph-ui.pages.gene-validity.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [genegraph-ui.common.views :as common-views]
            [genegraph-ui.pages.gene-validity.subs :as subs]
            [reitit.frontend.easy :as rfe]
            [clojure.string :as s]))


(defn title [assertion]
  [:div
   [:h1.title
    [:a
     {:title (get-in assertion [:gene :curie])
     :href (rfe/href :gene (:gene assertion))}
     (get-in assertion [:gene :label])]
    " / "
    [:a
     {:title (get-in assertion [:disease :curie])
      :href (rfe/href :disease (:disease assertion))}
     (get-in assertion [:disease :label])]]
   [:div.subtitle
    (get-in assertion [:mode_of_inheritance :label])]])
(defn links [assertion]
  (when-let [proposition-curie (get-in assertion [:subject :curie])]
    [:div.column.is-narrow
     "GCI"
     [:a.icon {:href (s/replace proposition-curie
                                #"GCI:"
                                "https://curation.clinicalgenome.org/curation-central/")
               :target "_blank"
               :rel "noopener noreferrer"}
      [:i.fas.fa-external-link-alt]]] ))


(defn details [assertion]
  [:div.columns
   [:div.column.has-text-weight-semibold
     {:title (:curie assertion)}
     (get-in assertion [:classification :label])]
   [:div.column
    (get-in assertion [:attributed_to :label]) ]
   [:div.column
    (get-in assertion [:report_date])]
   (links assertion)])

(defn evidence [evidence-list]
  [:div
   (for [el evidence-list]
     ^{:key el}[:div.columns
                [:div.column.is-one-fifth.has-text-weight-semibold (:score el) " points"]
                [:div.column.is-one-fifth (get-in el [:type 0 :label])]
                [:div.column.is-one-fifth (get-in el [:evidence_items 0 :label])]                
                [:div.column.is-two-fifths (:description el)]])])

(defn description [assertion]
  (when assertion
    (if @(subscribe [::subs/show-description])
      [:p
       {:on-click #(dispatch [:gene-validity/toggle-description])}
       (:description assertion)
       [:span.icon
        [:i.fas.fa-minus-circle]]]
      [:p
       {:on-click #(dispatch [:gene-validity/toggle-description])}
       (subs (:description assertion) 0 200)
       "..."
       [:span.icon
        [:i.fas.fa-plus-circle]]])))

(defn gene-validity-assertion []
  (let [assertion @(subscribe [::subs/assertion])]
    [:section.section
     [:div.columns
      [:div.column.is-one-third
       (common-views/side-panel)]
      [:div.column.is-two-thirds
       [:div.box
        (title assertion)]
       [:div.box
        (details assertion)]
       [:div.box
        (description assertion)]
       [:div.box
        [:h5.title.is-5 "Variant-level evidence"]
        (evidence (:variant_evidence assertion))]
       [:div.box
        [:h5.title.is-5 "Experimental evidence"]
        (evidence (:experimental_evidence assertion))]]]]))

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
