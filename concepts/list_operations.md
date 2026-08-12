# Useful lookup for List operations

#### Declare and Initialize an Immutable List simultaneously

#### 1. Make an immutable read-only copy
```java
List.copyOf(list);
```

#### 2. Declare and Initialize List simulataneously

```java
// Immutable: can not be modified later
List<String> fruits = List.of("Apple", "Banana", "Orange"); 

// Mutable
List<Integer> marks = new ArrayList<>(Arrays.asList(78, 89, 93, 66));
```

#### 3. Get the index of an element
```java
int index = fruits.indexOf("Banana");
// returns '-1' if element is not found
```

#### 4. Replace an element
```java
// A. Using an index
fruits.set(index, "Mango");

// B. Conditional/Bulk replacement
List<Integer> numbers = new ArrayList<>(Arrays.asList(10, 25, 34, 45, 22));
numbers.replaceAll(number -> number % 5 != 0 ? -1 : number);

// O/P: [10, 25, -1, 45, -1]
```

#### 5. Remove an Item
```java
// A. Using the index
List<String> mammals = new ArrayList<>(Arrays.asList("Dog", "Human", "Whale", "Deer"));
mammals.remove(2); // "Whale" will be removed

// B. Using the value
mammals.remove("Whale"); // returns true if found

// C. Using conditional
mammals.removeIf(mammal -> mammal.equals("Whale"));

// NOTE!
// For Integer lists, extra caution is needed, and cast should be done
List<Integer> numbers = new ArrayList<>(Arrays.asList(12, 34, 11, 56));
numbers.remove(34); // ❌ wrong, 34 is treated as index and not actual value
numbers.remove(Integer.valueOf(34)); // ✅ correct
```