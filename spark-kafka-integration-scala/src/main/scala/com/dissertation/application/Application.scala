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
import java.util.concurrent.atomic.AtomicLong
import org.apache.spark.sql.ForeachWriter
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.types.Decimal
import java.lang.Double
import java.util.ArrayList
import java.util.List

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
      .option("subscribe", "spooldir-search-topic")
      .option("startingOffsets", "latest")
      .load()

    val schema = new StructType()
      .add("tenantName", StringType)
      .add("cost", DoubleType)
      .add("clicks", IntegerType)
      .add("cpc", DoubleType)
      .add("avg_position", DoubleType)
      .add("impressions", IntegerType)
      .add("quote", DoubleType)
      .add("leads", IntegerType)

    val search = df.selectExpr("CAST(value AS STRING)")
      .select(from_json(col("value"), schema).as("data"))
      .select("data.*")

    
    val cost_column = search.select("cost")
    val cost_sum = cost_column.agg(sum("cost"))

    val query = cost_sum.writeStream
      .format("console")
      .outputMode("update")
      .start()
      .awaitTermination()
  }
}