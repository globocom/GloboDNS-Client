package com.globo.dnsapi.api;

import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.globo.dnsapi.AbstractAPI;
import com.globo.dnsapi.DNSAPIException;
import com.globo.dnsapi.DNSAPI;
import com.globo.dnsapi.model.Authentication;
import com.globo.dnsapi.model.DNSAPIRoot;
import com.globo.dnsapi.model.User;
import com.google.api.client.http.HttpRequest;

public class AuthAPI extends AbstractAPI<Authentication> {

	public AuthAPI(DNSAPI transport) {
		super(transport);
	}
	
	@Override
	protected Type getType() {
		return new TypeReference<Authentication>() {}.getType();
	}

	@Override
	protected Type getListType() {
		return new TypeReference<List<Authentication>>() {}.getType();
	}
	
	@Override
	protected void insertAuthenticationHeaders(HttpRequest request) {
		// do nothing
	}

	public Authentication signIn(String email, String password) throws DNSAPIException {
		User user = new User(email, password);
		DNSAPIRoot<User> payload = new DNSAPIRoot<User>();
		payload.set("user", user);
		DNSAPIRoot<Authentication> dnsAPIRoot = this.post("/users/sign_in.json", payload, false);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid authentication response");
		}

		Authentication auth = dnsAPIRoot.getFirstObject();
		return auth;
	}
}
