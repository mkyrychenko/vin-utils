# Vehicle Identification Number utilities

Library to generate and validate VIN's

Add to project in `pom.xml`
```xml
<dependency>
    <groupId>com.github.mkyrychenko</groupId>
    <artifactId>vin-utils</artifactId>
    <version>1.0.1</version>
</dependency>
```

For other languages look at [Maven Central](https://search.maven.org/beta/artifact/com.github.mkyrychenko/vin-utils/1.1.0/jar)

### Usage

To generate or validate some VIN, just call corresponding static methods
```java
import com.github.mkyrychenko.VinUtils;

class MyClass {
    public String generate(){
        return VinUtils.getRandomVin();
    }

    public boolean validate(final String vin){
        return VinUtils.isValidVin(vin);
    }
}
```

To expose exceptions of validation, use `VinUtils.validate(vin)` 
instead of `VinUtils.isValidVin(vin)`.

In case of wrong VIN, the `InvalidVinException` containing information,
why validation has been failed, will be thrown.

The exception handling should be implemented in this case.
```java
...
    public boolean validate(final String vin) throws InvalidVinException {
        return VinUtils.validate(vin);
    }
...
```

In applications, using validation constrains 
([Spring](https://spring.io/) for example), integrate VIN validation
due marking the field, method or parameter with `@VIN` annotation
in validation context.

In [Spring](https://spring.io/) the POJO's, that should be validated looks like
```java
class MyPojo {
    @NotNull
    private String id;
    
    @VIN
    private String id;
}
```

or method param
```java
@RestController
class MyController {

    @PostMapping("/{vin}")
    public saveVin(final @Valid @VIN @PathVariable("vin") String vin){
        // do my things
    }
}
```

> Information for maintainer available in the description of [deployment](deployment.md) process