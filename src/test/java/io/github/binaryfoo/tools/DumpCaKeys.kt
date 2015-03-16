package io.github.binaryfoo.tools

import org.jsoup.Jsoup
import java.io.File
import java.util.ArrayList

// Get the table from https://www.eftlab.com.au/index.php/site-map/knowledge-base/243-ca-public-keys
// Dump to a .csv file (that's actually tab separated...)

fun main(args: Array<String>) {
    val doc = Jsoup.parse(File("ca-public.html"), Charsets.UTF_8.name())
    val rows = doc.select("tr")
    val currentKeys = File("src/main/resources/ca-public-keys.txt").readLines()
    val newKeys = ArrayList(currentKeys)
    rows.forEach { row ->
        // Issuer    Exponent	RID Index	RID List	Modulus	Key length	SHA1
        // AMEX	03	03	A000000025	B0C2C6E2A6386933CD17C239496BF48C57E389164F2A96BFF133439AE8A77B20498BD4DC6959AB0C2D05D0723AF3668901937B674E5A2FA92DDD5E78EA9D75D79620173CC269B35F463B3D4AAFF2794F92E6C7A3FB95325D8AB95960C3066BE548087BCB6CE12688144A8B4A66228AE4659C634C99E36011584C095082A3A3E3	1024	8708A3E3BBC1BB0BE73EBD8D19D4E5D20166BF6C
        val key = row.select("td").map { it.text() }.join("\t")
        if (!currentKeys.contains(key)) {
            newKeys.add(key)
        }
    }

    File("new-ca-public-keys.txt").writeText(newKeys.join("\n"))
}

