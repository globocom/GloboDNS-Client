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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.globo.globodns.client.GloboDnsException;
import com.globo.globodns.client.MockGloboDns;
import com.globo.globodns.client.MockGloboDns.HttpMethod;
import com.globo.globodns.client.api.ExportAPI;
import com.globo.globodns.client.model.Export;

@RunWith(JUnit4.class)
public class ExportAPITest {

	private ExportAPI exportAPI;
	private MockGloboDns rp;
	
	@Before
	public void setUp() {
		this.rp = new MockGloboDns();
		this.exportAPI = this.rp.getExportAPI();
	}
	
	@Test
	public void testScheduleExport() throws GloboDnsException {
		this.rp.registerFakeRequest(HttpMethod.POST, "/bind9/schedule_export.json", 
				"{\"output\":\"BIND export scheduled for 2012-07-14 02:35:00 -0300\"}");
		
		Export export = this.exportAPI.scheduleExport();
		assertNotNull(export);
	}
}
