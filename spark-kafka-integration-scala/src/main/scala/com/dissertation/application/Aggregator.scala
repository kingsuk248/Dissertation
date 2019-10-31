package com.dissertation.application

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.types.DataTypes
import org.apache.spark.sql.functions._
import java.time.LocalDate
import java.time.Period

class Aggregator {
  def aggregateSearchData(df: DataFrame): Search = {
    val cost = df.select("cost").agg(sum("cost")).asInstanceOf[Double]
    val clicks = df.select("cost").agg(sum("clicks")).asInstanceOf[Integer]
    val cpc = (cost / clicks).asInstanceOf[Double]
    val avg_postion = df.select("avg_position").agg(avg("avg_position")).asInstanceOf[Double]
    val impressions = df.select("impressions").agg(sum("impressions")).asInstanceOf[Integer]
    val quote = (cost * clicks) / impressions
    val leads = df.select("quote").agg(sum("leads")).asInstanceOf[Integer]
    val search = new Search()
    search.setCost(cost)
    search.setClicks(clicks)
    search.setCpc(cpc)
    search.setAvg_position(avg_postion)
    search.setImpressions(impressions)
    search.setQuote(quote)
    search.setLeads(leads)
    return search
  }

  def aggregateDisplayData(df: DataFrame): Display = {
    val cost = df.select("cost").agg(sum("cost")).asInstanceOf[Double]
    val clicks = df.select("cost").agg(sum("clicks")).asInstanceOf[Integer]
    val cpc = (cost / clicks).asInstanceOf[Double]
    val avg_postion = df.select("avg_position").agg(avg("avg_position")).asInstanceOf[Double]
    val impressions = df.select("impressions").agg(sum("impressions")).asInstanceOf[Integer]
    val quote = (cost * clicks) / impressions
    val rtb_source = df.select("rtb_source").collect.mkString(",")
    val display = new Display()
    display.setCost(cost)
    display.setClicks(clicks)
    display.setCpc(cpc)
    display.setAvg_position(avg_postion)
    display.setImpressions(impressions)
    display.setQuote(quote)
    display.setRtb_source(rtb_source)
    return display
  }

  def aggregateSocialData(df: DataFrame): Social = {
    val cost = df.select("cost").agg(sum("cost")).asInstanceOf[Double]
    val clicks = df.select("cost").agg(sum("clicks")).asInstanceOf[Integer]
    val cpc = (cost / clicks).asInstanceOf[Double]
    val avg_postion = df.select("avg_position").agg(avg("avg_position")).asInstanceOf[Double]
    val impressions = df.select("impressions").agg(sum("impressions")).asInstanceOf[Integer]
    val ctr = (cost * clicks) / impressions
    val e_pcm = df.select("e_pcm").agg(avg("e_pcm")).asInstanceOf[Double]
    val social = new Social()
    social.setCost(cost)
    social.setClicks(clicks)
    social.setCpc(cpc)
    social.setAvg_position(avg_postion)
    social.setImpressions(impressions)
    social.setE_pcm(e_pcm)
    return social
  }
}