package com.dissertation.application

import com.mongodb.spark._
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.mongodb.spark.config.WriteConfig
import com.fasterxml.jackson.databind.ObjectMapper
import java.text.SimpleDateFormat
import java.util.Calendar
/**
 * Write aggregated data to MongoDB
 */
object MongoWriter {
  
  def getSparkContext(): SparkContext = {
    getSparkSession(Constants.mongodbDatabase, Constants.mongodbSearchCollection).sparkContext
  }

  def getSparkSession(database: String, collection: String): SparkSession = {

    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("MongoSparkConnectorTour")
      .set("spark.app.id", "MongoSparkConnectorTour")
      .set("spark.mongodb.input.uri", Constants.mongodbUri)
      .set("spark.mongodb.input.database", database)
      .set("spark.mongodb.input.collection", collection)
      .set("spark.mongodb.output.uri", Constants.mongodbUri)
      .set("spark.mongodb.output.database", database)
      .set("spark.mongodb.output.collection", collection)
      .set("primaryPreferred", "primaryPreferred")

    val session = SparkSession.builder().config(conf).getOrCreate()
    MongoConnector(session.sparkContext).withDatabaseDo(WriteConfig(session), { db => db.drop() })
    session
  }
  
  def writeSearchObjectToDB(search: Search): Unit = {
    val sc = getSparkContext()
    import com.mongodb.spark._

    import org.bson.Document
    import scala.collection.JavaConverters._
    val mapper = new ObjectMapper()
    val searchJson = mapper.writeValueAsString(search);
    val format = new SimpleDateFormat(Constants.mongodbDateKeyFormat)
    val todayDate = format.format(Calendar.getInstance().getTime())
    val searchDocument = sc.parallelize(
     Seq(new Document(todayDate, searchJson)))
    MongoSpark.save(searchDocument)
  }
  
  def writeDisplayObjectToDB(display: Display): Unit = {
    val sc = getSparkContext()
    import com.mongodb.spark._

    import org.bson.Document
    import scala.collection.JavaConverters._
    val mapper = new ObjectMapper()
    val displayJson = mapper.writeValueAsString(display);
    val format = new SimpleDateFormat(Constants.mongodbDateKeyFormat)
    val todayDate = format.format(Calendar.getInstance().getTime())
    val displayDocument = sc.parallelize(
     Seq(new Document(todayDate, displayJson)))
    MongoSpark.save(displayDocument)
  }
  
  def writeSocialObjectToDB(social: Social): Unit = {
    val sc = getSparkContext()
    import com.mongodb.spark._

    import org.bson.Document
    import scala.collection.JavaConverters._
    val mapper = new ObjectMapper()
    val socialJson = mapper.writeValueAsString(social);
    val format = new SimpleDateFormat(Constants.mongodbDateKeyFormat)
    val todayDate = format.format(Calendar.getInstance().getTime())
    val socialDocument = sc.parallelize(
     Seq(new Document(todayDate, socialJson)))
    MongoSpark.save(socialDocument)
  }
}