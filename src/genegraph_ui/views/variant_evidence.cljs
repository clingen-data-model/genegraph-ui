(ns genegraph-ui.views.variant-evidence
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
            [genegraph-ui.query :as query :refer [defpartial defpage]]
            [reitit.frontend.easy :refer [href]]))


(defpage  "VariantEvidence"
  "query ($iri: String) {
  resource(iri: $iri) {
...compact
...list_item
}
}"
  ([evidence]
   [:section.section
    (render-compact evidence)]))

(defpartial compact "VariantEvidence"
  "{ __typename
     label
     source { curie iri label short_citation }
     variant { curie label canonical_reference { curie } }
     zygosity { curie label }
     proband { curie label } 
     description }"
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
     [:p.block (get-in evidence [:variant :label])]
     (when (:zygosity evidence)
       [:p.block
        (render-link (:zygosity evidence))])
     [:div.break (:description evidence)]]]))

(defpartial list-item "VariantEvidence"
  "{ __typename
     label
     source { curie iri label short_citation }
     variant { curie label canonical_reference { curie } }
     zygosity { curie label }
     proband { curie label } 
     description }"
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
     [:p.block (get-in evidence [:variant :label])]
     (when (:zygosity evidence)
       [:p.block
        (render-link (:zygosity evidence))])
     [:div.break (:description evidence)]]]))
