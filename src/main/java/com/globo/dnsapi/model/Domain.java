package com.globo.dnsapi.model;

import java.sql.Timestamp;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class Domain extends GenericJson {

	@Key
	private Long id;
	
	@Key
	private String account;
	
	@Key("addressing_type")
	private String addressType;
	
	@Key("authority_type")
	private String authorityType;
	
	@Key("created_at")
	private Timestamp createdAt;
	
	@Key("last_check")
	private String lastCheck;
	
	@Key
	private String master;
	
	@Key
	private String name;
	
	//FIXME Include other fields

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getAuthorityType() {
		return authorityType;
	}

	public void setAuthorityType(String authorityType) {
		this.authorityType = authorityType;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getLastCheck() {
		return lastCheck;
	}

	public void setLastCheck(String lastCheck) {
		this.lastCheck = lastCheck;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Domain() {
	}
}
