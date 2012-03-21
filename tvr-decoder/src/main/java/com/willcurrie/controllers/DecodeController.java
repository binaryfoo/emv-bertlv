package com.willcurrie.controllers;

import java.util.*;
import java.util.logging.Logger;

import com.willcurrie.EmvTags;
import com.willcurrie.decoders.DataObjectListDecoder;
import com.willcurrie.decoders.DecodeSession;
import com.willcurrie.decoders.PopulatedDOLDecoder;
import com.willcurrie.decoders.TLVDecoder;
import com.willcurrie.decoders.apdu.*;
import com.willcurrie.hex.ByteElement;
import com.willcurrie.hex.HexDumpElement;
import com.willcurrie.hex.WhitespaceElement;
import com.willcurrie.tlv.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.willcurrie.DecodedData;
import com.willcurrie.TagInfo;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DecodeController {

	private static final Logger LOG = Logger.getLogger(DecodeController.class.getName());

    public static Map<String, TagInfo> ROOT_TAG_INFO = new LinkedHashMap<String, TagInfo>();
    static {
        putEmvTag(EmvTags.TERMINAL_VERIFICATION_RESULTS);
        putEmvTag(EmvTags.TSI);
        putEmvTag(EmvTags.APPLICATION_INTERCHANGE_PROFILE);
        putEmvTag(EmvTags.CVM_LIST);
        putEmvTag(EmvTags.CVM_RESULTS);
        putEmvTag(EmvTags.CARD_TX_QUALIFIERS);
        putEmvTag(EmvTags.TERMINAL_TX_QUALIFIERS);
        ROOT_TAG_INFO.put("dol", new TagInfo("DOL", "Data Object List", new DataObjectListDecoder()));
        ROOT_TAG_INFO.put("filled-dol", new TagInfo("Filled DOL", "Data Object List", new PopulatedDOLDecoder()));
        ROOT_TAG_INFO.put("constructed", new TagInfo("TLV Data", "Constructed TLV data", new TLVDecoder()));
        ROOT_TAG_INFO.put("apdu-sequence", new TagInfo("APDUs", "Sequence of Command/Reply APDUs", new APDUSequenceDecoder(new ReplyAPDUDecoder(new TLVDecoder()),
                new SelectCommandAPDUDecoder(), new GetProcessingOptionsCommandAPDUDecoder(), new ReadRecordAPDUDecoder(),
                new GenerateACAPDUDecoder(), new GetDataAPDUDecoder(), new ExternalAuthenticateAPDUDecoder(), new ComputeCryptoChecksumDecoder())));
    }
    private static void putEmvTag(Tag tag) {
        ROOT_TAG_INFO.put(tag.getHexString(), EmvTags.METADATA.get(tag));
    }

    @RequestMapping(value = "/decode", method = RequestMethod.POST)
    public String decode(@RequestParam String tag, @RequestParam String value, ModelMap modelMap) {
    	LOG.info("Request to decode tag [" + tag + "] and value [" + value + "]");
    	TagInfo tagInfo = ROOT_TAG_INFO.get(tag);
    	if (tagInfo == null) {
    		LOG.fine("Unknown tag");
    		modelMap.addAttribute("error", "Unknown tag");
    		return "validationError";
    	}
    	String error = tagInfo.getDecoder().validate(value);
		if (error != null) {
			LOG.fine("Validation error " + error);
			modelMap.addAttribute("error", error);
			return "validationError";
		}
		try {
            value = value.toUpperCase();
            DecodeSession decodeSession = new DecodeSession();
            decodeSession.setTagMetaData(EmvTags.METADATA);
            List<DecodedData> decodedData = tagInfo.getDecoder().decode(value, 0, decodeSession);
            LOG.fine("Decoded successfully " + decodedData);
            modelMap.addAttribute("rawData", splitIntoByteLengthStrings(value.replaceAll(":", " ")));
            modelMap.addAttribute("decodedData", decodedData);
            return "decodedData";
        } catch (Exception e) {
			LOG.fine("Error decoding " + e.getMessage());
			modelMap.addAttribute("error", e.getMessage());
			return "validationError";
		}
    }

    private List<HexDumpElement> splitIntoByteLengthStrings(String hexString) {
    	List<HexDumpElement> elements = new ArrayList<HexDumpElement>();
        int byteOffset = 0;
    	for (int i = 0; i < hexString.length(); ) {
            if (hexString.charAt(i) == ' ') {
                elements.add(new WhitespaceElement("<br>"));
                i++;
            } else {
                elements.add(new ByteElement(hexString.substring(i, i + 2), byteOffset++));
                i+=2;
            }
    	}
    	return elements;
    }
}
