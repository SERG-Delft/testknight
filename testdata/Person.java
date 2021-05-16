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
        if(spouse == null) {
            throw new NotMarriedException();
        }
        return spouse;
    }

    //Affects the passed argument as a side-effect.
    public void marryTo(Person spouse) {
        this.spouse = spouse;
        spouse.marryTo(this);
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

    public void nameToLowerCase() {
        this.name.toLowerCase();
    }

    public void nameToLowerCaseStaticCall() {
        String.toLowerCase(this.name)
    }

    public void methodCallWithoutThis() {
        name.methodSecond();
    }

    public void methodCallWithThis() {
        this.name.methodSecond();
    }

    public void methodCallOnShadowedField(String name) {
        name.methodSecond(3,2);
    }

    public void methodCallWithThisAndWithout() {
        this.spouse.mysteriousMethod(name)
    }

    public void multipleMethodCall() {
        this.name.toLowerCase();
        this.spouse.mysteriousMethod(name);
    }

    public void reversesList(ArrayList<Person> list) {
        list.reverse();
    }

    public void reversesListWithArgument(ArrayList<Person> list) {
        reverse(list);
    }

    public void addToList(ArrayList<Person> list, Person person) {
        list.add(person);
    }

    public void listEditor(ArrayList<Person> list, Person person) {
        list.add(person);
        reverse(list);
    }

    public void reverseName(String name) {
        name.reverse();
    }

}


