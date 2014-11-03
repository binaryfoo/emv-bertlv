package io.github.binaryfoo.decoders

import java.util.HashMap
import io.github.binaryfoo.res.ClasspathIO

/**
 * Read a CSV into a map. Say alpha to numeric code.
 *
 * @param source CSV file with source data
 * @param codeLength length of first column in source file (followed by comma, space and alpha-description of the code
 */
fun csvToMap(fileName: String, codeLength: Int): Map<String, String> {
    val map = HashMap<String, String>()
    map.putAll(ClasspathIO.readLines(fileName).map { line ->
        val numeric = line.substring(0, codeLength)
        val alpha = line.substring(codeLength + 2)
        numeric to alpha
    })
    return map
}