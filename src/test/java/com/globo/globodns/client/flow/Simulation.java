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
package com.globo.globodns.client.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.globo.globodns.client.GloboDns;
import com.globo.globodns.client.GloboDnsException;
import com.globo.globodns.client.api.AuthAPI;
import com.globo.globodns.client.api.DomainAPI;
import com.globo.globodns.client.api.RecordAPI;
import com.globo.globodns.client.model.Authentication;
import com.globo.globodns.client.model.Domain;
import com.globo.globodns.client.model.Record;
import com.google.api.client.http.HttpTransport;

public class Simulation {

    private static final Logger LOGGER = LoggerFactory.getLogger(Simulation.class);

    public static void main(String[] args) throws GloboDnsException {
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
        GloboDns rp = GloboDns.buildHttpApi(baseUrl, email, password);

        AuthAPI authAPI = rp.getAuthAPI();
        DomainAPI domainAPI = rp.getDomainAPI();
        RecordAPI recordAPI = rp.getRecordAPI();

        String domainName = "cp.dev.globoi.com";
        String newDomainName = "newdomain.dev.globoi.com";
        String newReverseDomainName = "2.1.10.in-addr.arpa";
        String newReverseIpv6DomainName = "9.2.3.8.e.1.e.f.f.f.3.b.2.0.2.0.ip6.arpa";
        String newAuthType = "M";
        String updateTTL = "11000";
        String newRecordName = "@";
        String newRecordContent = "ns1.newdomain.dev.globoi.com.";
        String newRecordType = "NS";
        String recordNameIpv6 = "record-ipv6";
        String recordContentIpv6 = "FE80:0000:0000:0000:0202:B3FF:FE1E:8329";

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

        // Step 4: Create a new domain and a new reverse domain (IPv4 and IPv6)
        LOGGER.info("Creating domain with name=" + newDomainName + " and authority type=" + newAuthType);
        domain = domainAPI.createDomain(newDomainName, 1L, newAuthType);

        assertNotNull(domain);
        LOGGER.info("Domain created successfully with id=" + domain.getId());

        LOGGER.info("Creating reverse domain with name=" + newReverseDomainName + " and authority type=" + newAuthType);
        Domain reverseDomain = domainAPI.createReverseDomain(newReverseDomainName, 1L, newAuthType);

        assertNotNull(reverseDomain);
        LOGGER.info("Reverse domain created successfully with id=" + reverseDomain.getId());

        LOGGER.info("Creating reverse IPv6 domain with name=" + newReverseIpv6DomainName + " and authority type=" + newAuthType);
        Domain reverseIpv6Domain = domainAPI.createReverseDomain(newReverseIpv6DomainName, 1L, newAuthType);

        assertNotNull(reverseIpv6Domain);
        LOGGER.info("Reverse IPv6 domain created successfully with id=" + reverseIpv6Domain.getId());

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
        Record recordtxt = recordAPI.createRecord(domain.getId(), recordNameTxt, recordContentTxt, "TXT");

        assertNotNull(recordtxt);
        LOGGER.info("Record created successfully with id=" + recordtxt.getId());

        // Step 10: Create IPv6 record on the created domain
        LOGGER.info("Creating IPv6 record with name=" + recordNameIpv6 + " and content=" + recordContentIpv6);
        Record recordIpv6 = recordAPI.createRecord(domain.getId(), recordNameIpv6, recordContentIpv6, "AAAA");

        assertNotNull(recordIpv6);
        LOGGER.info("Record created successfully with id=" + recordIpv6.getId());

        // Step 11: Remove all records from the created domain
        LOGGER.info("Removing all records from the created domain");
        recordList = recordAPI.listAll(domain.getId());
        assertNotNull(recordList);
        assertEquals(5, recordList.size()); // 2 by default + 3 created (A, TXT and AAAA)
        for (Record record : recordList) {
            LOGGER.info(">> Removing record " + record.getName());
            recordAPI.removeRecord(record.getId());
        }
        LOGGER.info("Removed " + recordList.size() + " records");

        // Step 12: Create reverse record
        String reverseRecordName = "134";
        String reverseRecordContent = newRecordName + ".";
        LOGGER.info("Creating reverse record with name=" + reverseRecordName + " and content=" + reverseRecordContent);
        Record reverseRecord = recordAPI.createRecord(reverseDomain.getId(), reverseRecordName, reverseRecordContent, "PTR");

        assertNotNull(reverseRecord);
        LOGGER.info("Reverse record created successfully with id=" + reverseRecord.getId());

        // Step 13: Create reverse IPv6 record
        String reverseRecordNameIpv6 = "0.0.0.0.0.0.0.0.0.0.0.0.0.8.e.f";
        String reverseRecordContentIpv6 = recordNameIpv6 + ".";
        LOGGER.info("Creating reverse IPv6 record with name=" + reverseRecordNameIpv6 + " and content=" + reverseRecordContentIpv6);
        Record reverseRecordIpv6 = recordAPI.createRecord(reverseIpv6Domain.getId(), reverseRecordNameIpv6, reverseRecordContentIpv6, "PTR");

        assertNotNull(reverseRecordIpv6);
        LOGGER.info("Reverse IPv6 record created successfully with id=" + reverseRecordIpv6.getId());

        // Step 14: Remove all reverse records from the created reverse domain
        LOGGER.info("Removing all reverse records from the created reverse domain");
        recordList = recordAPI.listAll(reverseDomain.getId());
        assertNotNull(recordList);
        assertEquals(3, recordList.size()); // 2 by default + 1 created (IPv4)
        for (Record record : recordList) {
            LOGGER.info(">> Removing record " + record.getName());
            recordAPI.removeRecord(record.getId());
        }
        LOGGER.info("Removed " + recordList.size() + " records");

        // Step 15: Remove all reverse IPv6 records from the created reverse IPv6 domain
        LOGGER.info("Removing all reverse records from the created reverse domain");
        recordList = recordAPI.listAll(reverseIpv6Domain.getId());
        assertNotNull(recordList);
        assertEquals(3, recordList.size()); // 2 by default + 1 created (IPv6)
        for (Record record : recordList) {
            LOGGER.info(">> Removing record " + record.getName());
            recordAPI.removeRecord(record.getId());
        }
        LOGGER.info("Removed " + recordList.size() + " records");

        // Step 16: Remove the created domain and the reverse domain
        LOGGER.info("Removing domain with name=" + domain.getName() + " and id=" + domain.getId());
        domainAPI.removeDomain(domain.getId());
        LOGGER.info("Domain removed successfully");

        LOGGER.info("Removing reverse domain with name=" + reverseDomain.getName() + " and id=" + reverseDomain.getId());
        domainAPI.removeReverseDomain(reverseDomain.getId());
        LOGGER.info("Reverse domain removed successfully");

        LOGGER.info("Removing reverse IPv6 domain with name=" + reverseIpv6Domain.getName() + " and id=" + reverseIpv6Domain.getId());
        domainAPI.removeReverseDomain(reverseIpv6Domain.getId());
        LOGGER.info("Reverse IPv6 domain removed successfully");
    }
}
