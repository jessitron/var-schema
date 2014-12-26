(ns var-schema.core
  (require [schema.core :refer [Schema subschema-walker explain both pred]]
           [schema.macros :as macros])
  )

;; as viewers of countless TV shows and movies will know, nerds are brilliant at fixing anything but unable to handle even the simplest interaction or emotional nuance.


;; I'm not so sure this is really FMap
(clojure.core/defrecord FMap [f schema]
  Schema
  (walker [this]
          (let [sub-walker (subschema-walker schema)]
            (clojure.core/fn [x]
              (try
                (sub-walker (f x))
                (catch Exception e
                   (macros/validation-error this x (list 'fmap f (explain schema)) (str "Exception thrown during function evaluation: " (.getMessage e))))))))
  (explain [this] (list 'fmap f (explain schema)))) ;; I don't know how to make this display f

(clojure.core/defn fmap
  "A value that, passed to f, leads to a result that must satisfy schema"
  [f schema]
    (FMap. f schema))

(clojure.core/defn Var
  "A var, which contains a value that must satisfy schema"
  [schema]
  (both (pred var?) (fmap deref schema)))
