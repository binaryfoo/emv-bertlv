package io.github.binaryfoo.decoders;

import io.github.binaryfoo.crypto.CaPublicKey;
import io.github.binaryfoo.crypto.CaPublicKeyTable;
import io.github.binaryfoo.tlv.ISOUtil;
import org.junit.Test;

public class SignedStaticDataRecovererTest {


    @Test
    public void testName() throws Exception {
        String encryptedIssuerCert = "94D25691DC520CE182B5F028EED84EA0783523A90F772FC50AE64C313A2FB81D09954BBAF06FFF1C7364FB1714F5049D78CAF4D77C36362B2C9A597303A1E29CA775D0741E79EED58F45F2CB73247AEF141A65B8464839FCB055E32E0C2A93F4";
        CaPublicKey caPublicKey = CaPublicKeyTable.getEntry("A000000025", "04");
        byte[] issuerBytes = new SignedDataRecoverer().recover(encryptedIssuerCert, caPublicKey);
        String decodedIssuerKey = new IssuerPublicKeyDecoder().decode(issuerBytes, caPublicKey.getModulus().length);
//        System.out.println(decodedIssuerKey);

        String signedStaticData = "06A6EB2D8F0B52D9B5241C497EC585B014AE265BE4F56CC07C94353A4AC19E60397150215ECB7EBD467802B856FDD712005013846544F644417838792169C76F";
        String issuerKey = "8D691CF05A0B48A5CFCB4FE2B935971BF5FAB9DBDA375F1F3115B8D64E672B052FE523827C1B14CD54B54B645150A78FAFD9E8175C576B1DA2D7B6FA" + "D2487161";
        byte[] staticDataBytes = new SignedDataRecoverer().recover(signedStaticData, "03", issuerKey);
//        System.out.println(ISOUtil.hexString(staticDataBytes));
//        String decodedSSD = new ICCPublicKeyDecoder().decode(staticDataBytes, ISOUtil.hex2byte(issuerKey).length);
//        System.out.println(decodedSSD);
    }
}
