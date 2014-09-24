import static com.xlson.groovycsv.CsvParser.parseCsv

def m = [:]
parseCsv(new FileReader("src/main/resources/currency-codes-all.csv")).grep { it.NumericCode != "" }.each {
    m[it.NumericCode] = "${it.AlphabeticCode} (${it.Currency}) "
}
m.each { k,v ->
    println "$k, $v"
}