package com.kpit.iocl.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserDataController {

	 @RequestMapping("/users/data")
	  public @ResponseBody String getUsers() {
	    return "{\"users\":[{\"firstname\":\"akshay\", \"lastname\":\"mohite\"}," +
	           "{\"firstname\":\"suhas\",\"lastname\":\"pawar\"}]}";
	  }
}
