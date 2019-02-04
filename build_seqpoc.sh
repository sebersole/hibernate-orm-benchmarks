#!/usr/bin/env bash

 ./gradlew -p seqpoc --no-daemon clean runShadow --args='-wi 10 -i 20 -f 1 -o seqpoc_report.jmh'
