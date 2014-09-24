package io.github.binaryfoo;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class DecodeController {

    private static final Logger LOG = Logger.getLogger(DecodeController.class.getName());

    private final HexDumpFactory hexDumpFactory = new HexDumpFactory();
    private final RootDecoder rootDecoder = new RootDecoder();

    @RequestMapping(value = "/decode", method = RequestMethod.POST)
    public String decode(@RequestParam String tag, @RequestParam String value, @RequestParam String meta, ModelMap modelMap) {
        try {
            decodeInto(tag, value, meta, modelMap);
            return "decodedData";
        } catch (DecodeFailedException e) {
            modelMap.addAttribute("error", e.getMessage());
            return "validationError";
        }
    }

    @RequestMapping(value = "/api/decode", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> decodeJson(@RequestParam String tag, @RequestParam String value, @RequestParam String meta) {
        Map<String, Object> response = new HashMap<String, Object>();
        decodeInto(tag, value, meta, response);
        return response;
    }

    private void decodeInto(String tag, String value, String meta, Map<String, Object> response) {
        LOG.info("Request to decode tag [" + tag + "] and value [" + value + "] and meta [" + meta + "]");
        TagInfo tagInfo = RootDecoder.getTagInfo(tag);
        if (tagInfo == null) {
            LOG.fine("Unknown tag");
            throw new DecodeFailedException("Unknown tag");
        }
        String error = tagInfo.getDecoder().validate(value);
        if (error != null) {
            LOG.fine("Validation error " + error);
            throw new DecodeFailedException(error);
        }
        try {
            value = value.toUpperCase();
            List<DecodedData> decodedData = rootDecoder.decode(value, meta, tagInfo);
            LOG.fine("Decoded successfully " + decodedData);
            if (decodedData.size() == 0 || decodedData.get(0).getHexDump() == null) {
                response.put("rawData", hexDumpFactory.splitIntoByteLengthStrings(value.replaceAll(":", " "), 0));
            }
            response.put("decodedData", decodedData);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.log(Level.FINE, "Error decoding", e);
            throw new DecodeFailedException(getMessageTrace(e));
        }
    }

    @ExceptionHandler(DecodeFailedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody Map<String, String> handleException(DecodeFailedException e) {
        return Collections.singletonMap("error", e.getMessage());
    }

    private String getMessageTrace(Exception e) {
        StringBuilder b = new StringBuilder(e.getMessage() != null ? e.getMessage() : "Either something has gone wrong or you've given me rubbish");
        for (Throwable t = e.getCause(); t != null; t = t.getCause()) {
            b.append(", ").append(t);
        }
        return b.toString();
    }

}
