(ns capital-gains.logic.json-to-list
	(:require [schema.core :as s]
						[clojure.data.json :as j]
						[capital-gains.models :as m])

	)

(s/defn json-to-list [json-str] :- [m/Operation]

	(j/read-str json-str :key-fn keyword))