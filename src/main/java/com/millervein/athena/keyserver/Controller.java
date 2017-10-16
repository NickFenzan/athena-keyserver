package com.millervein.athena.keyserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	@Autowired TokenService tokenService;
	
	@RequestMapping("token")
	public String getToken() {
		return tokenService.getToken();
	}
	
	@RequestMapping(method=RequestMethod.POST, path="refresh-token")
	public void refreshToken() {
		tokenService.refreshToken();
	}

}
