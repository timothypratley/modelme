(ns modelme.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [modelme.logic-tests]))

(doo-tests 'modelme.logic-tests)
