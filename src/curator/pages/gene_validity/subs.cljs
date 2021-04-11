(ns curator.pages.gene-validity.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::assertion
 (fn [db]
   (:gene_validity_assertion db)))
