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

    public void methodWithBrokenThrows() {
        throw 42;
        throw new ;
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


    public String commentOnAge() {
        switch (this.age) {
            case 10: return "Oh the joys of youth!";
            case 20: return "Oh the joys of youth!";
            case 30: return "Time to get serious...";
            case 40: return "You are not old. You are wise";
            default: "Hmmm?"
        }
    }

    public String commentOnAgeEnhanched() {
        switch (this.age) {
            case 10, 20 : return "Oh the joys of youth!";
            case 30 : return "Time to get serious...";
            case 40 : return "You are not old. You are wise";
        }
    }

    public String commentOnAgeEnhanchedWithRules() {
        switch (this.age) {
            case 10, 20 -> return "Oh the joys of youth!";
            case 30 -> return "Time to get serious...";
            case 40 -> return "You are not old. You are wise";
        }
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

}


