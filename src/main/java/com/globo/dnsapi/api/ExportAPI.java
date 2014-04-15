package com.globo.dnsapi.api;

import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.globo.dnsapi.AbstractAPI;
import com.globo.dnsapi.DNSAPIException;
import com.globo.dnsapi.DNSAPIFactory;
import com.globo.dnsapi.model.DNSAPIRoot;
import com.globo.dnsapi.model.Export;

public class ExportAPI extends AbstractAPI<Export> {

	public ExportAPI(DNSAPIFactory transport) {
		super(transport);
	}
	
	@Override
	protected Type getType() {
		return new TypeReference<Export>() {}.getType();
	}
	
	@Override
	protected Type getListType() {
		return new TypeReference<List<Export>>() {}.getType();
	}
	
	public Export scheduleExport() throws DNSAPIException {
		DNSAPIRoot<Export> dnsAPIRoot = this.post("/bind9/schedule_export.json", null, false);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return dnsAPIRoot.getFirstObject();
	}
}
