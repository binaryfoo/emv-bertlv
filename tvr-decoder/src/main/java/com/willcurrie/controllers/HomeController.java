package com.willcurrie.controllers;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for handling the basic home page request
 */
@Controller
public class HomeController {

    @RequestMapping("/home")
    public ModelAndView welcome() {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("tagInfos", DecodeController.ROOT_TAG_INFO.entrySet());
        model.put("tagMetaSets", DecodeController.TAG_META_SETS.keySet());
        return new ModelAndView("home", model);
    }

}