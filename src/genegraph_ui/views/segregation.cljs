(ns genegraph-ui.views.segregation
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
            [reitit.frontend.easy :refer [href]]))


;; fragment segregationFields on Segregation {
;;   conditions {
;;     curie
;;     label
;;   }
;;   estimated_lod_score
;;   published_lod_score
;;   meets_inclusion_criteria
;;   phenotype_negative_allele_negative_count
;;   phenotype_positive_allele_positive_count
;; }


(defmethod render-full "Segregation" [evidence]
  ^{:key evidence}
  [:section.section
   [:div.box.block
    [:h5.title.is-5 (:label evidence)]
    [:h6.subtitle.is-6
     [:div.level
      [:div.level-left
       (for [t (:type evidence)]
         ^{:key t}
         [:level-item
          (render-link t)])]]]]
   [:div.break "meets criteria for inclusion: "
    (if (:meets_inclusion_criteria evidence)
      "true"
      "false")]
   [:div.break "estimated lod score: " (:estimated_lod_score evidence)]
   [:div.break "published lod score: " (:published_lod_score evidence)]
   [:div.break "phenotype positive allele positive count "
    (:phenotype_positive_allele_positive_count evidence)]
   [:div.break "phenotype negative allele negative count "
    (:phenotype_negative_allele_negative_count evidence)]
   [:div.break "phenotypes: "]
   (for [condition (:conditions evidence)]
     [:div.break (render-link condition)
      [:span.is-size-7.has-text-grey " ("(:curie condition)")"]])])

(defmethod render-compact "Segregation" [evidence]
  ^{:key evidence}
  [:div.columns
   [:div.column.is-one-third (render-link evidence)
    [:div.break  "LOD score: "
     (or (:published_lod_score evidence) (:estimated_lod_score evidence))
     [:span.is-size-7.has-text-grey 
      (if (:published_lod_score evidence)
        " (published)"
        " (estimated)")]]
    [:div.break.is-size-7
     (if (:meets_inclusion_criteria evidence)
       [:p.has-text-success "included"]
       [:p.has-text-grey "not included"])]
    (when-let [source (:source evidence)]
      [:div.break [:a.is-size-7
                   {:href (:iri source)
                    :title (:label source)}
                   (:short_citation source)]])]
   [:div.column.is-two-thirds
    [:div.columns
     [:div.column "affected: " (:phenotype_positive_allele_positive_count evidence)]
     [:div.column "unaffected: " (:phenotype_negative_allele_negative_count evidence)]]
    [:p.break (:description evidence)]]])
