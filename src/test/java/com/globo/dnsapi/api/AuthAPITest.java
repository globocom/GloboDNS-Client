package com.globo.dnsapi.api;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.globo.dnsapi.api.AuthAPI;
import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.http.HttpJsonRequestProcessor;
import com.globo.dnsapi.model.Authentication;

@RunWith(JUnit4.class)
public class AuthAPITest {

	private AuthAPI authAPI;
	private HttpJsonRequestProcessor rp;
	
	@Before
	public void setUp() {
		// FIXME Use TestRequestProcessor instead
		this.rp = new HttpJsonRequestProcessor("http://globodns.dev.globoi.com/");
		this.authAPI = this.rp.getAuthAPI();
	}
	
	@Test
	public void testSignIn() throws DNSAPIException {
		
		Authentication auth = this.authAPI.signIn("admin@globoi.com", "password");
		assertNotNull(auth);
		System.out.println(auth.getId());
		System.out.println(auth.getToken());
	}

}
