(ns genegraph-ui.views.proband-evidence
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
            [reitit.frontend.easy :refer [href]]))

(defmethod render-full "ProbandEvidence" [evidence]
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
    [:h6.title.is-6 "Variants"]
    (for [variant (:variants evidence)]
      [:p.block (:label variant)])]])

(defmethod render-compact "ProbandEvidence" [evidence]
  ^{:key evidence}
  [:div.columns
   [:div.column.is-one-third (render-link evidence)
    (when-let [source (:source evidence)]
      [:div.break [:a.is-size-7
                   {:href (:iri source)
                    :title (:label source)}
                   (:short_citation source)]])]
   [:div.column
    (for [variant (:variants evidence)]
      ^{:key variant}
      [:div.break (:label variant)])
    [:div.break (:description evidence)]]]) 
