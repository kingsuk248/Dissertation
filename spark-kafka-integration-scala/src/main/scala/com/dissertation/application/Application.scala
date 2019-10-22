package com.dissertation.application

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.functions.from_json
import org.apache.spark.sql.functions.explode
import org.apache.spark.sql.functions._
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.streaming.Trigger

object Application {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("Dissertation_Spark-Kafka")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")
    
    val df = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "persons")
      .option("startingOffsets", "earliest")
      .load()

    val dummyDf = df.selectExpr("CAST(value AS STRING)")

    dummyDf.printSchema()

    val consoleOutput = dummyDf.writeStream
      .outputMode("append")
      .format("console")
      .start()
    consoleOutput.awaitTermination(5000)

    val schema = new StructType()
      .add("firstName", StringType)
      .add("lastName", StringType)
      .add("age", StringType)
    

    val dummy = df.selectExpr("CAST(value AS STRING)")
      .select(from_json(col("value"), schema).as("data"))
      .select("data.*")

    val query = dummy.writeStream
      .format("console")
      .outputMode("append")
      .start()
      .awaitTermination(5000)

    System.out.println("CODE Ends")
  }
  /*def main(args: Array[String]) {
    System.in.read();
    val spark = SparkSession
      .builder
      .appName("Spark-Kafka-Integration")
      .master("local")
      .getOrCreate()

    val lines = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "or1010051252233.corp.adobe.com:9092")
      .option("subscribe", "spooldir-search-topic")
      .option("startingOffsets", "earliest")
      .load
      .select(col("value").cast("string"))

    lines.writeStream.format("console").outputMode("append").trigger(Trigger.ProcessingTime("5 seconds")).start()

    /*val mySchema = StructType(Array(
      StructField("ad_group", StringType),
      StructField("device", StringType),
      StructField("search_engine", StringType),
      StructField("cost", StringType),
      StructField("clicks", StringType),
      StructField("cpc", StringType),
      StructField("avg_position", StringType),
      StructField("impressions", StringType),
      StructField("quote", StringType),
      StructField("leads", StringType),
      StructField("tenant_name", StringType),
      StructField("time_stamp", StringType)))*/

    /*val lines = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "or1010051252233.corp.adobe.com:9092")
      .option("subscribe", "spooldir-search-topic")
      .option("startingOffsets", "earliest")
      .load
      .select(col("value").cast("string")).alias("topic_values")*/

    /*val lines = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "or1010051252233.corp.adobe.com:9092")
      .option("subscribe", "spooldir-search-topic")
      .option("startingOffsets", "earliest")
      .load()
      .select(from_json(col("value").cast("string"), mySchema).alias("parsed_value"))*/

    //val parsedData = df.select(from_json(col("value").cast("string"), mySchema).alias("parsed_value"))
    //parsedData.printSchema()

    //lines.writeStream.format("console").outputMode("append").trigger(Trigger.ProcessingTime("5 seconds")).start().awaitTermination()
    //println(test)
    //df.select(df.col("parsed_value.ad_group"), df.col("data.keyNote"), df.col("data.details"))
    /*parsedData.writeStream.format("console").option("truncate", "false")
      .start()
      .awaitTermination()*/

    //parsedData.getAs[Row]("struct").getAs[String]("ad_group")
    /*val parsedData = df.select(
      col("key").cast("string"),
      from_json(col("value").cast("string"), mySchema))*/

  }*/
}