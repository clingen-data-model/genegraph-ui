(ns genegraph-ui.pages.find.events
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]))

(def search-query
  "query ($text: String, $type: String) {
  find(text: $text, type: $type) {
    results {
      __typename
      curie
      label
      description
      type {
        curie
        label
      }
      subject_of {
        __typename
        curie
        label
        subject {
          curie
          label
        }
        predicate {
          curie
          label
        }
        object {
          curie
          label
        }
        qualifier {
          curie
          label
        }
      }
    }
    count
  }
}")

(re-frame/reg-event-db
 :find/recieve-result
 (fn [db [_ {:keys [data errors]}]]
   (js/console.log "recieved search result")
   (merge db data)))

(re-frame/reg-event-fx
 :find/search
 (fn [{:keys [db]} [_ search-text]]
   (js/console.log search-query)
   {:db (assoc db
               :common/page 1
               :common/search-text search-text)
    :fx [[:dispatch
          [::re-graph/query
           search-query
           {:text search-text}
           [:find/recieve-result]]]]}))

