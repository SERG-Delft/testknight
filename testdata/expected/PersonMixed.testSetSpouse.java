package test;

public class Person {

    private String name;
    private Person spouse;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    //Throws an Exception as a side-effect.
    public Person getSpouse() throws NotMarriedException {
        if (spouse == null) {
            throw new NotMarriedException();
        }
        return spouse;
    }


    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    public int getAge() {
        return age;
    }

    //Affects a class field as a side-effect.
    public void setAge(int age) throws AgeException {
        if (age <= 0) {
            throw new CannotBeThatYoungException();
        }
        if (age > 100) {
            throw new CannotBeThatOldException();
        }
        this.age = age;
    }

    //No side effect -> should assert on return value.
    public String getName() {
        return name;
    }

    //Affects a class field as a side-effect.
    public void setName(String name) {
        this.name = name;
    }

    //Affects a class field as a side-effect.
    public void setFullName(String firstName, String lastName) {
        String res = firstName + " " + lastName;
        name = res;
    }

    //No side effect -> should assert on return value.
    public int getYearBorn(int currentYear) {
        return currentYear - this.age;
    }

    //Throws an exception as a side-effect.
    public void save() throws IOException {
        EntityManager.save(this);
    }

    //No side effect but access class fields.
    public String greet() {
        String message = "Hello! My name is " + name + " and I am " + this.age + " years old";
        return message;
    }

    @Test
    public void testSetSpouse() {
        Person p1 = new Person("Bob", 25);
        Person p2 = new Person("Alice", 28);
        p1.setSpouse(p2);
        assertEquals(p1.spouse, p2);

        /**
         * Assert that attribute spouse is re-assigned properly.
         */

    }
}


