package com.globo.dnsapi.model;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.json.GenericJson;


public class DNSAPIRoot<T> extends GenericJson {

	private List<T> objectList;
	
	public List<T> getObjectList() {
		return this.objectList;
	}
	
	public void setObjectList(List<T> objectList) {
		this.objectList = objectList;
	}
	
	public DNSAPIRoot() {
		this.objectList = new ArrayList<T>();
	}
	
	/**
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
