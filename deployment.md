> This description used only for maintainer of project

##### Open Source Project Repository Hosting
Ticket [OSSRH-39506](https://issues.sonatype.org/browse/OSSRH-39506) 
to deploy a library was created at [sonatype.org](sonatype.org), using Apache's 
[Guide to uploading artifacts to the Central Repository](https://maven.apache.org/repository/guide-central-repository-upload.html) 
and [OSSRH Guide](http://central.sonatype.org/pages/ossrh-guide.html).

Library is hosted at [Sonatype Maven Repository](https://oss.sonatype.org/content/groups/public/com/github/mkyrychenko/vin-utils/)

#### Requirements
For deployment process some requirements should be fulfilled: 

* Installed Java 8+
* Installed and configured (`settings.xml`) Maven 3+
* Installed and configured GPG ([GnuPG](https://www.gnupg.org/index.html))

#### Deploy
To set a new version of library use
```bash
$ mvn versions:set -DnewVersion=x.x.x
```
After new version has been set, deploy to [sonatype](https://sonatype.org) with
```bash
$ mvn deploy
```

In case of troubles with deployment, check maven's `settings.xml` file to contain
```xml
<server>
    <id>ossrh</id>
    <username>sonatype-username-token</username>
    <password>sonatype-access-token-encoded</password>
</server>
```

To retrieve access token, login to Sonatype [`Nexus Repository Manager`](https://oss.sonatype.org) 
and after opening `Profile`, select [`User Token`](https://oss.sonatype.org/#profile;User%20Token) 
from drop-down list.

Click `Access User Token` button and after entering of current user password, 
take provided access tokens into `settings.xml`.

For successful signing of artifact, corresponding profile should also be set in `settings.xml`
```xml
<profile>
    <id>ossrh</id>
    <activation>
        <activeByDefault>true</activeByDefault>
    </activation>
    <properties>
        <gpg.executable>gpg2</gpg.executable>
        <gpg.passphrase>password-of-gpg-key</gpg.passphrase>
    </properties>
</profile>
```

It is recommended to keep all passwords in encrypted form. 
Detailed learn at [Maven Password Encryption](https://maven.apache.org/guides/mini/guide-encryption.html) Guide
