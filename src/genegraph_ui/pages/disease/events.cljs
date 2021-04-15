(ns genegraph-ui.pages.disease.events
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [cljs.pprint]))

(def disease-query
  "query($id: String){
  disease(iri: $id) {
    label
    curie
    propositions {
      curie
      subject {
        curie
        label
      }
    }
  }
}")

(re-frame/reg-event-db
 :disease/recieve-disease-query
 (fn [db [_ {:keys [data errors]}]]
   (js/console.log "Recieved disease ")
   (merge db data)))

(re-frame/reg-event-fx
 :disease/request-disease
 (fn [_ [_ id]]
   (js/console.log "Requesting disease " id)
   {:fx [[:dispatch
          [::re-graph/query
           disease-query
           {:id id}
           [:disease/recieve-disease-query]]]]}))
