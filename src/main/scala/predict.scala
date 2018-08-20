/**
  * This object is for locally linear deep factorization machines inference
 */

import org.apache.spark.rdd.RDD

import scala.io._
import DeepFM.DeepFm
import scala.Array._

import scala.collection.mutable.ArrayBuffer

/**
  * Feature format
  * xi: non-zero index
  * xv: corresponding value
  * -----------------------
  * Model format
  * anchor points indicates the number of deep_fm model
  * each deep_fm contains
  * 1. anchor point
  * 2. fm second embedding
  * 3. dnn input and hidden layer weights
  */

object predict {

  def main(args: Array[String]): Unit = {
    // num of anchor points
    val num_anchor = 20

    // num of nearest neighbours
    val num_nn = 3

    // deep layer
    val input_width = 32

    val hidden_width = 32

    // feature number
    val num_feature = 1935

    val c = 0.01
    val embedding_size = 4

    // field dict
    val field_info = Array(29, 33, 34, 35, 51, 201, 351, 501, 651, 801, 951, 1101, 1251, 1301, 1601, 1636, 1731, 1801, 1950)

    // fm embedding length
    /*
    load fm embedding and nn for each anchor point
    load from table online
    mock by load from file offline
     */

    val table_name = new Array[String](num_anchor)
    for (i <- 0 until num_anchor){
      table_name(i) = "data/deep_fm_" + i
    }

    for (i <- table_name.indices){
      var lines = Source.fromFile(table_name(i)).getLines()
      // anchor point
      var anchor_point = lines.next()
      var tokens = anchor_point.split(" ")
      var ap = new Array[Double](tokens.length)
      for (i <- ap.indices){
        ap(i) = tokens(i).toDouble
      }

      // fm bias
      var fm_bias = lines.next().toDouble

      // fm embedding
      var fm_embedding = new ArrayBuffer[Any]()
      for (i <- field_info.indices){
        var rows = 0
        if (i==0){
          rows = field_info(i) + 1
        }
        else{
          rows = field_info(i) - field_info(i-1)
        }

        var value = lines.next().split(" ")
        var field_embedding = ofDim[Double](rows, embedding_size)

        for (i <- value.indices) {
          field_embedding(i / embedding_size)(i % embedding_size) = value(i).toDouble
        }

        fm_embedding = fm_embedding ++ field_embedding

      }

      // input layer weight
      var input_layer_weight = ofDim[Double](field_info.length * embedding_size)

      // input layer bias
      var input_layer_bias = new Array[Double](input_width)

      // hidden layer weight
      var hidden_layer_weight = ofDim[Double](input_width, hidden_width)

      // hidden layer bias
      var hidden_layer_bias = new Array[Double](hidden_width)


    }


  }
}
