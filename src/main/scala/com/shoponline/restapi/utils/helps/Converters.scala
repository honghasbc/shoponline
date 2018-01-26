package com.shoponline.restapi.utils.helps

import java.{sql, util}
import java.sql.Date

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}

object Converters {
  def stringToInt(str:String): Int =
    Integer.parseInt(str);

  val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
  def stringToDate(str:String): util.Date =
    format.parse(str);

  def stringToDateSql(str:String): sql.Date =
    new java.sql.Date(format.parse(str).getTime);

  implicit val DateFormat : Encoder[Date] with Decoder[Date] = new Encoder[Date] with Decoder[Date] {
    override def apply(a: Date): Json = Encoder.encodeLong.apply(a.getTime)

    override def apply(c: HCursor): Result[Date] = Decoder.decodeLong.map(s => new Date(s)).apply(c)
  }

//  val utilDate2SqlTimestampMapper = MappedColumnType.base[java.util.Date, java.sql.Timestamp](
//    { utilDate => new java.sql.Timestamp(utilDate.getTime()) },
//    { sqlTimestamp => new java.util.Date(sqlTimestamp.getTime()) })
//
//  val utilDate2SqlDate = MappedColumnType.base[java.util.Date, java.sql.Date](
//    { utilDate => new java.sql.Date(utilDate.getTime()) },
//    { sqlDate => new java.util.Date(sqlDate.getTime()) })
}
