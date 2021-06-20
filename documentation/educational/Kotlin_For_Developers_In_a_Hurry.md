# Kotlin for developers in a hurry
This is a short introduction guide/cheatsheat we created during development of the initial version of the TestKnight IntelliJ IDEA plugin. 

## Intro
Kotlin is a general purpose statically typed language developed by JetBrains in 2011. It can be compiled to JVM bytecode, Javascript and even native code. It is designed to interoperate fully with Java. 


Kotlin programs are (usually) stored in `.kt` files. In contrast to Java it is not required that this files contain a class.

## Hello World
Kotlin starts executing the program from the `main` function of the `Main.kt` file. This function looks like this:

```kotlin
fun main() {
    println("Hello World!")
}
```

Notice here the absence of parameters to the `main` function as well as the absence of `;`.

## Variables
There are 2 general types of variables in Kotlin:

### Types of variables
* **Mutable**: Mutable variables are declared using the `var` keyword.
```kotlin
var answer: Int = 42
```
* **Imutable**: Immutable variables are declared using the `val` keyword.
```kotlin
val answer: Int = 42
```

### Type inference
Notice that after the name we also give the type of the variable. This is not strictly required since Kotlin supports **type inference**, meaning that the following also compiles and is type-safe:
```kotlin
val answer = 42
```
Although this syntax is a bit cleaner I suggest you use explicit type declarations, as they make it easier for someone else on the team (and future you) to understand what's going on.

### Null
Kotlin tries to be a null safe language, therefore variables cannot by default take null values. For example the following program would **not** compile:
```kotlin
var name: String = null
```
If we want a variable to be able to take null values we need to mark it as nullable by adding the `?` character after the type name. The following program compiles successfully:
```kotlin
var name: String? = null
```
Use nullable types with caution as they make programs "null unsafe" making for, at best nasty null checks in the code and at worst, bugs. 

### Equality
Equality checking in Kotlin works by comparing values by default. Concretely `stringA == stringB` returns `true` iff `stringA` has the same content as `stringB`. If we want to compare variables based on their memory address we can use the `===` operator. 

An intresting thing here can be showcased by the following program:
```kotlin
fun main() {
    val a: String = "Bruce Wayne"
    val b: String = "Bruce Wayne"
    println(a === b)
}
```
What do you expect this program to output? Well if you are anything like me, you thought this would return `false` since `a` and `b` are 2 different variables. This is actually **not** the case here. The program returns `true`. The JVM notices that the 2 strings have the same content and to save on memory makes `b` point to the memory location of `a`.  Apparently [Java does the same](https://stackoverflow.com/questions/13450392/why-are-equal-java-strings-taking-the-same-address).

## String Templates
To modify a string based on a variable you can go the classic way of using `+` or you can use string templates!
```kotlin
val name: String = "Bruce"
println("Hey, my name is $name")
```
This will print `Hey, my name is Bruce`

## Control Flow
### If statements
Nothing fancy here `if` statements work pretty much in the way you would expect.
```kotlin
if (some condition) {
    ...
} else if (some other condition) {
    ...
} else {
    ...
}
```
### When statements
`when` statements act in the same way that `switch` statements act in a language like Java. (Un)fortunately they are not as powerful as Scala's pattern matching. Here is the syntax:
```kotlin
when(variable) {
    some_value -> println("some value")
    some_other_value -> println("some other value")
    else -> println("Base case")
}
```
For example the following program prints "forty two".
```kotlin
val answer: Int = 42
when(answer) {
    40 -> println("forty")
    42 -> println("forty two")
    else -> println("some useless number")
}
```
### Value assignmnet with control-flow statements
In Kotlin you can easily assign a value to a new variable based on a condition:
```kotlin
val name: String = if(condition) "Superman" else "Batman"
```
We can achieve a similar effect with `when` statements:
```kotlin
val name: String = when(name) {
    "Bruce Wayne" -> "Batman"
    "Clark Kent" -> "Superman"
    else -> "Not super"
}
```
### The Elvis Operator
In programming it commonly occurs that you want to check wheter a certain variable is `null` and if it is not make another varaible equal to it. Usually you would it kind of like this:
```kotlin
val whatToPrint = if (myVariable == null) "I am not printing..." else myVariable
```
But Kotlin makes this even easier using the [Elvis operator](https://en.wikipedia.org/wiki/Elvis_operator). With this trick the above program becomes:
```kotlin
val whatToprint = myVariable ?: "I am not printing"
```

## Collections & Iteration
We can create arrays and lists in the following way:
```kotlin
val myList = listOf(1,2,3,5,7)
val myArray = arrayOf(1,2,3,5,7)
```
You can access the above collections in this way:
```kotlin
myList[0] //1
myArray[4] //7
```
And you can get the size like this:
```kotlin
myList.size //5
myArray.size //5
```
### Looping 
Kotlin does have the traditional Java `for` loop, however it offers some intresting alternatives.

**Enhanced for loop**
```kotlin
for (item in list) {
    println(item)
}
```

**Higher-order "loops"**
Kotlin also has support for going through all the elements of a list using higher-order function (similar to the way scala does it). 
```kotlin
list.forEach {println(it)} \\prints all the elements 
list.forEach {i -> println(i)} \\again prints all the elements without relying on `it`
```
Note that in the above examples Kotlin has `it` bound as the default element name.

**Keeping track of the index**
If you need access to the index you can use
```kotlin
list.forEachIndexed {index, it -> println("element $it at index $index")}
```


### Maps
We can easily create a map
```kotlin
val myMap = mapOf(1 to "a", 2 to "b", 3 to "c")
```
The `to` operator creates a key-value `Pair` object. In this case the integers are the keys and the letters are the values.

### Mutable Collections
By default all collections in Kotlin are **immutable**. If you want to create a mutable collection you can simple add `mutable` in front of the previously mentioned methods.
```kotlin
val myMutableList = mutableListOf(1,2,3,5)
val myMutablArray = mutableArrayOf(1,2,3,5)
```



## Functions
Kotlin supports both normal function and high-order functions.

### A Simple function
```kotlin
fun foo(number: Int): Int {
    return number + 1
}
```
Which you can later call in the way you would expect:
```kotlin
foo(41)
```

### Single Expression functions
Similar to the way you can use `def` in Scala, in Kotlin you can declare function in a single expression:
```kotlin
fun foo() = "Hello World!"
```

### Named & Default Parameters
Kotlin allows you to set the parameters of a function by referring to their name, as well as setting default values for them. Concretely if we have the following definition:
```kotlin
fun foo(a: Int = 40, b: Int = 2): Int {
    return a + b
}
```
Then we can call this method in the following ways
```kotlin
foo(32, 46) //returns 78
foo() //returns 2
foo(b = 1) //returns 41
foo(b = 1, a = 2) //returns 3
```

### High-Order Functions
In Kotlin we can have functions take functions as parameters:
```kotlin
fun foo(a : Int, f: (Int) -> Int): Int {
    return f(a)
}
```
Kotlin also has a feature called **lambda syntax**. If the last parameter of a function is another function then on invocation we can use the following syntax (using `foo` from above): 
```kotlin
foo(40) {
    it + 2
}
```
Kotlin has `it` bound as the default parameter name. We can also override it in the following name: 
```kotlin
foo(40) { paramName ->
    paramName + 2
}
```

### Varargs
Function in Kotlin can have a variable number of arguments. Consider the following example
```kotlin
fun foo(param: Int, vararg otherParam: Int) {
    otherParam.foreach { println(it + param) }
}
```
Here `otherParam` is a `vararg` and can be used like a list whithin the function. What changes is the way we call the function when a `vararg` is involved. 
```kotlin
foo(40, 1, 2) // prints 41 and 42
foo(40, 1) // prints 41
foo(40) //does not print anything
```
If we already have a list of numbers and we want to pass all them in `foo` we can use the spread operator (`*`):
```kotlin
var list = listOf(1,2,3,4,5)
foo(40, *list)
```

## Classes 
### Creating and Using Classes
There are 3 basic ways to declare a class in Kotlin.
The first one initialises the fields via an `init` block. The `init` block is a part of code that gets executed first everytime an instance of the class is created. If there are multiple `init` blocks then they are all executed in the order the appear in the code. 
```kotlin
class Person(_firstname: String, _lastName: String) {
    val firstName: String
    val lastName: String
    
    init {
        firstName = _firstName
        lastName = _lastName
    }
}
```
The second way is a little more direct:
```kotlin
class Person(_firstname: String, _lastName: String) {
    val firstName: String = _firstName
    val lastName: String = _lastName
}
```
The third way is the most direct:
```kotlin
class Person(val firstName: String, val lastName: String) {
    //functions go here
    ...
}
```
`Person(...)` basically acts as the constructor definition here. It is important to note the in Kotlin getters and setters are automatically generated for us. So if you want to use the `Person` class you can do so in the following way: 
```kotlin
fun main() {
    val person: Person = Person("Bruce", "Wayne")
    println(person.firstName) //prints "Bruce"
}
```
Because we used `val` in the class definition we **cannot** reassign fields in this case. Had we used `var` we could do the following:
```kotlin
class Person(_firstName: String, _lastName: String) {
    var firstName: String = _firstName
    var lastName: String = _lastName
}

fun main() {
    val person: Person = Person("Bruce", "Wayne")
    person.firstname = "Damian"
    println(person.firstName) //prints "Damian"
}
```
At first this looks like it breaks the **Encapsulation Principle** and indeed it does since all fields in Kotlin are `public` by default. However in the next section we will see how we can overcome this.
### Access level modifiers
Kotlin has 4 access level modifiers
* `public`: Works like `public` in Java. Everything is `public` by default.
* `internal`: Classes, fields and methods with this access level modifier are `public` within the **same module**.
* `private`: Classes, fields and methods with this access level modifier are only available within the **same file**.
* `protected`: Classes, fields and methods with this access level modifier are only available within the **same class** and **subclasses**.

The above modifiers work for classes, fields and methods. Here is an example:
```kotlin
//Person.kt
class Person(_firstName: String, _lastName: String) {
    private var firstName: String = _firstName
    private var lastName: String = _lastName 
    
    public fun getFullName(): String {
        return "$firstName $lastName"
    }
}

//Main.kt
fun main() {
    val person: Person = Person("Bruce", "Wayne")
    println(person.getFullName()) //prints "Bruce Wayne"
}
```

### Custom Getters and Setters 
Even though Kotlin automatically generates getters and setters (with the `.` syntax) we can define our own. We are going to add setter that prints the previous value of the `firstName` field of our `Person` class. We will also give a getter that prints the value.
```kotlin
class Person(_firstName: String, _lastName: String) {
    var firstName: String = _firstName
        set(value) {
            println(field)
            field = value
        }
        get() {
            println(field)
            return field
        }
    val lastName: String = _lastName
}
```
There are a couple interesting things in the above example. First of all notice that we do not need to define the type of the parameter of the setter. Kotlin's compiler can infer it automatically. Also notice that we introduced a `field` variable out of nowhere. Kotlin automatically has `field` bound to the variable the setter/getter is about. It is equivalent to something like `self.fieldName` in Java.

There is a small caveat in this example. The getter and setter must have the same visibility level. Meaning that the following program does not compile: 
```kotlin
class Person(_firstName: String, _lastName: String) {
    private var firstName: String = _firstName
        public set(value) {
            println(field)
            field = value
        }
        public get() {
            println(field)
            return field
        }
    val lastName: String = _lastName
}
```
To circumvent this restriction we can use an approach similar to how getters and setters are defined in Java: 
```kotlin
class Person(_firstName: String, _lastName: String) {
    private var firstName: String = _firstName
    private val lastName: String = _lastName
    
    public fun getFirstName(): String {
        println(firstName)
        return this.firstName
    }
    
    public fun setFirstName(name: String) {
        println(this.firstName)
        this.firstName = name
    }
}
```

### Secondary Constuctors
It might come up that we want a class to have multiple constructors. We can do this in the following way
```kotlin
class Person(val firstName: String, val lastName: String) {
    constructor(): this("Bruce", "Wayne")
    constructor(firstName: String, lastName: String, degree: Int): this(firstName, "$lastName the $degree")
}
```

### Default Values
Similar to how we can set default values for our function parameters we can do the same for Class fields (at the end of the day the constructor is just a function). 
```kotlin
class Person(val firstName: String = "Bruce", val lastName: String = "Wayne")  {
    constructor(firstName: String, lastName: String, degree: Int): this(firstName, "$lastName the $degree")
}
```

## Inheritance
In Kotlin classes are closed by default, meaning that they cannot be extended. We can easily make a class extensible by using the `open` keyword in its definition. Similarly we have to usen `open` to declare which methods can be overriden. 
```kotlin
//Inheritance.kt
open class Parent(_name: String) {
    val name: String = _name

    open fun getInfo(): String {
        return "Name: $name"
    }
}

class Child(_name: String, _age: Int) : Parent(_name) {
    val age: Int = _age

    override fun getInfo(): String {
        val nameToPrint: String = super.getInfo()
        return  "$nameToPrint - Age: $age"
    }
}

//Main.kt
fun main() {
    val kid: Child = Child("Tim", 8)
    println(kid.getInfo()) //prints "Name: Tim - Age: 8"
}
```

## Object Expressions
In Kotlin we can quickly create instances of an anonymous class. 
```kotlin
val obj = object: ParentClass {
    //override ParentClass functionality here.
    ...
}
```
This can be useful for example for quickly creating comparators.

## Interfaces
Kotlin offers support for interfaces:
```kotlin
interface MyInterface {
    fun methodDefinition(arg: Int) : String
}

class MyImplementation: MyInterface {
    override fun methodDefinition(arg: Int): String {
        return "$arg"
    }
}
```

## Type Checking and Casting
### Type Checking
We can use the `is` operator to check whether an object is an instance of a given class:
```kotlin
if(myObj is ClassA) {
    println("$myObj belongs to ClassA")
} 
```
We can negate `is` using `!`:
```kotlin
if(myObj !is ClassA) {
    println("$myObj does not belong to ClassA")
} 
```
### Type Casting
We can cast an object of one type to another in the following way:
```kotlin
(objectOfClassA as ClassB).methodOfClassB()
```
### Smart casts
If we have already checked that an object belongs to a certain class then the Kotlin compiler can do the casting for us:
```kotlin
if (objectOfClassA is ClassB) {
    objectOfClassA.methodOfClassB() //this now works without casting
}
```

## Companion Objects
A companion object is an object scoped to the instance of another class. It can be used to create effects similar to those of `static` in Java.
```kotlin
class MyClass private constructor(val name: String) {
    companion object {
        fun create() = MyClass("name")
    }
}

fun main() {
    val class = MyClass.Companion.create()
}
```
Notice how the companion object has access to the private constructor.

## Object Declarations
In Kotlin we can use object declarations to create **thread-safe singletons**:
```kotlin
object SingletonObj {
    fun foo(...) {
        ...
    }
}

fun main() {
    SingletonObj.foo(...)
}
```

## Enums
In Kotlin we can also use Enums
```kotlin
enum class Difficulty {
    EASY, MEDIUM, HARD
}
```
Enums can nicely be combined with `when` expressions:
```kotlin
val name = when(difficult) {
    Difficulty.EASY -> "easy difficulty"
    Difficulty.MEDIUM -> "medium difficulty"
    Difficulty.HARD -> "hard difficulty"
}
```

## Sealed Classes
Sealed classes allow us to define restricted class hierarchies.
```kotlin
sealed class Difficulty() {
    class Easy(val points: Int)
    class Medium(val points: Int)
    class Hard(val points: Int)
}
```

## Data Classes
Data classes are Kotlin's way to provide immutable data types. For each data class Kotlin automatically generates `equals()`, `hashcode()` and `toString()` methods that use all the fields of the type. 
We can easily create a data class:
```kotlin
data class Node(val left: Node, val right: Node)
```
Kotlin also automatically generates a `copy` constructor with named parameters.
```kotlin
val copiedNode = originalNode.copy(right = someNode)
```
Of course we can also override the automatically generated methods:
```kotlin
data class Node(val left: Node, val right: Node) {
    override fun equals(other: Any?): Boolean {
        ...
    }
}
```

## Extension Function and Properties
Kotlin allows you to add functions and properties to a class outside of its definition. Concretely:
```kotlin
sealed class Difficulty() {
    data class Easy(val points: Int)
    data class Medium(val points: Int)
    data class Hard(val points: Int)
}

//Adds a function to Easy
fun Difficulty.Easy.printIfo() {
    println("${this.points}")
}

//Adds a property to Hard
val Difficulty.Hard.timeLimit: Int
    get() = 10
```

## Acknowledgements
This short guide was created based on [this tutorial](https://www.youtube.com/watch?v=F9UC9DY-vIU&t=2312s) by Nate Ebel.