(ns capital-gains.core
	(:require [capital-gains.logic.helpers :as h]
						[capital-gains.logic.json-to-list :as js]
						[capital-gains.logic.operation-reducer :as or]
						[clojure.string :as str]))

(defn split-str-at-pattern [str pattern]
	(str/split str pattern))

(defn fix-json-str [str]
	(str/replace str "] [" "]===["))

(defn foo [fo]
	(reduce or/operation-reducer (h/create-new-accumulator) (js/json-to-list fo)))

(defn processar-entrada [linha]
	(let [
				fixed   (fix-json-str linha)
				splited (split-str-at-pattern fixed #"===")
				result  (mapv #(foo %) splited)

				]

		(println "-----------------------------------------------------------------")
		(println "result" (mapv #(:tax %) result))
		(println "-----------------------------------------------------------------")
		(mapv #(:tax %) result)
		))

(defn -main [& args]
	(let [linha (read-line)]
		(processar-entrada linha)))

(processar-entrada "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}, {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 5000}] [{\"operation\":\"buy\", \"unit-cost\":20.00, \"quantity\": 10000}, {\"operation\":\"sell\", \"unit-cost\":10.00, \"quantity\": 5000}]")