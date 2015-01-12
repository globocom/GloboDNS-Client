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

import com.google.api.client.util.Key;

public class Record {
    @Key("record")
    private GenericRecordAttributes genericRecordAttributes;

    @Key("a")
    private TypeARecordAttributes typeARecordAttributes;

    @Key("aaaa")
    private TypeAAAARecordAttributes typeAAAARecordAttributes;

    @Key("ns")
    private TypeNSRecordAttributes typeNSRecordAttributes;

    @Key("soa")
    private TypeSOARecordAttributes typeSOARecordAttributes;

    @Key("mx")
    private TypeMXRecordAttributes typeMXRecordAttributes;

    @Key("ptr")
    private TypePTRRecordAttributes typePTRRecordAttributes;

    @Key("txt")
    private TypeTXTRecordAttributes typeTXTRecordAttributes;

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

    public TypeAAAARecordAttributes getTypeAAAARecordAttributes() {
        return this.typeAAAARecordAttributes;
    }

    public void setTypeAAAARecordAttributes(TypeAAAARecordAttributes typeAAAARecordAttributes) {
        this.typeAAAARecordAttributes = typeAAAARecordAttributes;
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

    public TypePTRRecordAttributes getTypePTRRecordAttributes() {
        return this.typePTRRecordAttributes;
    }

    public void setTypePTRRecordAttributes(TypePTRRecordAttributes typePTRRecordAttributes) {
        this.typePTRRecordAttributes = typePTRRecordAttributes;
    }

    public TypeTXTRecordAttributes getTypeTXTRecordAttributes() {
        return this.typeTXTRecordAttributes;
    }

    public void setTypeTXTRecordAttributes(TypeTXTRecordAttributes typeTXTRecordAttributes) {
        this.typeTXTRecordAttributes = typeTXTRecordAttributes;
    }

    public Record() {
        this.genericRecordAttributes = new GenericRecordAttributes();
        this.typeARecordAttributes = new TypeARecordAttributes();
        this.typeAAAARecordAttributes = new TypeAAAARecordAttributes();
        this.typeNSRecordAttributes = new TypeNSRecordAttributes();
        this.typeSOARecordAttributes = new TypeSOARecordAttributes();
        this.typeMXRecordAttributes = new TypeMXRecordAttributes();
        this.typePTRRecordAttributes = new TypePTRRecordAttributes();
        this.typeTXTRecordAttributes = new TypeTXTRecordAttributes();
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
        } else if (this.typeAAAARecordAttributes.getId() != null) {
            return this.typeAAAARecordAttributes.getId();
        } else if (this.typeNSRecordAttributes.getId() != null) {
            return this.typeNSRecordAttributes.getId();
        } else if (this.typeSOARecordAttributes.getId() != null) {
            return this.typeSOARecordAttributes.getId();
        } else if (this.typeMXRecordAttributes.getId() != null) {
            return this.typeMXRecordAttributes.getId();
        } else if (this.typePTRRecordAttributes.getId() != null) {
            return this.typePTRRecordAttributes.getId();
        } else if (this.typeTXTRecordAttributes.getId() != null) {
            return this.typeTXTRecordAttributes.getId();
        } else {
            return null;
        }
    }

    public String getName() {
        if (this.genericRecordAttributes.getName() != null) {
            return this.genericRecordAttributes.getName();
        } else if (this.typeARecordAttributes.getName() != null) {
            return this.typeAAAARecordAttributes.getName();
        } else if (this.typeAAAARecordAttributes.getName() != null) {
            return this.typeARecordAttributes.getName();
        } else if (this.typeNSRecordAttributes.getName() != null) {
            return this.typeNSRecordAttributes.getName();
        } else if (this.typeSOARecordAttributes.getName() != null) {
            return this.typeSOARecordAttributes.getName();
        } else if (this.typeMXRecordAttributes.getName() != null) {
            return this.typeMXRecordAttributes.getName();
        } else if (this.typePTRRecordAttributes.getName() != null) {
            return this.typePTRRecordAttributes.getName();
        } else if (this.typeTXTRecordAttributes.getName() != null) {
            return this.typeTXTRecordAttributes.getName();
        } else {
            return null;
        }
    }

    public String getContent() {
        if (this.genericRecordAttributes.getContent() != null) {
            return this.genericRecordAttributes.getContent();
        } else if (this.typeARecordAttributes.getContent() != null) {
            return this.typeARecordAttributes.getContent();
        } else if (this.typeAAAARecordAttributes.getContent() != null) {
            return this.typeAAAARecordAttributes.getContent();
        } else if (this.typeNSRecordAttributes.getContent() != null) {
            return this.typeNSRecordAttributes.getContent();
        } else if (this.typeSOARecordAttributes.getContent() != null) {
            return this.typeSOARecordAttributes.getContent();
        } else if (this.typeMXRecordAttributes.getContent() != null) {
            return this.typeMXRecordAttributes.getContent();
        } else if (this.typePTRRecordAttributes.getContent() != null) {
            return this.typePTRRecordAttributes.getContent();
        } else if (this.typeTXTRecordAttributes.getContent() != null) {
            return this.typeTXTRecordAttributes.getContent();
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
    // Type AAAA Record Attributes //
    ///////////////////////////////
    public static class TypeAAAARecordAttributes extends GenericRecordAttributes {
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

    ///////////////////////////////
    // Type PTR Record Attributes //
    ///////////////////////////////
    public static class TypePTRRecordAttributes extends GenericRecordAttributes {
        // Same attributes as super class
    }

    ///////////////////////////////
    // Type PTR Record Attributes //
    ///////////////////////////////
    public static class TypeTXTRecordAttributes extends GenericRecordAttributes {
        // Same attributes as super class
    }
}
