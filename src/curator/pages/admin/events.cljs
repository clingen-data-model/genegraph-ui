(ns curator.pages.admin.events
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [cljs.pprint]))

(def find-user-query
"query($email: String) {
  user_query(email: $email) {
    label
    email
    member_of {
      label
      iri
      curie
    }
  }
}")

(def groups-query
  "{
  groups_query {
    label
    curie
    iri
  }
}")

(re-frame/reg-event-fx
 :admin/create-user
 (fn [_ [_ params]]
   (println "in create-user")
   (cljs.pprint/pprint params)))

(re-frame/reg-event-fx
 :admin/fetch-groups
 (fn [_ _]
   {:fx [[:dispatch
          [::re-graph/query
           groups-query
           {}
           [:admin/recieve-groups]]]]}))

(re-frame/reg-event-db
 :admin/recieve-groups
 (fn [db [_ {:keys [data errors]}]]
   (when data
     (assoc db :groups (:groups_query data)))))

(re-frame/reg-event-db
 :admin/recieve-find-user-query
 (fn [db [_ {:keys [data errors]}]]
   (cljs.pprint/pprint data)
   (cljs.pprint/pprint errors)
   (assoc db :user-result (:user_query data))))

(re-frame/reg-event-fx
 :admin/send-find-user-query
 (fn [_ [_ email]]
   (println "finding user " email)
   {:fx [[:dispatch [::re-graph/query
                     find-user-query
                     {:email email}
                     [:admin/recieve-find-user-query]]]
         [:dispatch [::re-graph/query
                     groups-query
                     {}
                     [:admin/recieve-groups]]]]}))

(re-frame/reg-event-fx
 :admin/set-section
 (fn [{:keys [db]} [_ section]]
   {:db (assoc db :admin/section section)
    :fx [[:dispatch [:admin/fetch-groups]]]}))
