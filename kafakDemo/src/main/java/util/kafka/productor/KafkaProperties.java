/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package util.kafka.productor;

public interface KafkaProperties
{
//  final static String zkConnect = "spark001:2181,spark002:2181,spark003:2181";
  final static String zkConnect = "localhost:2181";
//  final static String zkConnect = "bi-test-004:2181/kafka01cluster";
  final static String broker_list = "localhost:9092" ;
//  final static String hbase_zkList = ",spark001,,spark002,,spark003" ; 
	
//  final static String zkConnect   = "192.168.113.80:2181,192.168.113.81:2181,192.168.113.82:2181";
//  final static String broker_list = "192.168.113.80:9092,192.168.113.81:9092,192.168.113.82:9092" ;
//  final static String hbase_zkList = "10.161.164.202,10.161.164.203" ;
  
  final static  String groupId = "gr";
  final static String topic = "test01";
  final static String Cell_Topic = "order5test";
}
