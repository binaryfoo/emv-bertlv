package com.willcurrie.controllers;

import java.util.HashMap;

import com.willcurrie.RootDecoder;
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
        model.put("tagInfos", RootDecoder.getSupportedTags());
        model.put("tagMetaSets", RootDecoder.getAllTagMeta());
        return new ModelAndView("home", model);
    }

}