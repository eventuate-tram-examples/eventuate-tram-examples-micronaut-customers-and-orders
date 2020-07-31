#! /bin/bash

set -e

export DATABASE=postgres
export MODE=wal

export MICRONAUT_ENVIRONMENTS=postgres

./_build-and-test-all.sh
