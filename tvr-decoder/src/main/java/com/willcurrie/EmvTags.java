package com.willcurrie;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.willcurrie.tlv.Tag;

public final class EmvTags {

    private EmvTags() {
    }

    private static final Set ALPHA_NUMERIC_TAGS = new HashSet();
    private static final Map TAG_NAMES = new HashMap();

    public static final Tag RECOGNISE_CARD_SUPPORTED_OPTIONS = newTag("DF8178", "recognise card supported options", false);
    public static final Tag ASCII_CODE_TABLE_INDEX = newTag("DF8172", "ascii code table index", false);
    public static final Tag BRAND_TABLE = newTag("FB", "brand table", false);
    public static final Tag BRAND_TABLE_CHIP_FLAG = newTag("DF8173", "brand table chip flag", false);
    public static final Tag BRAND_ID = newTag("DF5F", "BRAND_ID", false);
    public static final Tag APPLICATION_SELECTION_INDICATOR = newTag("DF8175", "application selection indicator", false);
    public static final Tag BRAND_ACCEPTED = newTag("DF8174", "brand accepted", false);
    public static final Tag AID_TABLE_DOMESTIC_FLAG = newTag("DF9009", "aid table domestic flag", false);
    public static final Tag NEXT_INDEX_IF_POS = newTag("DF9007", "next index if pos", false);
    public static final Tag NEXT_INDEX_IF_NEG = newTag("DF9008", "next index if neg", false);
    public static final Tag AID_TABLE = newTag("E5", "AID table", false);
    public static final Tag POS_ENTRY_MODE = newTag("9F39", "pos entry mode", false);
    public static final Tag TERMINAL_APPLICATION_VERSION_NUMBER = newTag("9F09", "terminal application version number", false);
    public static final Tag DEFAULT_DDOL = newTag("DF5D", "default ddol", false);
    public static final Tag TAC_DENIAL = newTag("DF57", "TAC denial", false);
    public static final Tag TAC_ONLINE = newTag("DF58", "TAC online", false);
    public static final Tag TAC_DEFAULT = newTag("DF56", "TAC default", false);
    public static final Tag TERMINAL_FLOOR_LIMIT = newTag("9F1B", "terminal floor limit", false);
    public static final Tag TARGET_PERCENTAGE = newTag("DF5A", "target percentage", false);
    public static final Tag MAX_TARGET_PERCENTAGE = newTag("DF5B", "max target percentage", false);
    public static final Tag THRESHOLD_VALUE = newTag("DF5C", "threshold value", false);
    public static final Tag FINAL_SELECT_INITIATE_TX = newTag("DF3A", "final select initiate tx", false);
    public static final Tag TRANSACTION_CURRENCY_CODE = newTag("5F2A", "transaction currency code", false);
    public static final Tag TERMINAL_COUNTRY_CODE = newTag("9F1A", "terminal country code", false);
    public static final Tag TRANSACTION_CURRENCY_EXPONENT = newTag("5F36", "transaction currency exponent", false);
    public static final Tag MERCHANT_ID = newTag("9F16", "merchant id", true);
    public static final Tag MERCHANT_CATEGORY_CODE = newTag("9F15", "merchant category code", false);
    public static final Tag TERMINAL_ID = newTag("9F1C", "terminal id", true);
    public static final Tag TERMINAL_CAPABILITIES = newTag("9F33", "terminal capabilities", false);
    public static final Tag ADDITIONAL_TERMINAL_CAPABILITIES = newTag("9F40", "additional terminal capabilities", false);
    public static final Tag TERMINAL_TYPE = newTag("9F35", "terminal type", false);
    public static final Tag APPLICATION_ID = newTag("9F06", "application id", false);
    public static final Tag TRANSACTION_DATE = newTag("9A", "transaction date", false);
    public static final Tag TRANSACTION_TIME = newTag("9F21", "transaction time", false);
    public static final Tag TRANSACTION_AMOUNT = newTag("DF50", "transaction amount", false);
    public static final Tag TRANSACTION_OTHER_AMOUNT = newTag("9F03", "transaction other amount", false);
    public static final Tag OFFLINE_TOTAL_AMOUNT = newTag("DF52", "offline total amount", false);
    public static final Tag TRANSACTION_TYPE = newTag("9C", "transaction type", false);
    public static final Tag TRANSACTION_GROUP = newTag("E0", "transaction group", false);
    public static final Tag TABLE_RECORD = newTag("EF", "table record", false);
    public static final Tag CA_PUBLIC_KEY_MODULUS = newTag("DF53", "CA public key modulus", false);
    public static final Tag CA_PUBLIC_KEY_EXPONENT = newTag("DF54", "CA public key exponent", false);
    public static final Tag TRANSACTION_SEQUENCE_COUNTER = newTag("9F41", "transaction sequence counter", false);
    public static final Tag AMOUNT_AUTHORIZED = newTag("9F02", "amount authorized", false);
    public static final Tag AMOUNT_OTHER = newTag("9F03", "amount other", false);
    public static final Tag APPLICATION_INTERCHANGE_PROFILE = newTag("82", "application interchange profile", false);
    public static final Tag APPLICATION_TRANSACTION_COUNTER = newTag("9F36", "application transaction counter", false);
    public static final Tag APPLICATION_CRYPTOGRAM = newTag("9F26", "application cryptogram", false);
    public static final Tag ISSUER_APPLICATION_DATA = newTag("9F10", "issuer application data", false);
    public static final Tag TERMINAL_CURRENCY_CODE = newTag("5F2A", "terminal currency code", false);
    public static final Tag TERMINAL_SERIAL_NUMBER = newTag("9F1E", "terminal serial number", false);
    public static final Tag UNPREDICTABLE_NUMBER = newTag("9F37", "unpredictable number", false);
    public static final Tag CVM_RESULTS = newTag("9F34", "CVM results", false);
    public static final Tag CRYPTOGRAM_INFORMATION_DATA = newTag("9F27", "cryptogram information data", false);
    public static final Tag HOST_INCIDENT_CODE = newTag("DF2E", "host incident code", false);
    public static final Tag ISSUER_AUTHENTICATION_DATA = newTag("91", "issuer authentication data", false);
    public static final Tag ISSUER_SCRIPT_TERMPLATE_1 = newTag("71", "issuer script termplate 1", false);
    public static final Tag ISSUER_SCRIPT_TERMPLATE_2 = newTag("72", "issuer script termplate 2", false);
    public static final Tag APPLICATION_LABEL = newTag("50", "application label", true);
    public static final Tag DEDICATED_FILE_NAME = newTag("84", "dedicated file name", false);
    public static final Tag APPLICATION_PRIORITY_INDICATOR = newTag("87", "application priority indicator", false);
    public static final Tag CA_PUBLIC_KEY_INDEX = newTag("8F", "ca public key index", false);
    public static final Tag TRACK_2_EQUIVALENT_DATA = newTag("57", "track 2 equivalent data", false);
    public static final Tag CARD_HOLDER_NAME = newTag("5F20", "card holder name", true);
    public static final Tag TRACK_1_DISCRETIONARY_DATA = newTag("9F1F", "track 1 discretionary data", true);
    public static final Tag TRACK_2_DISCRETIONARY_DATA = newTag("9F20", "track 2 discretionary data", false);
    public static final Tag CARD_EXPIRY = newTag("5F24", "card expiry", false);
    public static final Tag ISSUER_COUNTRY_CODE = newTag("5F28", "issuer country code", false);
    public static final Tag PAN_SEQUENCE_NUMBER = newTag("5F34", "PAN sequence number", false);
    public static final Tag PAN = newTag("5A", "PAN", false);
    public static final Tag AUTHORISATION_RESPONSE_CODE = newTag("8A", "authorisation response code", true);
    public static final Tag TERMINAL_VERIFICATION_RESULTS = newTag("95", "TVR", false);
    public static final Tag TSI = newTag("9B", "TSI", false);
    public static final Tag CVM_LIST = newTag("8E", "CVM list", false);
    public static final Tag APPLICATION_CURRENCY_CODE = newTag("9F42", "application currency code", false);
    public static final Tag TRANSACTION_CATEGORY_CODE = newTag("9F53", "transaction category code", true);
    public static final Tag FCI_TEMPLATE = newTag("6F", "FCI template", false);
    public static final Tag FCI_PROPRIETARY_TEMPLATE = newTag("A5", "FCI proprietary template", false);
    public static final Tag AFL = newTag("94", "Application File Locator (AFL)", false);
    public static final Tag APPLICATION_EFFECTIVE_DATE = newTag("5F25", "application effective date", false);
    public static final Tag PDOL = newTag("9F38", "PDOL", false);
    public static final Tag CDOL_1 = newTag("8C", "CDOL 1", false);
    public static final Tag CDOL_2 = newTag("8D", "CDOL 2", false);
    public static final Tag APPLICATION_USAGE_CONTROL = newTag("9F07", "application usage control", false);
    public static final Tag CARD_APPLICATION_VERSION_NUMBER = newTag("9F08", "card application version number", false);
    public static final Tag IAC_DEFAULT = newTag("9F0D", "IAC default", false);
    public static final Tag IAC_DENIAL = newTag("9F0E", "IAC denial", false);
    public static final Tag IAC_ONLINE = newTag("9F0F", "IAC online", false);
    public static final Tag SDA_TAG_LIST = newTag("9F4A", "SDA tag list", false);
    public static final Tag ISSUER_PUBLIC_KEY_EXPONENT = newTag("9F32", "issuer public key exponent", false);
    public static final Tag ISSUER_PUBLIC_KEY_REMAINDER = newTag("92", "issuer public key remainder", false);
    public static final Tag ISSUER_PUBLIC_KEY_CERTIFICATE = newTag("90", "issuer public key certificate", false);
    public static final Tag ICC_PUBLIC_KEY_EXPONENT = newTag("9F47", "ICC public key exponent", false);
    public static final Tag ICC_PUBLIC_KEY_REMAINDER = newTag("9F48", "ICC public key remainder", false);
    public static final Tag SIGNED_DYNAMIC_APPLICATION_DATA = newTag("9F4B", "signed dynamic application data", false);
    public static final Tag TERMINAL_TX_QUALIFIERS = newTag("9F6C", "terminal transaction qualifiers", false);
    public static final Tag CARD_TX_QUALIFIERS = newTag("9F6C", "card transaction qualifiers", false);
    public static final Tag RESPONSE_TEMPLATE = newTag("77", "response template", false);

    private static Tag newTag(String hexString, String name, boolean isValueAscii) {
        Tag tag = Tag.fromHex(hexString);
        TAG_NAMES.put(tag, name);
        if (isValueAscii) {
            ALPHA_NUMERIC_TAGS.add(tag);
        }
        return tag;
    }

    public static boolean isTagAscii(Tag tag) {
        return ALPHA_NUMERIC_TAGS.contains(tag);
    }

    public static String getTagName(Tag tag) {
        String name = (String) TAG_NAMES.get(tag);
        return name == null ? "" : name;
    }
}
