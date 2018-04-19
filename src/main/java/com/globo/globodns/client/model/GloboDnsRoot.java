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
package com.globo.globodns.client.model;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.json.GenericJson;


public class GloboDnsRoot<T> extends GenericJson {

	private List<T> objectList;
	
	public List<T> getObjectList() {
		return this.objectList;
	}
	
	public void setObjectList(List<T> objectList) {
		this.objectList = objectList;
	}
	
	public GloboDnsRoot() {
		this.objectList = new ArrayList<T>();
	}
	
	/*
	 * Return first object in list or <code>null</code> if list is empty
	 * @return
	 */
	public T getFirstObject() {
		if (this.getObjectList() == null || this.getObjectList().isEmpty()) {
			return null;
		}
		return this.getObjectList().get(0);
	}
}
