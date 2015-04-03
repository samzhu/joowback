package com.joow.test

import java.io.File
import java.nio.file.{Paths, Files}
import net.sf.jmimemagic.{MagicMatch, Magic}

/**
 * Created by SAM on 2015/4/3.
 */
object ImageTest {
  def main (args: Array[String]) {

    val in = Files.readAllBytes(Paths.get("D:/gcm-a-modr.png"))

    //System.out.println(Files.probeContentType(in));
    val matchs:MagicMatch = Magic.getMagicMatch(in)
    System.out.println(matchs.getMimeType())

    //new MimetypesFileTypeMap().get
    //val image = Image(in)



    //println(image.toBufferedImage.getType)




  }


}
