(ns curator.pages.genes.events
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [cljs.pprint]))

(def gene-query
  "query($id: String) {
  gene(iri: $id) {
    label
    gene_validity_assertions {
      curie
      report_date
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
      attributed_to {
        curie
        label
      }
    }
  }
}")

(re-frame/reg-event-db
 :gene/recieve-gene-query
 (fn [db [_ {:keys [data errors]}]]
   (js/console.log "Recieved gene ")
   (cljs.pprint/pprint errors)
   (merge db data)))

(re-frame/reg-event-fx
 :gene/request-gene
 (fn [_ [_ id]]
   (js/console.log "Requesting gene " id)
   {:fx [[:dispatch
          [::re-graph/query
           gene-query
           {:id id}
           [:gene/recieve-gene-query]]]]}))
