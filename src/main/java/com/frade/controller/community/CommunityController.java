package com.frade.controller.community;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/community-lists")
public class CommunityController {

	@GetMapping("")
	public String lists() {
		
		
		return "community/lists";
	}
	
	
	@GetMapping("/write")
	public String write() {
		
		
		return "community/write";
	}
}
