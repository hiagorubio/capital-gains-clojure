(ns capital-gains.fixtures
  (:require [clojure.test :refer :all]))

(defn create-accumulator [wd quantity tax loss]
	{:weighted-average-price wd
	 :quantity               quantity
	 :tax                    tax
	 :loss                   loss
	 }
	)


(defn create-operation [operation unity-cost quantity]
	{
	 :operation operation :unit-cost unity-cost :quantity quantity
	 }
	)