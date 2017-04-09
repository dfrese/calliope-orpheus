(ns dfrese.calliope.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            dfrese.calliope.orpheus-test
            ))

(doo-tests 'dfrese.calliope.orpheus-test
           )

