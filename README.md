GloboDNS-Client
==================

Java client for GloboDNS.

https://github.com/globocom/GloboDNS


## To release a new version

mvn clean release:prepare -Dgpg.passphrase='XXX'
mvn clean release:perform -Dgpg.passphrase='XXX'

