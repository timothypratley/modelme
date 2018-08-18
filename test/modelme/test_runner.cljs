(ns modelme.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [modelme.logic-test]))

(doo-tests 'modelme.logic-test)
