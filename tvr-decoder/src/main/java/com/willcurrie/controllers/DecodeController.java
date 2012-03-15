package com.willcurrie.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.willcurrie.DecodedData;
import com.willcurrie.TagInfo;

@Controller
public class DecodeController {

	private static final Logger LOG = Logger.getLogger(DecodeController.class.getName());
	
    @RequestMapping(value = "/decode/{tagHexString}/{value}", method = RequestMethod.GET)
    public String decode(@PathVariable String tagHexString, @PathVariable String value, ModelMap modelMap) {
    	LOG.info("Request to decode tag [" + tagHexString + "] and value [" + value + "]");
    	TagInfo tagInfo = TagInfo.get(tagHexString);
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
            value = value.toUpperCase().replaceAll("[^A-Z0-9 ]", "");
            List<DecodedData> decodedData = tagInfo.getDecoder().decode(value, 0);
            LOG.fine("Decoded successfully " + decodedData);
            modelMap.addAttribute("rawData", splitIntoByteLengthStrings(value.replaceAll(" ", "")));
            modelMap.addAttribute("decodedData", decodedData);
            return "decodedData";
        } catch (Exception e) {
            e.printStackTrace();
			LOG.fine("Error decoding " + e.getMessage());
			modelMap.addAttribute("error", e.getMessage());
			return "validationError";
		}
    }

    private List<String> splitIntoByteLengthStrings(String hexString) {
    	ArrayList<String> bytes = new ArrayList<String>();
    	for (int i = 0; i < hexString.length(); i+=2) {
    		bytes.add(hexString.substring(i, i + 2));
    	}
    	return bytes;
    }
}
