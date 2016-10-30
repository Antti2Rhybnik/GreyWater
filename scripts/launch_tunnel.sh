#!/usr/bin/env bash

#Первым аргументом первый раз закиньте путь до neo.sh
path_to_neo=""


if [ -z "$1" ]
  then
    path_to_neo=$(head -1 path_config)
  else
    path_to_neo="$1"
    echo "$1">path_config
fi
(cd ${path_to_neo}; ./neo.sh open-db-tunnel -h hanatrial.ondemand.com -a p1942206778trial -u electrognom@yandex.ru -p Q548137q! -i db2)