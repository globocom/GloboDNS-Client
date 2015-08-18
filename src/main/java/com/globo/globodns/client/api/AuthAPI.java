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
package com.globo.globodns.client.api;

import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.globo.globodns.client.AbstractAPI;
import com.globo.globodns.client.GloboDns;
import com.globo.globodns.client.GloboDnsException;
import com.globo.globodns.client.model.Authentication;
import com.globo.globodns.client.model.GloboDnsRoot;
import com.globo.globodns.client.model.User;
import com.google.api.client.http.HttpRequest;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;

public class AuthAPI extends AbstractAPI<Authentication> {

	public AuthAPI(GloboDns transport) {
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

	@Trace(dispatcher = true)
	public Authentication signIn(String email, String password) throws GloboDnsException {
		NewRelic.setTransactionName(null, "/globodns/signin");
		User user = new User(email, password);
		GloboDnsRoot<User> payload = new GloboDnsRoot<User>();
		payload.set("user", user);
		GloboDnsRoot<Authentication> globoDnsRoot = this.post("/users/sign_in.json", payload, false);
		if (globoDnsRoot == null) {
			throw new GloboDnsException("Invalid authentication response");
		}

		Authentication auth = globoDnsRoot.getFirstObject();
		return auth;
	}
}
