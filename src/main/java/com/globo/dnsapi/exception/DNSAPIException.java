package com.globo.dnsapi.exception;

public class DNSAPIException extends Exception {

	private static final long serialVersionUID = 5686288460895537688L;

	public DNSAPIException(String msg) {
		super(msg);
	}
	
	public DNSAPIException(String msg, Throwable e) {
		super(msg, e);
	}
}
