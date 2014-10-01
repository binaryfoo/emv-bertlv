@Grab(group='com.xlson.groovycsv', module='groovycsv', version='1.0')
import static com.xlson.groovycsv.CsvParser.parseCsv

def m = [:]

parseCsv(new FileReader("src/main/resources/currency-codes-all.csv")).grep { it.NumericCode != "" }.each {
    m[it.NumericCode] = "${it.AlphabeticCode} (${it.Currency})"
}
new File("src/main/resources/numeric-currency-list.csv").withWriter { out -> 
    m.each{k,v -> out.writeLine("$k, $v") }
}

m.clear()

parseCsv(new FileReader("src/main/resources/country-codes.csv")).each {
    m[it[4]] = "${it[3]} (${it[0]})"
}
new File("src/main/resources/numeric-country-list.csv").withWriter { out -> m.each{k,v -> out.writeLine("$k, $v") } }