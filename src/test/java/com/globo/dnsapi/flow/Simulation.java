package com.globo.dnsapi.flow;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.globo.dnsapi.DNSAPI;
import com.globo.dnsapi.DNSAPIException;
import com.globo.dnsapi.api.AuthAPI;
import com.globo.dnsapi.api.DomainAPI;
import com.globo.dnsapi.api.RecordAPI;
import com.globo.dnsapi.model.Authentication;
import com.globo.dnsapi.model.Domain;
import com.globo.dnsapi.model.Record;
import com.google.api.client.http.HttpTransport;

public class Simulation {

	private static final Logger LOGGER = LoggerFactory.getLogger(Simulation.class);

	public static void main(String[] args) throws DNSAPIException {
		String baseUrl = "http://example.com/";
		String email = "admin@globoi.com";
		String password = "password";
		
		LOGGER.info("Using url " + baseUrl);
		
		// Setup java logging
		java.util.logging.Logger httpLogging = java.util.logging.Logger.getLogger(HttpTransport.class.getName());
		httpLogging.setLevel(Level.ALL);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter());
		handler.setLevel(Level.ALL);
		httpLogging.addHandler(handler);
		
		// Start
		DNSAPI rp = DNSAPI.buildHttpApi(baseUrl, email, password);
		
		AuthAPI authAPI = rp.getAuthAPI();
		DomainAPI domainAPI = rp.getDomainAPI();
		RecordAPI recordAPI = rp.getRecordAPI();
		
		String domainName = "cp.dev.globoi.com";
		String newDomainName = "newdomain.dev.globoi.com";
		String newAuthType = "M";
		String updateTTL = "11000";
		String newRecordName = "@";
		String newRecordContent = "ns1.newdomain.dev.globoi.com.";
		String newRecordType = "NS";
		
		
		// Step 1: Sign in
		LOGGER.info("Signing in with email=" + email + " and password=" + password);
		Authentication auth = authAPI.signIn(email, password);
		
		assertNotNull(auth);
		LOGGER.info("Logged in successfully. Token=" + auth.getToken());
		
		
		// Step 2: List all domains
		LOGGER.info("Listing all domains");
		List<Domain> domainList = domainAPI.listAll();
		
		assertNotNull(domainList);
		String domains = "";
		for (Domain domain : domainList) {
			domains += domain.getName() + ", ";
		}
		LOGGER.info("Found " + domainList.size() + " domains: " + domains.substring(0, domains.length() - 2));
		
		
		// Step 3: List specific domain
		LOGGER.info("Getting info about specific domain: " + domainName);
		domainList = domainAPI.listByQuery(domainName);
		
		assertNotNull(domainList);
		assertEquals(1, domainList.size());
		
		Domain domain = domainList.get(0);
		LOGGER.info("Found domain with name=" + domainName + ": id=" + domain.getId());
		
				
		// Step 4: Create a new domain
		LOGGER.info("Creating domain with name=" + newDomainName + " and authority type=" + newAuthType);
		domain = domainAPI.createDomain(newDomainName, 1L, newAuthType);
		
		assertNotNull(domain);
		LOGGER.info("Domain created successfully with id=" + domain.getId());
		
		
		// Step 5: Get the domain we just created
		LOGGER.info("Retrieving created domain with name=" + domain.getName() + "  and id=" + domain.getId());
		domain = domainAPI.getById(domain.getId());
		
		assertNotNull(domain);
		LOGGER.info("Domain retrieved successfully");
		
		
		// Step 6: Update data on the created domain
		LOGGER.info("Updating created domain with TTL=" + updateTTL);
		domainAPI.updateDomain(domain.getId(), null, null, updateTTL);
		LOGGER.info("Domain updated successfully");
		
		
		// Step 7: List all records on the created domain
		LOGGER.info("Listing all records on the created domain");
		List<Record> recordList = recordAPI.listAll(domain.getId());
		assertNotNull(recordList);
		assertEquals(2, recordList.size()); // Template creates domain with 2 records by default
		String records = "";
		for (Record record : recordList) {
			records += record.getName() + ", ";
		}
		LOGGER.info("Found " + recordList.size() + " records: " + records.substring(0, records.length() - 2));
		
		
		// Step 8: Create a new record on the created domain
		LOGGER.info("Creating " + newRecordType + " record with name=" + newRecordName + " and content=" + newRecordContent);
		Record createdRecord = recordAPI.createRecord(domain.getId(), newRecordName, newRecordContent, newRecordType);
		
		assertNotNull(createdRecord);
		LOGGER.info("Record created successfully with id=" + createdRecord.getId());
		
		// Step 9: Create TXT record on the created domain
		String recordNameTxt = "cloudstack-network";
		String recordContentTxt = "df83j-dfy8";
		LOGGER.info("Creating TXT record with name=" + recordNameTxt + " and content=" + recordContentTxt);
		Record recordtxt = recordAPI.createRecord(domain.getId(), recordNameTxt, recordNameTxt, "TXT");
		
		assertNotNull(recordtxt);
		LOGGER.info("Record created successfully with id=" + recordtxt.getId());
		
		
		// Step 10: Remove all records on the created domain
		LOGGER.info("Removing all records on the created domain");
		recordList = recordAPI.listAll(domain.getId());
		assertNotNull(recordList);
		assertEquals(4, recordList.size()); // 2 by default + 1 just created
		for (Record record : recordList) {
			LOGGER.info(">> Removing record " + record.getName());
			recordAPI.removeRecord(record.getId());
		}
		LOGGER.info("Removed " + recordList.size() + " records");
		
		
		// Step 11: Remove the created domain
		LOGGER.info("Removing domain with name=" + domain.getName() + " and id=" + domain.getId());
		domainAPI.removeDomain(domain.getId());
		LOGGER.info("Domain removed successfully");
	}
}
