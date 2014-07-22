package com.globo.globodns.client.model;

import com.google.api.client.util.Key;

public class Domain {
	@Key("domain")
	private DomainAttributes domainAttributes;
	
	public DomainAttributes getDomainAttributes() {
		return this.domainAttributes;
	}
	
	public void setDomainAttributes(DomainAttributes domainAttributes) {
		this.domainAttributes = domainAttributes;
	}
	
	public Domain() {
		this.domainAttributes = new DomainAttributes();
	}
	
	/////////////////////////////////
	// Attributes direct accessors //
	/////////////////////////////////
	public Long getId() {
		return this.domainAttributes.getId();
	}
	
	public String getAccount() {
		return this.domainAttributes.getAccount();
	}

	public String getAddressType() {
		return this.domainAttributes.getAddressType();
	}

	public String getAuthorityType() {
		return this.domainAttributes.getAuthorityType();
	}

	public String getCreatedAt() {
		return this.domainAttributes.getCreatedAt();
	}

	public String getLastCheck() {
		return this.domainAttributes.getLastCheck();
	}

	public String getMaster() {
		return this.domainAttributes.getMaster();
	}

	public String getName() {
		return this.domainAttributes.getName();
	}

	public String getNotes() {
		return this.domainAttributes.getNotes();
	}

	public String getNotifiedSerial() {
		return this.domainAttributes.getNotifiedSerial();
	}

	public String getTTL() {
		return this.domainAttributes.getTTL();
	}

	public String getUpdatedAt() {
		return this.domainAttributes.getUpdatedAt();
	}

	public Long getUserId() {
		return this.domainAttributes.getUserId();
	}

	public Long getViewId() {
		return this.domainAttributes.getViewId();
	}
	
	public Long getTemplateId() {
		return this.domainAttributes.getTemplateId();
	}
	
	
	///////////////////////
	// Domain attributes //
	///////////////////////
	public static class DomainAttributes {

		@Key
		private Long id;
		
		@Key
		private String account;
		
		@Key("addressing_type")
		private String addressType;
		
		@Key("authority_type")
		private String authorityType;
		
		@Key("created_at")
		private String createdAt;
		
		@Key("last_check")
		private String lastCheck;
		
		@Key
		private String master;
		
		@Key
		private String name;
		
		@Key
		private String notes;
		
		@Key("notified_serial")
		private String notifiedSerial;
		
		@Key
		private String ttl;
		
		@Key("updated_at")
		private String updatedAt;
		
		@Key("user_id")
		private Long userId;
		
		@Key("view_id")
		private Long viewId;
		
		@Key("domain_template_id")
		private Long templateId;
				
		public DomainAttributes() {
		}

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

		public String getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(String createdAt) {
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

		public String getNotes() {
			return notes;
		}

		public void setNotes(String notes) {
			this.notes = notes;
		}

		public String getNotifiedSerial() {
			return notifiedSerial;
		}

		public void setNotifiedSerial(String notifiedSerial) {
			this.notifiedSerial = notifiedSerial;
		}

		public String getTTL() {
			return ttl;
		}

		public void setTTL(String ttl) {
			this.ttl = ttl;
		}

		public String getUpdatedAt() {
			return updatedAt;
		}

		public void setUpdatedAt(String updatedAt) {
			this.updatedAt = updatedAt;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public Long getViewId() {
			return viewId;
		}

		public void setViewId(Long viewId) {
			this.viewId = viewId;
		}
		
		public Long getTemplateId() {
			return templateId;
		}
		
		public void setTemplateId(Long templateId) {
			this.templateId = templateId;
		}
	}
}
