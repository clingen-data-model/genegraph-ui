(ns genegraph-ui.query)

;; TODO two different macros, defpartial (instead of defview) and defpage
;; defpartial includes

(defmacro defpage
  ([resolved-type view]
   `(defmethod
      genegraph-ui.protocols/render-full
      ~resolved-type
      ~view))
  ([resolved-type query view]
   `(do
      (defmethod
        genegraph-ui.query/query-for-resource
        ~resolved-type
        ([~(gensym)] ~query))
      (defmethod
        genegraph-ui.protocols/render-full
        ~resolved-type
        ~view))))

(defmacro defpartial
  ([scope resolved-type view]
   `(defmethod
      ~(symbol "genegraph-ui.protocols" (str "render-" scope))
      ~resolved-type
      ~view))
  ([scope resolved-type fragment view]
   `(do
      (genegraph-ui.query/add-fragment
       {:scope (keyword '~scope)
        :type ~resolved-type
        :fragment ~fragment})
      (defmethod
        ~(symbol "genegraph-ui.protocols" (str "render-" scope))
        ~resolved-type
        ~view))))
