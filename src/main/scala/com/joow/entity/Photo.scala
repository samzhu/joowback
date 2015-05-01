package com.joow.entity

/**
 * Created by SAM on 2015/4/3.
 */
case class Photo(photoid: String,
                  rowdata: String,
                  datatype: String,
                  location: Option[Location],
                  createtime: String,
                  ownerid: String)


