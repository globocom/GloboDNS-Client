package com.globo.dnsapi.api;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.http.HttpJsonRequestProcessor;
import com.globo.dnsapi.model.Domain;

@RunWith(JUnit4.class)
public class DomainAPITest {

	private DomainAPI domainAPI;
	private HttpJsonRequestProcessor rp; // FIXME Use TestRequestProcessor instead
	private String token = "Xjn5GEsYsQySAsr7APqj";
	
	@Before
	public void setUp() {
		this.rp = new HttpJsonRequestProcessor("http://globodns.dev.globoi.com/");
		this.domainAPI = this.rp.getDomainAPI(this.token);
	}
	
	@Test
	public void testListDomains() throws DNSAPIException {
		
		Domain domain = this.domainAPI.listByName("cp.dev.globoi.com");
		assertNotNull(domain);
		// for (Domain domain : domainList) {
		//	System.out.println("========");
			System.out.println(domain.getId());
			System.out.println(domain.getName());
		// }
	}

}
