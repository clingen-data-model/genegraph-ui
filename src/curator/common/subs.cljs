(ns curator.common.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::user
 (fn [db]
   (:user db)))

(re-frame/reg-sub
 ::user-is-registered
 (fn [db]
   (:user-is-registered db)))

(re-frame/reg-sub
 ::user-authorization
 (fn [db]
   (:user-authorization db)))

(re-frame/reg-sub
 ::user-is-admin
 (fn [db]
   (:admin-user db)))
