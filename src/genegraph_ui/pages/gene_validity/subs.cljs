(ns genegraph-ui.pages.gene-validity.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::assertion
 (fn [db]
   (:gene_validity_assertion db)))

(re-frame/reg-sub
 ::assertion-list
 (fn [db]
   (:gene_validity_assertions db)))

(re-frame/reg-sub
 ::show-description
 (fn [db]
   (:gene-validity/show-description db)))
