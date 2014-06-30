/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
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
	protected void interceptRequest(HttpRequest request) {
		request.setUnsuccessfulResponseHandler(null);
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
