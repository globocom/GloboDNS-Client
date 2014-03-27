package com.globo.dnsapi.api;

import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.http.RequestProcessor;
import com.globo.dnsapi.model.Authentication;
import com.globo.dnsapi.model.DNSAPIRoot;
import com.globo.dnsapi.model.User;

public class AuthAPI extends BaseAPI<Authentication> {

	public AuthAPI(RequestProcessor transport) {
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
	
	public Authentication signIn(String email, String password) throws DNSAPIException {
		User user = new User(email, password);
		DNSAPIRoot<User> payload = new DNSAPIRoot<User>();
		payload.set("user", user);
		DNSAPIRoot<Authentication> dnsAPIRoot = this.post("/users/sign_in.json", payload, false, false);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid authentication response");
		}

		Authentication auth = dnsAPIRoot.getFirstObject();
		// Setup token for future accesses for other APIs
		super.token = auth.getToken();
		return auth;
	}
	
}
