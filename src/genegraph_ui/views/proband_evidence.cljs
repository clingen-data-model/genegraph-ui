(ns genegraph-ui.views.proband-evidence
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
            [genegraph-ui.query :as query :refer [defpartial defpage]]
            [reitit.frontend.easy :refer [href]]))


(defpage "ProbandEvidence"
  "query ($iri: String) {
  resource(iri: $iri) {
...compact
}
}"
  ([evidence]
   (render-compact evidence)))

(defpartial compact "ProbandEvidence"
  "{__typename
    label
    curie    
    source { curie iri label short_citation } 
    variant_evidence { variant { curie label } }
    description
    }"
  ([evidence]
  ^{:key evidence}
  [:div.columns
   [:div.column.is-one-third (render-link evidence)
    (when-let [source (:source evidence)]
      [:div.break [:a.is-size-7
                   {:href (:iri source)
                    :title (:label source)}
                   (:short_citation source)]])]
   [:div.column
    (for [variant-evidence (:variant_evidence evidence)]
      ^{:key variant-evidence}
      [:div.break (get-in variant-evidence [:variant :label])])
    [:div.break (:description evidence)]]]))

(defpartial list-item "ProbandEvidence"
  "{__typename
    label
    curie    
    source { curie iri label short_citation } 
    variant_evidence { variant { curie label } }
    description
    }"
  ([evidence]
  ^{:key evidence}
  [:div.columns
   [:div.column.is-one-third (render-link evidence)
    (when-let [source (:source evidence)]
      [:div.break [:a.is-size-7
                   {:href (:iri source)
                    :title (:label source)}
                   (:short_citation source)]])]
   [:div.column
    (for [variant-evidence (:variant_evidence evidence)]
      ^{:key variant-evidence}
      [:div.break (get-in variant-evidence [:variant :label])])
    [:div.break (:description evidence)]]]))
