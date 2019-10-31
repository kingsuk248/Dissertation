package com.dissertation.application

import org.apache.spark.sql.DataFrame

/**
 * Write aggregated data to downstream Kafka topics
 * 
 */
class KafkaWriter {
   def writeSearchAggregateToKafka(searchDf: DataFrame) = {
     val searchKafkaOutput = searchDf.writeStream
      .format("kafka")
      .option("kafka.bootstrap.servers", Constants.kafkaBootstrapServer)
      .option("topic", Constants.downstreamSearchTopic)
      .option("checkpointLocation", Constants.checkpointLocation)
      .start()

    searchKafkaOutput.awaitTermination()
   }
   
   def writeDisplayAggregateToKafka(displayDf: DataFrame) = {
    val displayKafkaOutput = displayDf.writeStream
      .format("kafka")
      .option("kafka.bootstrap.servers", Constants.kafkaBootstrapServer)
      .option("topic", Constants.downstreamDisplayTopic)
      .option("checkpointLocation", Constants.checkpointLocation)
      .start()

    displayKafkaOutput.awaitTermination()
   }
   
   def writeSocialAggregateToKafka(socialDf: DataFrame) = {
    val socialKafkaOutput = socialDf.writeStream
      .format("kafka")
      .option("kafka.bootstrap.servers", Constants.kafkaBootstrapServer)
      .option("topic", Constants.downstreamSocialTopic)
      .option("checkpointLocation", Constants.checkpointLocation)
      .start()

    socialKafkaOutput.awaitTermination()
   }
}