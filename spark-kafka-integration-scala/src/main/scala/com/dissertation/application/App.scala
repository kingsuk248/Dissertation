package com.dissertation.application

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.functions.from_json
import org.apache.spark.sql.functions.col
import java.util.ArrayList

/**
 * Spark Application
 * 
 * Aggregates data from Kafka topics and writes to downstream
 * Kafka topics and MongoDB
 */
class App {
  def main(args: Array[String]): Unit = {
    process(args)
  }

  def process(args: Array[String]) = {
    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("Dissertation_Spark-Kafka")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    val df = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", Constants.kafkaBootstrapServer)
      .option("subscribe", Constants.kafkaUpstreamTopcis)
      .option("startingOffsets", "latest")
      .load()

    val searchSchema = getSearchSchema()
    val displaySchema = getDisplaySchema()
    val socialSchema = getSocialSchema()

    val searchData = df.selectExpr("CAST(value AS STRING)")
      .select(from_json(col("value"), searchSchema).as("data"))
      .select("data.*")

    val displayData = df.selectExpr("CAST(value AS STRING)")
      .select(from_json(col("value"), displaySchema).as("data"))
      .select("data.*")

    val socialData = df.selectExpr("CAST(value AS STRING)")
      .select(from_json(col("value"), socialSchema).as("data"))
      .select("data.*")

    val dataAggregator = new Aggregator()

    val searchAggregatedData = dataAggregator.aggregateSearchData(searchData)
    val displayAggregatedData = dataAggregator.aggregateDisplayData(displayData)
    val socialAggregatedData = dataAggregator.aggregateSocialData(socialData)
    
    val sc = spark.sparkContext
    val searchRdd = sc.parallelize(Seq(searchAggregatedData))
    val displayRdd = sc.parallelize(Seq(displayAggregatedData))
    val socialRdd = sc.parallelize(Seq(socialAggregatedData))
    
    val searchKafkaOutput = df.writeStream
      .format("kafka")
      .option("kafka.bootstrap.servers", Constants.kafkaBootstrapServer)
      .option("topic", Constants.downstreamSearchTopic)
      .option("checkpointLocation", Constants.checkpointLocation)
      .start()

    val displayKafkaOutput = df.writeStream
      .format("kafka")
      .option("kafka.bootstrap.servers", Constants.kafkaBootstrapServer)
      .option("topic", Constants.downstreamDisplayTopic)
      .option("checkpointLocation", Constants.checkpointLocation)
      .start()

    val socialKafkaOutput = df.writeStream
      .format("kafka")
      .option("kafka.bootstrap.servers", Constants.kafkaBootstrapServer)
      .option("topic", Constants.downstreamSocialTopic)
      .option("checkpointLocation", Constants.checkpointLocation)
      .start()

    searchKafkaOutput.awaitTermination()
    displayKafkaOutput.awaitTermination()
    socialKafkaOutput.awaitTermination()
  }

  def getSearchSchema(): StructType = {
    val searchSchema = new StructType()
      .add("tenantName", StringType)
      .add("cost", DoubleType)
      .add("clicks", IntegerType)
      .add("cpc", DoubleType)
      .add("avg_position", DoubleType)
      .add("impressions", IntegerType)
      .add("quote", DoubleType)
      .add("leads", IntegerType)
    return searchSchema
  }

  def getDisplaySchema(): StructType = {
    val displaySchema = new StructType()
      .add("tenantName", StringType)
      .add("cost", DoubleType)
      .add("clicks", IntegerType)
      .add("cpc", DoubleType)
      .add("avg_position", DoubleType)
      .add("impressions", IntegerType)
      .add("quote", DoubleType)
      .add("rtb_source", IntegerType)
    return displaySchema
  }

  def getSocialSchema(): StructType = {
    val socialSchema = new StructType()
      .add("tenantName", StringType)
      .add("cost", DoubleType)
      .add("clicks", IntegerType)
      .add("cpc", DoubleType)
      .add("avg_position", DoubleType)
      .add("impressions", IntegerType)
      .add("ctr", DoubleType)
      .add("e_pcm", IntegerType)
    return socialSchema
  }
}