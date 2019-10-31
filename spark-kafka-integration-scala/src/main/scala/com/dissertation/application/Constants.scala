package com.dissertation.application

import java.time.format.DateTimeFormatter

object Constants {
  val kafkaBootstrapServer = "localhost:9092"
  
  val upstreamSearchTopic = "spooldir-search-topic";
  val upstreamDisplayTopic = "spooldir-display-topic";
  val upstreamSocialTopic = "spooldir-social-topic";
  val kafkaUpstreamTopcis = "spooldir-search-topic,spooldir-display-topic,spooldir-social-topic";
  
  val downstreamSearchTopic = "downstream-search-topic";
  val downstreamDisplayTopic = "downstream-display-topic";
  val downstreamSocialTopic = "downstream-social-topic";
  
  val checkpointLocation = "C:\\kafka\\checkpoints";
  
  val mongodbUri = "mongodb://localhost:27017";
  val mongodbDatabase = "dissertationDB";
  val mongodbSearchCollection = "searchColl";
  val mongodbDateKeyFormat = "dd-MM-YYYY";
  
}