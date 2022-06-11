#! /bin/bash -e

docker run $* \
   --name mysqlterm --network=${PWD##*/}_default --rm \
   mysql/mysql-server:8.0.27-1.2.6-server \
   sh -c 'exec mysql -h mysql  -uroot -prootpassword -o eventuate'
