package com.willcurrie.controllers;

import java.util.*;
import java.util.logging.Logger;

import com.willcurrie.*;
import com.willcurrie.decoders.DataObjectListDecoder;
import com.willcurrie.decoders.PopulatedDOLDecoder;
import com.willcurrie.decoders.TLVDecoder;
import com.willcurrie.decoders.apdu.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DecodeController {

    private static final Logger LOG = Logger.getLogger(DecodeController.class.getName());

    private final HexDumpFactory hexDumpFactory = new HexDumpFactory();
    private final RootDecoder rootDecoder = new RootDecoder();

    @RequestMapping(value = "/decode", method = RequestMethod.POST)
    public String decode(@RequestParam String tag, @RequestParam String value, @RequestParam String meta, ModelMap modelMap) {
    	LOG.info("Request to decode tag [" + tag + "] and value [" + value + "] and meta [" + meta + "]");
    	TagInfo tagInfo = RootDecoder.getTagInfo(tag);
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
            List<DecodedData> decodedData = rootDecoder.decode(value, meta, tagInfo);
            LOG.fine("Decoded successfully " + decodedData);
            if (decodedData.size() == 0 || decodedData.get(0).getHexDump() == null) {
                modelMap.addAttribute("rawData", hexDumpFactory.splitIntoByteLengthStrings(value.replaceAll(":", " "), 0));
            }
            modelMap.addAttribute("decodedData", decodedData);
            return "decodedData";
        } catch (Exception e) {
            e.printStackTrace();
			LOG.fine("Error decoding " + e.getMessage());
			modelMap.addAttribute("error", e.getMessage());
			return "validationError";
		}
    }

}
