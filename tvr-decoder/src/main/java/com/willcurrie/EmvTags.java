package com.willcurrie;

import com.willcurrie.decoders.*;
import com.willcurrie.tlv.Tag;

import static com.willcurrie.decoders.PrimitiveDecoder.ASCII;
import static com.willcurrie.decoders.PrimitiveDecoder.HEX;

public final class EmvTags {

    public static final TagMetaData METADATA = new TagMetaData();

    private EmvTags() {
    }

    public static final Tag RECOGNISE_CARD_SUPPORTED_OPTIONS = newTag("DF8178", "recognise card supported options", HEX);
    public static final Tag ASCII_CODE_TABLE_INDEX = newTag("DF8172", "ascii code table index", HEX);
    public static final Tag BRAND_TABLE = newTag("FB", "brand table", HEX);
    public static final Tag BRAND_TABLE_CHIP_FLAG = newTag("DF8173", "brand table chip flag", HEX);
    public static final Tag BRAND_ID = newTag("DF5F", "BRAND_ID", HEX);
    public static final Tag APPLICATION_SELECTION_INDICATOR = newTag("DF8175", "application selection indicator", HEX);
    public static final Tag BRAND_ACCEPTED = newTag("DF8174", "brand accepted", HEX);
    public static final Tag AID_TABLE_DOMESTIC_FLAG = newTag("DF9009", "aid table domestic flag", HEX);
    public static final Tag NEXT_INDEX_IF_POS = newTag("DF9007", "next index if pos", HEX);
    public static final Tag NEXT_INDEX_IF_NEG = newTag("DF9008", "next index if neg", HEX);
    public static final Tag AID_TABLE = newTag("E5", "AID table", HEX);
    public static final Tag POS_ENTRY_MODE = newTag("9F39", "pos entry mode", HEX);
    public static final Tag TERMINAL_APPLICATION_VERSION_NUMBER = newTag("9F09", "terminal application version number", HEX);
    public static final Tag DEFAULT_DDOL = newTag("DF5D", "Default DDOL", "Default DDOL", new DataObjectListDecoder());
    public static final Tag TAC_DENIAL = newTag("DF57", "TAC denial", HEX);
    public static final Tag TAC_ONLINE = newTag("DF58", "TAC online", HEX);
    public static final Tag TAC_DEFAULT = newTag("DF56", "TAC default", HEX);
    public static final Tag TERMINAL_FLOOR_LIMIT = newTag("9F1B", "terminal floor limit", HEX);
    public static final Tag TARGET_PERCENTAGE = newTag("DF5A", "target percentage", HEX);
    public static final Tag MAX_TARGET_PERCENTAGE = newTag("DF5B", "max target percentage", HEX);
    public static final Tag THRESHOLD_VALUE = newTag("DF5C", "threshold value", HEX);
    public static final Tag FINAL_SELECT_INITIATE_TX = newTag("DF3A", "final select initiate tx", HEX);
    public static final Tag TRANSACTION_CURRENCY_CODE = newTag("5F2A", "transaction currency code", HEX);
    public static final Tag TERMINAL_COUNTRY_CODE = newTag("9F1A", "terminal country code", HEX);
    public static final Tag TRANSACTION_CURRENCY_EXPONENT = newTag("5F36", "transaction currency exponent", HEX);
    public static final Tag MERCHANT_ID = newTag("9F16", "merchant id", ASCII);
    public static final Tag MERCHANT_CATEGORY_CODE = newTag("9F15", "merchant category code", HEX);
    public static final Tag TERMINAL_ID = newTag("9F1C", "terminal id", ASCII);
    public static final Tag TERMINAL_CAPABILITIES = newTag("9F33", "terminal capabilities", HEX);
    public static final Tag ADDITIONAL_TERMINAL_CAPABILITIES = newTag("9F40", "additional terminal capabilities", HEX);
    public static final Tag TERMINAL_TYPE = newTag("9F35", "terminal type", HEX);
    public static final Tag APPLICATION_ID = newTag("9F06", "application id", HEX);
    public static final Tag TRANSACTION_DATE = newTag("9A", "transaction date", HEX);
    public static final Tag TRANSACTION_TIME = newTag("9F21", "transaction time", HEX);
    public static final Tag TRANSACTION_AMOUNT = newTag("DF50", "transaction amount", HEX);
    public static final Tag TRANSACTION_OTHER_AMOUNT = newTag("9F03", "transaction other amount", HEX);
    public static final Tag OFFLINE_TOTAL_AMOUNT = newTag("DF52", "offline total amount", HEX);
    public static final Tag TRANSACTION_TYPE = newTag("9C", "transaction type", HEX);
    public static final Tag TRANSACTION_GROUP = newTag("E0", "transaction group", HEX);
    public static final Tag TABLE_RECORD = newTag("EF", "table record", HEX);
    public static final Tag CA_PUBLIC_KEY_MODULUS = newTag("DF53", "CA public key modulus", HEX);
    public static final Tag CA_PUBLIC_KEY_EXPONENT = newTag("DF54", "CA public key exponent", HEX);
    public static final Tag TRANSACTION_SEQUENCE_COUNTER = newTag("9F41", "transaction sequence counter", HEX);
    public static final Tag AMOUNT_AUTHORIZED = newTag("9F02", "amount authorized", HEX);
    public static final Tag AMOUNT_OTHER = newTag("9F03", "amount other", HEX);
    public static final Tag APPLICATION_INTERCHANGE_PROFILE = newTag("82", "AIP", "Application Interchange Profile", new AIPDecoder());
    public static final Tag APPLICATION_TRANSACTION_COUNTER = newTag("9F36", "application transaction counter", HEX);
    public static final Tag APPLICATION_CRYPTOGRAM = newTag("9F26", "application cryptogram", HEX);
    public static final Tag ISSUER_APPLICATION_DATA = newTag("9F10", "issuer application data", HEX);
    public static final Tag TERMINAL_CURRENCY_CODE = newTag("5F2A", "terminal currency code", HEX);
    public static final Tag TERMINAL_SERIAL_NUMBER = newTag("9F1E", "terminal serial number", ASCII);
    public static final Tag UNPREDICTABLE_NUMBER = newTag("9F37", "unpredictable number", HEX);
    public static final Tag CVM_RESULTS = newTag("9F34", "CVM Results", "Cardholder Verification Results", new CVMResultsDecoder());
    public static final Tag CRYPTOGRAM_INFORMATION_DATA = newTag("9F27", "cryptogram information data", HEX);
    public static final Tag HOST_INCIDENT_CODE = newTag("DF2E", "host incident code", HEX);
    public static final Tag ISSUER_AUTHENTICATION_DATA = newTag("91", "issuer authentication data", HEX);
    public static final Tag ISSUER_SCRIPT_TERMPLATE_1 = newTag("71", "issuer script termplate 1", HEX);
    public static final Tag ISSUER_SCRIPT_TERMPLATE_2 = newTag("72", "issuer script termplate 2", HEX);
    public static final Tag APPLICATION_LABEL = newTag("50", "application label", ASCII);
    public static final Tag DEDICATED_FILE_NAME = newTag("84", "dedicated file name", HEX);
    public static final Tag APPLICATION_PRIORITY_INDICATOR = newTag("87", "application priority indicator", HEX);
    public static final Tag CA_PUBLIC_KEY_INDEX = newTag("8F", "ca public key index", HEX);
    public static final Tag TRACK_2_EQUIVALENT_DATA = newTag("57", "track 2 equivalent data", HEX);
    public static final Tag CARD_HOLDER_NAME = newTag("5F20", "card holder name", ASCII);
    public static final Tag TRACK_1_DISCRETIONARY_DATA = newTag("9F1F", "track 1 discretionary data", ASCII);
    public static final Tag TRACK_2_DISCRETIONARY_DATA = newTag("9F20", "track 2 discretionary data", HEX);
    public static final Tag CARD_EXPIRY = newTag("5F24", "card expiry", HEX);
    public static final Tag ISSUER_COUNTRY_CODE = newTag("5F28", "issuer country code", HEX);
    public static final Tag PAN_SEQUENCE_NUMBER = newTag("5F34", "PAN sequence number", HEX);
    public static final Tag PAN = newTag("5A", "PAN", HEX);
    public static final Tag AUTHORISATION_RESPONSE_CODE = newTag("8A", "authorisation response code", ASCII);
    public static final Tag TERMINAL_VERIFICATION_RESULTS = newTag("95", "TVR", "Terminal Verification Results", new TVRDecoder());
    public static final Tag TSI = newTag("9B", "TSI", "Transaction Status Indicator", new TSIDecoder());
    public static final Tag CVM_LIST = newTag("8E", "CVM List", "Cardholder Verification Method List", new CVMListDecoder());
    public static final Tag APPLICATION_CURRENCY_CODE = newTag("9F42", "application currency code", HEX);
    public static final Tag TRANSACTION_CATEGORY_CODE = newTag("9F53", "transaction category code", ASCII);
    public static final Tag FCI_TEMPLATE = newTag("6F", "FCI template", HEX);
    public static final Tag FCI_PROPRIETARY_TEMPLATE = newTag("A5", "FCI proprietary template", HEX);
    public static final Tag AFL = newTag("94", "Application File Locator (AFL)", HEX);
    public static final Tag APPLICATION_EFFECTIVE_DATE = newTag("5F25", "application effective date", HEX);
    public static final Tag PDOL = newTag("9F38", "PDOL", "Processing DOL", new DataObjectListDecoder());
    public static final Tag CDOL_1 = newTag("8C", "CDOL 1", "Data object list", new DataObjectListDecoder());
    public static final Tag CDOL_2 = newTag("8D", "CDOL 1", "Data object list", new DataObjectListDecoder());
    public static final Tag APPLICATION_USAGE_CONTROL = newTag("9F07", "application usage control", HEX);
    public static final Tag CARD_APPLICATION_VERSION_NUMBER = newTag("9F08", "card application version number", HEX);
    public static final Tag IAC_DEFAULT = newTag("9F0D", "IAC default", HEX);
    public static final Tag IAC_DENIAL = newTag("9F0E", "IAC denial", HEX);
    public static final Tag IAC_ONLINE = newTag("9F0F", "IAC online", HEX);
    public static final Tag SDA_TAG_LIST = newTag("9F4A", "SDA tag list", HEX);
    public static final Tag ISSUER_PUBLIC_KEY_EXPONENT = newTag("9F32", "issuer public key exponent", HEX);
    public static final Tag ISSUER_PUBLIC_KEY_REMAINDER = newTag("92", "issuer public key remainder", HEX);
    public static final Tag ISSUER_PUBLIC_KEY_CERTIFICATE = newTag("90", "issuer public key certificate", HEX);
    public static final Tag ICC_PUBLIC_KEY_EXPONENT = newTag("9F47", "ICC public key exponent", HEX);
    public static final Tag ICC_PUBLIC_KEY_REMAINDER = newTag("9F48", "ICC public key remainder", HEX);
    public static final Tag SIGNED_DYNAMIC_APPLICATION_DATA = newTag("9F4B", "signed dynamic application data", HEX);
    public static final Tag TERMINAL_TX_QUALIFIERS = newTag("9F66", "TTQ", "Terminal transaction qualifiers", new TerminalTxQualifiersDecoder());
    public static final Tag CARD_TX_QUALIFIERS = newTag("9F6C", "CTQ", "Card transaction qualifiers", new CardTxQualifiersDecoder());
    public static final Tag RESPONSE_TEMPLATE = newTag("77", "response template", HEX);
    public static final Tag PIN_TRY_COUNTER = newTag("9F17", "pin try counter", HEX);
    public static final Tag SIGNED_STATIC_APPLICATION_DATA = newTag("93", "signed static application data", HEX);
    public static final Tag ICC_PUBLIC_KEY_CERTIFICATE = newTag("9F46", "ICC public key certificate", HEX);
    public static final Tag DATA_AUTHENTICATION_CODE = newTag("9F45", "data authentication code", HEX);
    public static final Tag ICC_DYNAMIC_NUMBER = newTag("9F4C", "ICC dynamic number", HEX);
    public static final Tag RESPONSE_TEMPLATE_2 = newTag("70", "response template", HEX);
    public static final Tag FCI_DISCRETIONARY_DATA = newTag("BF0C", "FCI discretionary data", HEX);

    // mastercard magstripe only

    public static final Tag MSD_TRACK_1 = newTag("56", "MSD track 1", ASCII);
    public static final Tag MSD_TRACK_2 = newTag("9F6B", "MSD track 2", HEX);
    public static final Tag MSD_CVC_3_TRACK_1 = newTag("9F60", "MSD CVC3 track 1", HEX);
    public static final Tag MSD_CVC_3_TRACK_2 = newTag("9F61", "MSD CVC3 track 2", HEX);
    public static final Tag MSD_POSITION_OF_CVC_3_TRACK_1 = newTag("9F62", "MSD position of CVC3 in track 1", HEX);
    public static final Tag MSD_POSITION_OF_CVC_3_TRACK_2 = newTag("9F65", "MSD position of CVC3 in track 2", HEX);
    public static final Tag MSD_POSITION_OF_UN_AND_ATC_TRACK_1 = newTag("9F63", "MSD position of UN and ATC in track 1", HEX);
    //    public static final Tag MSD_POSITION_OF_UN_AND_ATC_TRACK_2 = newTag("9F66", "MSD position of UN and ATC in track 2", false);
    public static final Tag MSD_NATC_TRACK_1 = newTag("9F64", "MSD number of digits from ATC in track 1", HEX);
    public static final Tag MSD_NATC_TRACK_2 = newTag("9F67", "MSD number of digits from ATC in track 2", HEX);
    public static final Tag MSD_CVM_LIST = newTag("9F68", "MSD CVM List", "MSD Cardholder Verification Method List", new CVMListDecoder());
//    public static final Tag MSD_APPLICATION_VERSION_NUMBER = newTag("9F6C", "MSD application version number", false);

    private static Tag newTag(String hexString, String shortName, String longName, Decoder decoder) {
        Tag tag = Tag.fromHex(hexString);
        METADATA.put(tag, new TagInfo(shortName, longName, decoder, HEX));
        return tag;
    }

    private static Tag newTag(String hexString, String name, PrimitiveDecoder primitiveDecoder) {
        Tag tag = Tag.fromHex(hexString);
        METADATA.put(tag, new TagInfo(name, null, new NullDecoder(), primitiveDecoder));
        return tag;
    }

    public static String getTagName(Tag tag) {
        return METADATA.get(tag).getShortName();
    }
}
