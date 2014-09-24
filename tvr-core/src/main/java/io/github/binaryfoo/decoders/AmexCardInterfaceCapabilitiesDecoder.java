package io.github.binaryfoo.decoders;

public class AmexCardInterfaceCapabilitiesDecoder extends FixedLengthDecoder {
    public AmexCardInterfaceCapabilitiesDecoder() {
        super(4,
                "8000", "Keyed Data Entry Supported (Embossed or Printed PAN)",
                "4000", "Physical Magnetic Stripe Supported",
                "2000", "Contact EMV Interface Supported",
                "1000", "Contactless EMV Interface Supported",
                "0800", "Mobile Interface Supported"
                );
    }

}
