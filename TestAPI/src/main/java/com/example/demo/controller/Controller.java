package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.message.Message;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api")
public class Controller {

	@GetMapping("/test")
	public Message testAPI() {
		
		Message message = new Message();
		
		return message;
	}
	
}
