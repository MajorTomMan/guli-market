###
 # @Author: flashnames 765719516@qq.com
 # @Date: 2022-05-14 11:02:02
 # @LastEditors: flashnames 765719516@qq.com
 # @LastEditTime: 2023-02-18 17:40:56
 # @FilePath: /common/home/master/project/GuliMall/dockerCommand/dockerContainer.sh
 # @Description: 
 # 
 # Copyright (c) 2022 by flashnames 765719516@qq.com, All Rights Reserved. 
### 


sudo docker run -p 3300:3306 --name mysql --restart=always \
-v /home/master/mysql/sql:/sql \
-v /home/master/mysql/conf:/etc/mysql/conf.d \
-v /home/master/mysql/data:/var/lib/mysql \
-v /home/master/mysql/logs:/var/log/mysql \
-v /home/master/mysql/mysql-files:/var/lib/mysql-files \
-e MYSQL_ROOT_PASSWORD=981221 \
-d mysql:8.0.27

docker run -p 6379:6379 --name redis --restart=always   \
-v /home/master/redis/data:/data \
-v /home/master/redis/conf/redis.conf:/etc/redis/redis.conf \
-d redis redis-server /etc/redis/redis.conf --appendonly yes

sudo docker cp -a 04728f6460d8:/etc/redis/redis.conf /home/master/redis/conf/redis.conf
 
sudo docker run -it --name=nginx --privileged=true --restart=always -p 80:80 \
-v /home/master/nginx/nginx.conf:/etc/nginx/nginx.conf \
-v /home/master/nginx/html:/usr/share/nginx/html \
-v /home/master/nginx/logs:/var/log/nginx \
-v /home/master/nginx/conf.d:/etc/nginx/conf.d \
-d nginx

docker run --name es --privileged=true --restart=always -p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" -e ES_JAVA_OPTS="-Xms512m -Xmx512m" \
-v /home/master/es/config:/usr/share/elasticsearch/config \
-v /home/master/es/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /home/master/es/data/:/usr/share/elasticsearch/data \
-v /home/master/es/plugins:/usr/share/elasticsearch/plugins \
-d elasticsearch:8.2.0

# docker cp 95bd972d43c0:/usr/share/elasticsearch/config/ ./es/config/


sudo docker exec -it -u root es bash
./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v8.2.0/elasticsearch-analysis-ik-8.2.0.zip

docker run --name=head --restart=always -p 9100:9100 mobz/elasticsearch-head:5

docker run --name kibana --restart=always -p 5601:5601 -d kibana:8.2.0

docker run --name=rabbit --privileged=true --restart=always -p 15672:15672 -p 5672:5672 \
-v /home/master/rabbitmq/data:/var/lib/rabbitmq \
-v /home/master/rabbitmq/conf:/etc/rabbitmq \
-v /home/master/rabbitmq/logs:/var/log/rabbitmq \
-e RABBITMQ_DEFAULT_USER=master -e RABBITMQ_DEFAULT_PASS=981221 -d rabbitmq:management

sudo docker cp -a 9a9909fc25df:/etc/rabbitmq ./rabbitmq/conf
sudo docker cp -a 9a9909fc25df:/var/lib/rabbitmq ./rabbitmq/data
sudo docker cp -a 9a9909fc25df:/var/log/rabbitmq ./rabbitmq/logs


docker exec -it 9a9909fc25df /bin/bash


sudo docker run -d -p 8848:8848 -p 9848:9848 -p 9849:9849 \
-e MODE=standalone \
-e PREFER_HOST_MODE=ip \
-e MYSQL_SERVICE_HOST=localhost \
-v /home/master/nacos/init.d/custom.properties:/home/nacos/init.d/custom.properties \
-v /home/master/nacos/logs:/home/nacos/logs \
-v /home/master/nacos/plugins/mysql:/home/nacos/plugins/mysql \
-v /home/master/nacos/conf:/home/nacos/conf \
-v /home/master/nacos/bin:/home/nacos/bin \
--restart always --name nacos nacos/nacos-server


sudo docker cp -a bdf60dc2ada3:/home/nacos/conf ./nacos/conf

# nacos 备忘
# Mysql的版本和Mysql-Connector版本必须一致且不能为最新版本
# 用于nacos的MySQL-Connector的版本必须和docker上正在跑的一致
# 必须满足有Mysql-Connector和nacos_config数据库都有的情况下nacos才能跑起来