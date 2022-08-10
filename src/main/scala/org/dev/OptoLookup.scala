package org.dev
import scalaj.http.{Http, HttpOptions}
import scala.collection.mutable.Map
import scala.collection.immutable.ListMap
import scala.collection.mutable


object OptoLookup {

  val baseUrl: String = "https://gist.githubusercontent.com/akozumpl/6b5b1b4b670df57db574d7ccd1b2aaff/raw/ab243b481838e4baf3dd8083993f79cd6c3bb587/corpus-short.txt"

  def callapi(urlParam : String, prefix : String): Unit = {
    var tempUrl = urlParam
    //    var url = "https://gist.githubusercontent.com/akozumpl/61e98ab8aae99762a517656a61436e65/raw/23881f9513e31b7ca295b4da9f77028db1a91f91/corpus.txt"
    if (!urlParam.startsWith("https://gist.githubusercontent.com/akozumpl/")) {
      tempUrl = baseUrl;
    }
    var result = Http(tempUrl).asString;
    var wordlist = result.body.split(" ");
    var myMap: mutable.Map[String, Int] = Map()

    for (word <- wordlist) {
      if (word.startsWith(prefix)) {
        val elem = myMap.get(word)
        if (elem == None) {
          myMap += (word -> 1)
        } else {
          myMap.update(word, elem.get + 1)
        }
      }
    }

    val res = ListMap(myMap.toSeq.sortWith(_._2 > _._2): _*)

    printResults(wordlist, res)

  }


  private def printResults(wordlist: Array[String], res: ListMap[String, Int]) = {
    println("{")
    print("suggestions: ")
    print("[")
    for (str <- res.keySet) {
      print("\"" + str + "\",")
    }
    println("]")
    println("corpusSize: " + wordlist.size)
    println("}")
  }

  def main(args: Array[String]): Unit ={
    if(args.length<3){
      println("USAGE :  scala optolookup.jar [-c <corpus-url> ] <prefix>")
    } else {
      callapi(args(1), args(2))
    }
  }

}
