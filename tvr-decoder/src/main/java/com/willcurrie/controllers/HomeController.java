package com.willcurrie.controllers;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.willcurrie.TagInfo;

/**
 * Controller for handling the basic home page request
 */
@Controller
public class HomeController {

    @RequestMapping("/home")
    public ModelAndView welcome(ModelMap model) {
    	return new ModelAndView("home", "tagInfos", getTagsToShow());
    }

	private ArrayList<TagInfo> getTagsToShow() {
		ArrayList<TagInfo> tagInfos = new ArrayList<TagInfo>();
		for (String tag : Arrays.asList("95", "9B", "82", "8E", "9F34", "constructed", "apdu-sequence")) {
			tagInfos.add(TagInfo.get(tag));
		}
		return tagInfos;
	}
}