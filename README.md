What is this thing?
===================

A library for deciphering [BER TLV](http://en.wikipedia.org/wiki/X.690#BER_encoding) data used in [EMV](http://en.wikipedia.org/wiki/EMV) (chip card transactions).

You might be wondering what some of the strange characters on a credit card receipt mean. Or you might be trying to achieve certification with Visa, Mastercard or Amex (perhaps even UnionPay). 

The tests provide some idea of what kind of data this code can decipher. The [EMVCo](http://www.emvco.com/specifications.aspx) website has the full (1000+ page) specs. Add to that the Visa, Mastercard and Amex documents.

How might I use it?
===================

In a couple of ways:

1. As a library in a java (or other JVM language) project doing wonderful things with chip card data.
2. On the web in a little [tool](http://tvr-decoder.appspot.com/t/home). The code for this tool lives in another [repo](https://github.com/wcurrie/emv-bertlv-tools).
2. As a command line tool.

From Maven or Gradle
--------------------

Dependency Information (available from [Maven Central](https://repo1.maven.org/maven2/io/github/binaryfoo/emv-bertlv/)):

        <dependency>
            <groupId>io.github.binaryfoo</groupId>
            <artifactId>emv-bertlv</artifactId>
            <version>0.1.3</version>
        </dependency>
        
To get started decoding call decode() on [RootDecoder](https://github.com/binaryfoo/emv-bertlv/blob/master/src/main/java/io/github/binaryfoo/RootDecoder.java).

Command Line
------------

The library can be used as an executable jar: 

1. Download the latest [jar](https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=io.github.binaryfoo&a=emv-bertlv&c=shaded&v=LATEST)
2. Run using `java -jar emv-bertlv-x.y.z-shaded.jar`

This will dump out some (hopefully) somewhat helpful help output:

    Usage Main <decode-type> <value> [<tag-set>]
      <decode-type> is one of
        95: TVR
        9B: TSI
        82: AIP
        8E: CVM List
        9F34: CVM Results
        9F6C: CTQ
        9F66: TTQ
        dol: DOL
        filled-dol: Filled DOL
        constructed: TLV Data
        apdu-sequence: APDUs
        bit-string: Bits
      <value> is the hex string or '-' for standard input
      <tag-set> is one of [EMV, qVSDC, MSD, Amex] defaults to EMV

For example to decode the Terminal Verification Results:

    java -jar emv-bertlv-x.y.z-shaded.jar 95 ffffffffff
    
Or say you have a file containing a set of [APDUs](http://en.wikipedia.org/wiki/Smart_card_application_protocol_data_unit) encoded as hex strings:
    
    cat apdus.txt | java -jar emv-bertlv-x.y.z-shaded.jar apdu-sequence - 

Domain Knowledge
================

Visa's [chip terms explained](http://www.visa-asia.com/ap/center/merchants/productstech/includes/uploads/CTENov02.pdf) document is a good list.

Eftlab's [knowledge base](https://www.eftlab.com.au/index.php/site-map/knowledge-base).

Alternative Tools
=================

Eftlab's [BP-Tools](https://www.eftlab.com.au/index.php/site-map/tutorials/256-bp-tools-emvt)

Emvlab's [tlvtool](http://www.emvlab.org/tlvutils/)
