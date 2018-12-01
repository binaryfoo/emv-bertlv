package io.github.binaryfoo

import java.io.File

object TestResources {

  @JvmStatic
  fun loadExpected(clazz: Class<*>, fileName: String): String {
    return File("src/test/resources/${clazz.simpleName}/$fileName").readText()
  }
}
