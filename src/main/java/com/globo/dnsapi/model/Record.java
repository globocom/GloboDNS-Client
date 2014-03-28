package com.globo.dnsapi.model;

import com.google.api.client.util.Key;

public class Record {
	@Key("record")
	private GenericRecordAttributes genericRecordAttributes;
	
	@Key("a")
	private TypeARecordAttributes typeARecordAttributes;
	
	@Key("ns")
	private TypeNSRecordAttributes typeNSRecordAttributes;
	
	@Key("soa")
	private TypeSOARecordAttributes typeSOARecordAttributes;
	
	@Key("mx")
	private TypeMXRecordAttributes typeMXRecordAttributes;
	
	public GenericRecordAttributes getGenericRecordAttributes() {
		return this.genericRecordAttributes;
	}
	
	public void setGenericRecordAttributes(GenericRecordAttributes genericRecordAttributes) {
		this.genericRecordAttributes = genericRecordAttributes;
	}
	
	public TypeARecordAttributes getTypeARecordAttributes() {
		return this.typeARecordAttributes;
	}
	
	public void setTypeARecordAttributes(TypeARecordAttributes typeARecordAttributes) {
		this.typeARecordAttributes = typeARecordAttributes;
	}
	
	public TypeNSRecordAttributes getTypeNSRecordAttributes() {
		return this.typeNSRecordAttributes;
	}
	
	public void setTypeNSRecordAttributes(TypeNSRecordAttributes typeNSRecordAttributes) {
		this.typeNSRecordAttributes = typeNSRecordAttributes;
	}
	
	public TypeSOARecordAttributes getTypeSOARecordAttributes() {
		return this.typeSOARecordAttributes;
	}
	
	public void setTypeSOARecordAttributes(TypeSOARecordAttributes typeSOARecordAttributes) {
		this.typeSOARecordAttributes = typeSOARecordAttributes;
	}
	
	public TypeMXRecordAttributes getTypeMXRecordAttributes() {
		return this.typeMXRecordAttributes;
	}
	
	public void setTypeMXRecordAttributes(TypeMXRecordAttributes typeMXRecordAttributes) {
		this.typeMXRecordAttributes = typeMXRecordAttributes;
	}
	
	public Record() {
		this.genericRecordAttributes = new GenericRecordAttributes();
		this.typeARecordAttributes = new TypeARecordAttributes();
		this.typeNSRecordAttributes = new TypeNSRecordAttributes();
		this.typeSOARecordAttributes = new TypeSOARecordAttributes();
		this.typeMXRecordAttributes = new TypeMXRecordAttributes();
	}
	
	
	/////////////////////////////////
	// Attributes direct accessors //
	/////////////////////////////////
	// FIXME Better way of accessing the variables?
	public Long getId() {
		if (this.genericRecordAttributes.getId() != null) {
			return this.genericRecordAttributes.getId();
		} else if (this.typeARecordAttributes.getId() != null) {
			return this.typeARecordAttributes.getId();
		} else if (this.typeNSRecordAttributes.getId() != null) {
			return this.typeNSRecordAttributes.getId();
		} else if (this.typeSOARecordAttributes.getId() != null) {
			return this.typeSOARecordAttributes.getId();
		} else if (this.typeMXRecordAttributes.getId() != null) {
			return this.typeMXRecordAttributes.getId();
		} else {
			return null;
		}
	}
	
	public String getName() {
		if (this.genericRecordAttributes.getName() != null) {
			return this.genericRecordAttributes.getName();
		} else if (this.typeARecordAttributes.getName() != null) {
			return this.typeARecordAttributes.getName();
		} else if (this.typeNSRecordAttributes.getName() != null) {
			return this.typeNSRecordAttributes.getName();
		} else if (this.typeSOARecordAttributes.getName() != null) {
			return this.typeSOARecordAttributes.getName();
		} else if (this.typeMXRecordAttributes.getName() != null) {
			return this.typeMXRecordAttributes.getName();
		} else {
			return null;
		}
	}
	
	public String getContent() {
		if (this.genericRecordAttributes.getContent() != null) {
			return this.genericRecordAttributes.getContent();
		} else if (this.typeARecordAttributes.getContent() != null) {
			return this.typeARecordAttributes.getContent();
		} else if (this.typeNSRecordAttributes.getContent() != null) {
			return this.typeNSRecordAttributes.getContent();
		} else if (this.typeSOARecordAttributes.getContent() != null) {
			return this.typeSOARecordAttributes.getContent();
		} else if (this.typeMXRecordAttributes.getContent() != null) {
			return this.typeMXRecordAttributes.getContent();
		} else {
			return null;
		}
	}
	
	
	///////////////////////////////
	// Generic Record Attributes //
	///////////////////////////////
	public static class GenericRecordAttributes {
		
		@Key
		private Long id;
		
		@Key("domain_id")
		private Long domainId;
		
		@Key
		private String content;
		
		@Key
		private String type;
		
		@Key("created_at")
		private String createdAt;
		
		@Key
		private String name;
		
		@Key("prio")
		private Integer priority;
		
		@Key
		private String ttl;
		
		@Key("updated_at")
		private String updatedAt;
		
		@Key("change_date")
		private String changeDate;

		public GenericRecordAttributes() {
		}
		
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getDomainId() {
			return domainId;
		}

		public void setDomainId(Long domainId) {
			this.domainId = domainId;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
		
		public String getType() {
			return type;
		}
		
		public void setType(String type) {
			this.type = type;
		}

		public String getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(String createdAt) {
			this.createdAt = createdAt;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getPriority() {
			return priority;
		}

		public void setPriority(Integer priority) {
			this.priority = priority;
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
		
		public String getChangeDate() {
			return changeDate;
		}
		
		public void setChangeDate(String changeDate) {
			this.changeDate = changeDate;
		}
	}
	
	///////////////////////////////
	// Type A Record Attributes //
	///////////////////////////////
	public static class TypeARecordAttributes extends GenericRecordAttributes {
		// Same attributes as super class
	}
	
	///////////////////////////////
	// Type NS Record Attributes //
	///////////////////////////////
	public static class TypeNSRecordAttributes extends GenericRecordAttributes {
		// Same attributes as super class		
	}
	
	///////////////////////////////
	// Type SOA Record Attributes //
	///////////////////////////////
	public static class TypeSOARecordAttributes extends GenericRecordAttributes {
		// Same attributes as super class
	}
	
	///////////////////////////////
	// Type MX Record Attributes //
	///////////////////////////////
	public static class TypeMXRecordAttributes extends GenericRecordAttributes {
		// Same attributes as super class
	}
}
