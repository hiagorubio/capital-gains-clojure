(ns capital-gains.models
	(:require [schema.core :as s]))


(s/def OperationType (s/enum :buy :sell))

(s/defschema Operation
	"Schema da operacao"
	{:operation s/Str, :unity-cost s/Num, :quantity s/Str})

(s/def Tax {:tax s/Num})

(s/defschema Accumulator
	{:weighted-average-price s/Num
	 :quantity               s/Int
	 :tax                    [Tax]
	 :loss                   s/Num
	 }
	)