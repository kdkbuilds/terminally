# Jackson Library

Java does not have in built support for JSON objects. This is taken care by the `jackson` library. <br>

## Serialization: Converting POJO's into JSON
In order to effectively convert an object with fields into a JSON object, it is highly recommended to create a class first<br>
which will serve as the blueprint for the shape of individual JSON item.

```java
    // Immutable Class
    public final class Task {
    
        private final int id;
        private final String description;
    
        public Task(int id, String description) {
            this.id = id;
            this.description = description;
        }
        
        public int getId() {
            return id;
        }
    
        public String getDescription() {
            return description;
        }
    } 
```
The getter methods are needed by the jackson library to fetch the value associated with the private fields to construct the JSON.<br>

### JSON Key Naming Convention
The name of the getter method is converted into the name for the `key` for the JSON object. <br>
- `getUserID` will be converted into `userID`
- `getName` will be converted into `name`

This is the default behaviour and can be overriden with the use of `@JsonProperty` annotation

```java
import com.fasterxml.jackson.annotation.JsonProperty;

    @JsonProperty("user_display_name")
    public geName() {
        return name;
    }
```

### Implementation

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Client {

    public static void main(String[] args) {
        File file = new File("path/to/file");
        ObjectMapper mapper = new ObjectMapper();
        List<Task> tasks = new ArrayList<>();

        try {
            if (file.createNewFile()) {
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                mapper.writeValue(file, tasks);
            }
        } catch (Exception e) {
            System.out.println("Error occurred while serializing");
        }
    }
}
```
Now, the JSON object created will use key `user_display_name` instead of `name`

### Custom Indentation
Jackson's `mapper.enable(SerializationFeature.INDENT_OUTPUT)` provides only two white spaces for each indent by default. <br>
If you want a roomier feel, and want custom indentation, here is how to implement it.

```java
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

DefaultIndenter indenter = new DefaultIndenter("    ", "\n"); // custom white spacing 

DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
printer.

indentObjectsWith(indenter);
printer.

indentArraysWith(indenter);

// While serializing
ObjectMapper mapper = new ObjectMapper();
mapper.writer(printer).writeValue(file, object);
```

## De-serialization: Converting JSON into POJO's

### A. Using the no-args constructor (fake immutability)
```java
public final class Task {
    
    private final int id;
    private final String description;
    
    // no-args constructor
    public Task() {
        id = 0;
        description = null;
    }
    
    public Task(int id, String description) {
        this.id = id;
        this.description = description;
    }
    
    // getter methods below...
}
```

Here, `jackson` bypasses the standard Java immutability rules using `deep reflection` and can forcefully inject values <br>
in the `final` fields without setters. This approach is only for old legacy code bases and should be heavily avoided.

### B. Using `@JsonCreator and @JsonProperty` (Recommended)
 ```java
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

    @JsonCreator
    public Task(@JsonProperty("id") int id, @JsonProperty("description") String description) {
        this.id = id;
        this.description = description;
    }
```

The above two annotations instruct the `jackson` library to look for _exactly_ this constructor while creating the java object<br>
and the _exact JSON key_ mapping to the constructor parameter.

### Implementation

```java
import com.fasterxml.jackson.core.type.TypeReference;

// wrap snippet in try-catch
List<Task> tasks = mapper.readValue(file, new TypeReference<List<Task>>(){});
```

## Runtime Errors

During serialization and deserialization `mapper.writeValue(...)`, `mapper.readValue(...)` throw <br>
1. `IOException` if a low level I/O error occrus
2. `StreamWriteException`
2. `StreamReadException` if the JSON being read from source file is invalid
3. `DatabindException` if the input JSON structure does not match the structure of the required result type


