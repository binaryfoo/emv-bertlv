package com.willcurrie.controllers;

import org.junit.Test;
import org.springframework.ui.ModelMap;

public class DecodeControllerTest {

    private DecodeController decodeController = new DecodeController();

    @Test
    public void testAPDU() throws Exception {
        ModelMap modelMap = new ModelMap();
        decodeController.decode("apdu-sequence", "00A4040007A000000004101000 6F1C8407A0000000041010A511500F505043204D434420303420207632309000", modelMap);
        Object decodedData = modelMap.get("decodedData");
        System.out.println(decodedData);
    }
}
