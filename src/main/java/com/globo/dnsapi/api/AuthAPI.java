package com.globo.dnsapi.api;

import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.http.RequestProcessor;
import com.globo.dnsapi.model.Authentication;
import com.globo.dnsapi.model.DNSAPIRoot;
import com.globo.dnsapi.model.User;

public class AuthAPI extends BaseAPI {

	public AuthAPI(RequestProcessor transport) {
		super(transport);
	}
	
	public Authentication signIn(String email, String password) throws DNSAPIException {
		User user = new User(email, password);
		DNSAPIRoot<User> payload = new DNSAPIRoot<User>();
		payload.set("user", user);
		Authentication auth = this.post("/users/sign_in.json", payload, Authentication.class, null);
		if (auth == null) {
			throw new DNSAPIException("Invalid authentication response");
		} else {
			return auth;
		}
	}
}
