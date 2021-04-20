(ns genegraph-ui.pages.gene-validity.events
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]))

(def gene-validity-query
  "query ($id: String) {
  gene_validity_assertion(iri: $id) {
    curie
    report_date
    disease {
      curie
      label
    }
    gene {
      curie
      label
    }
    mode_of_inheritance {
      curie
      label
    }
    classification {
      curie
      label
    }
    attributed_to {
      curie
      label
    }
    variant_evidence: evidence_lines(recursive: true, class: ProbandEvidenceLine) {
       iri
       score
       description
       type {
          iri
          curie
          label
       }
      evidence_items {
        label
      }
     }
  }
}")

(re-frame/reg-event-db
 :gene-validity/recieve-assertion-query
 (fn [db [_ {:keys [data errors]}]]
   (js/console.log "Recieved assertion ")
   (cljs.pprint/pprint errors)
   (merge db data)))

(re-frame/reg-event-fx
 :gene-validity/request-assertion
 (fn [_ [_ id]]
   (js/console.log "Requesting assertion " id)
   {:fx [[:dispatch
          [::re-graph/query
           gene-validity-query
           {:id id}
           [:gene-validity/recieve-assertion-query]]]]}))


(def gene-validity-list-query
  "{
  gene_validity_assertions {
    count
    curation_list {
      curie
      gene {
        curie
        label
      }
      disease {
        curie
        label
      }
      mode_of_inheritance {
        curie
        label
      }
      classification {
        curie
        label
      }
    }
  }
}")


(re-frame/reg-event-db
 :gene-validity/recieve-assertion-list
 (fn [db [_ {:keys [data errors]}]]
   (js/console.log "Recieved assertion list ")
   (cljs.pprint/pprint errors)
   (merge db data)))

(re-frame/reg-event-fx
 :gene-validity/request-assertion-list
 (fn [_ _]
   (js/console.log "Requesting assertion list")
   {:fx [[:dispatch
          [::re-graph/query
           gene-validity-list-query
           {}
           [:gene-validity/recieve-assertion-list]]]]}))
