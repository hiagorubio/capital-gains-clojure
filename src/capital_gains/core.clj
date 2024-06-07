(ns capital-gains.core
	(:require [capital-gains.logic.helpers :as h]
						[capital-gains.logic.json-to-list :as js]
						[capital-gains.logic.operation-reducer :as or]
						[clojure.string :as str]))

(defn split-str [str]
	(str/split str #"==="))

(defn fix-json-str [str]
	(str/replace str "] [" "]===["))

(defn reducer [input]
	(reduce or/operation-reducer (h/create-new-accumulator) (js/json-to-list input)))

(defn processar-entrada [input]
	(let [result (mapv reducer
								 (-> input
									 fix-json-str
									 split-str))]
		(mapv :tax result)
		))

(defn -main [& args]
	(let [linha (read-line)]
		(processar-entrada linha)))

;(processar-entrada "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}, {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 5000}] [{\"operation\":\"buy\", \"unit-cost\":20.00, \"quantity\": 10000}, {\"operation\":\"sell\", \"unit-cost\":10.00, \"quantity\": 5000}]")