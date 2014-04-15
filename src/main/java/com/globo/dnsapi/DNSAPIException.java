package com.globo.dnsapi;

public class DNSAPIException extends RuntimeException {

	private static final long serialVersionUID = 5686288460895537688L;

	public DNSAPIException(String msg) {
		super(msg);
	}
	
	public DNSAPIException(String msg, Throwable e) {
		super(msg, e);
	}
}
