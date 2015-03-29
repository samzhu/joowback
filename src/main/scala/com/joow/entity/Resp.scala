package com.joow.entity

/**
 * Created by SAM on 2015/2/27.
 */
case class Response (header:RsHeader, body:Map[Any,Any])

case class RsHeader (prc:String)
