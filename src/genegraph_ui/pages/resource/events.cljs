(ns genegraph-ui.pages.resource.events
  (:require [genegraph-ui.query :as query]
            [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]))


(def type-query
  "query ($iri: String) {
  resource(iri: $iri) {
    curie
    type {
      curie
    }
    __typename
  }
}")

(re-frame/reg-event-fx
 :resource/recieve-type-resolution
 (fn [{:keys [db]} [_ {:keys [data errors]}]]
   (js/console.log (str "recieve type resolution " data))
   {:db (assoc db :common/navigate-to-resource (:resource data))
    :fx [[:dispatch
          [:resource/select-resource (get-in data [:resource :curie])]]]}))

(re-frame/reg-event-fx
 :resource/recieve-resource
 (fn [{:keys [db]} [_ {:keys [data errors]}]]
   (js/console.log "recieve value object")
   (js/console.log (str errors))
   {:db (-> db
            (merge data)
            (assoc :common/query-response data
                   :common/query-error errors
                   :common/is-loading false))
    :fx [[:common/scroll-to-top]]}))

(re-frame/reg-event-fx
 :resource/select-resource
 (fn [{:keys [db]} [_ curie]]
   (js/console.log "select value object " curie)
   (let [resource (:common/navigate-to-resource db)]
     (if (and (contains? resource :curie)
              (contains? resource :type)
              (contains? resource :__typename))
       (let [params {:iri curie
                     :genetic_evidence_type "SEPIO:0004083"
                     :experimental_evidence_type "SEPIO:0004105"}
             query (query/uberquery resource)]
         {:db (assoc
               db
               :common/last-query query
               :common/last-params (str params)
               :common/is-loading true)
          :fx [[:dispatch
                [::re-graph/query
                 query
                 params
                 [:resource/recieve-resource]]]]})
       {:fx [[:dispatch
              [::re-graph/query
               type-query
               {:iri curie}
               [:resource/recieve-type-resolution]]]]}))))
