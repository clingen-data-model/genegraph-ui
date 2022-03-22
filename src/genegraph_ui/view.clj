(ns genegraph-ui.view)

(defmacro defview
  "Define a resolver given a view and a type.
  
  Scope should be one of full, compact, list item, or link. These will
  correspond with the multimethods.

  Type will be either a keyword corresponding to the curie for an RDF type,
  or a keyword corresponding to a GraphQL type. Resolution to RDF type will
  be preferred over GraphQL type, if an appropriate view exists.

  Fragment should be a complete GraphQL query fragment, sans name
  (i.e. \"{curie label}\"). The name will be generated based on the scope
  and type defined for the view.

  View should be a function returning Reagent/Hiccup compatible markup."
  [scope type fragment view]
  `(defmethod ~scope ~type ~view))
