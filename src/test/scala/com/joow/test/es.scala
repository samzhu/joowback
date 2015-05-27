package com.joow.test

import com.joow.entity.Account
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.elasticsearch.search.SearchHits

import scala.collection.mutable.ListBuffer

/**
 * Created by SAM on 2015/3/8.
 */
object es {
  def main(args: Array[String]): Unit = {
    val client = ElasticClient.remote("127.0.0.1", 9300)
    val resp = client.execute {
      //search in "joow/account" query "帥"
      //search in "joow" / "account" query  { term("nickname", "小朱") }
      //search in "joow/account" query("nickname", "小朱")

      //search in "joow/account" query termQuery("nickname" -> "小朱")
      //search in "joow/account" query matchQuery("nickname", "小朱")
      //search in "joow/account" query nestedQuery("nickname").query(termQuery("account.nickname" -> "帥哥"))

      //search in "joow" -> "account" query "小" fields "nickname"

      /*
      search in "joow" / "account" postFilter {
        and(
          termFilter("nickname", "michon")
        )
      }
      */
      val keywordhits: ListBuffer[String] = new ListBuffer()
      keywordhits += "別羨慕哥"
      keywordhits += "帥"
      search in "joow/posts" query {
        bool {
          should(
            //term("tags" -> "哥")
            matchQuery("title", keywordhits) boost 3,
            matchQuery("content", keywordhits) boost 1,
            matchQuery("tags", keywordhits) boost 5
          )
        }
      }
    }.await
    client.close()
    println("Hits=" + resp.getHits.getTotalHits)
    val sh: SearchHits = resp.getHits
    println(resp.getHits.getHits.size)



    println("size=" + resp.getHits.getHits.size)

    //println(resp.getHits.getHits()(0))
    //resp.getHits.getHits().foreach( u =>
    //  println(u.field("nickname"))
    //)
    //println(resp.getHits.getHits()(0).sourceAsMap.get("nickname").toString)

    //val acc = Account(resp.getHits.getHits()(0))
    //println(acc.s);

    //println(acc.json);
    //resp.getHits.getHits.foreach(u =>
    //println(u.fields().get("nickname").getValues)
    //)

  }
}
