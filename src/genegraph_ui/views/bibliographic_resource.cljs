(ns genegraph-ui.views.bibliographic-resource
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
            [reitit.frontend.easy :refer [href]]))

(defmethod render-link "BibliographicResource" [resource]
  ^{:key resource}
  [:a
   {:href (:iri resource)}
   (:short_citation resource)])
