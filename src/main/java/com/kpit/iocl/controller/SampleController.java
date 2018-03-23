package com.kpit.iocl.controller;


import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kpit.iocl.utils.AuthoritiesConstants;
import com.kpit.iocl.utils.SecurityUtils;


@RestController
public class SampleController {

	@RequestMapping("/api/testadmin")
	@Secured(AuthoritiesConstants.ADMIN)
	public String testApi() {
		return "Hello admin " + SecurityUtils.getCurrentUserLogin().get() + " and token is " + SecurityUtils.getCurrentUserJWT().get();
	}

	@RequestMapping("/api/testuser")
	public String testUser() {
		return "Hello user " + SecurityUtils.getCurrentUserLogin().get() + " and token is " + SecurityUtils.getCurrentUserJWT().get();
	}

}
