package com.millervein.athena.keyserver;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenService {
	private String key;
	private String secret;
	private String tokenUrl;
	private RestTemplate rest;
	
	private String token;

	TokenService(
			@Value("${athena.key}") String key, 
			@Value("${athena.secret}") String secret,
			@Value("${athena.tokenurl}") String tokenUrl
			) {
		this.key = key;
		this.secret = secret;
		this.tokenUrl = tokenUrl;
		this.rest = new RestTemplate();
	}

	private String generateAuthString(String key, String secret) {
		String credentials = key + ":" + secret;
		String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
		return "Basic " + encodedCredentials;
	}

	private HttpHeaders generateHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add(HttpHeaders.AUTHORIZATION, generateAuthString(key, secret));
		return headers;
	}

	private MultiValueMap<String, String> generateBody() {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("grant_type", "client_credentials");
		return body;
	}

	private HttpEntity<MultiValueMap<String, String>> generateTokenRequest() {
		return new HttpEntity<MultiValueMap<String, String>>(generateBody(), generateHeaders());
	}

	@Scheduled(cron = "0 0 * * * *")
	public void refreshToken() {
		ResponseEntity<TokenResponse> response = rest.exchange(tokenUrl, HttpMethod.POST, generateTokenRequest(),
				TokenResponse.class);
		this.token = response.getBody().getAccessToken();
	}

	public String getToken() {
		return token;
	}
}
