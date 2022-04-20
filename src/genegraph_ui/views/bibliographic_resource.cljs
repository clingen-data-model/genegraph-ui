(ns genegraph-ui.views.bibliographic-resource
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
            [re-frame.core :refer [subscribe dispatch]]
            [reitit.frontend.easy :refer [href]]))

(defmethod render-link "BibliographicResource" [resource]
  ^{:key resource}
  [:a
   {:href (:iri resource)
    :on-click #(dispatch [:common/navigate-to-resource
                          (select-keys resource
                                       [:curie :type :__typename])])}
   (:short_citation resource)])
