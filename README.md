# var-schema

I want to specify a schema for a var that contains a particular type.
Here it is, my over-lunch attempt

## Usage

If I release it on Clojars, it'll be:

    [jessitron/var-schema "0.1.0"]

And then in the code, something like:

    (require [var-schema.core :as vs]
             [schema.core :as s])

    (def VarContainingAString (vs/Var s/String))

    (s/defn function-that-accepts-a-var [stringvar :- VarContainingAString]
      (println "I have metadata: " (meta stringvar))
      (println "I contain this string: ->" (deref stringvar) "<-"))

## License

Copyright © 2014 Jessica Kerr

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
