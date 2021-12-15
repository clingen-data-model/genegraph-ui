(ns genegraph-ui.views.variant-evidence
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
            [reitit.frontend.easy :refer [href]]))

(defmethod render-full "VariantEvidence" [evidence]
  [:section.section
   [:div.box.block
    [:h5.title.is-5
     [:div.level 
      [:div.level-right
       [:div.level-item (:curie evidence)]]
      [:div.level-left
       [:div.level-item (render-link (:source evidence))]]]]
    [:h6.subtitle.is-6
     (map render-link (:type evidence))]]
   [:div.box.block
    [:h6.title.is-6 "Variant"]
    [:p.block (get-in evidence [:variant :label])]]])

(defmethod render-compact "VariantEvidence" [evidence]
  ^{:key evidence}
  [:div.columns
   [:div.column.is-one-third (render-link evidence)
    (when-let [source (:source evidence)]
      [:div.break [:a.is-size-7
                   {:href (:iri source)
                    :title (:label source)}
                   (:short_citation source)]])]
   [:div.column
    [:p.block (get-in evidence [:variant :label])]
    [:div.break (:description evidence)]]]) 
