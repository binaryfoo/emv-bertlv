package io.github.binaryfoo.decoders

import io.github.binaryfoo.res.ClasspathIO
import java.util.*

/**
 * Read a CSV into a map. Say alpha to numeric code.
 *
 * @param source CSV file with source data
 * @param codeLength length of first column in source file (followed by comma, space and alpha-description of the code
 */
fun csvToMap(fileName: String, codeLength: Int): Map<String, String> {
    val map = HashMap<String, String>()
    ClasspathIO.readLines(fileName).forEach { line ->
        val numeric = line.substring(0, codeLength)
        val alpha = line.substring(codeLength + 2)
        map.put(numeric, alpha)
    }
    return map
}