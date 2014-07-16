dnsapi-client-java
==================

Java client for DNS API.

https://github.com/globocom/dns-api


## To release a new version

mvn clean release:prepare -Dgpg.passphrase='XXX'
mvn clean release:perform -Dgpg.passphrase='XXX'

