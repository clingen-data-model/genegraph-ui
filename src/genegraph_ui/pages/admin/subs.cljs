(ns genegraph-ui.pages.admin.subs
  (:require [re-frame.core :as re-frame]
            [clojure.set :as set]))


(re-frame/reg-sub
 ::user-result
 (fn [db]
   (:user-result db)))

(re-frame/reg-sub
 ::groups
 (fn [db]
   (:groups db)))

(re-frame/reg-sub
 ::groups-with-user-result-memberships
 (fn [db]
   (let [user-groups (into #{} (map :curie (get-in db [:user-result :member_of])))]
     (map #(assoc %
                  :user-is-member
                  (user-groups (:curie %)))
          (:groups db)))))

(re-frame/reg-sub
 ::current-section
 (fn [db]
   (:admin/section db)))
