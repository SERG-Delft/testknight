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

    public String getSpouseName() {
        try {
            Person spouce = this.getSpouse();
            return spouce.name;
        } catch (NotMarriedException e) {
            return "I am not married actually";
        }
    }

    public String getSpouseNameNoCatch() {
        try {
            Person spouce = this.getSpouse();
            return spouce.name;
        } finally {
            return "For the last time, I am not married!";
        }
    }


    public String getSpouseNameMultipleCatches() {
        try {
            Person spouce = this.getSpouse();
            return spouce.name;
        } catch (NotMarriedException e) {
            return "I am not married actually!";
        } catch (NullPointerException e) {
            return "I don't have a spouce!"
        }
    }

    public String getSpouseNameCatchAndFinally() {
        try {
            Person spouce = this.getSpouse();
            return spouce.name;
        } catch (NotMarriedException e) {
            return "I am not married actually!";
        } finally {
            System.out.println("Thanks for asking!")
        }
    }

    public String getSpouseNameIncompleteCatch() {
        try {
            Person spouce = this.getSpouse();
            return spouce.name;
        } catch (NotMarriedException e) {
            return "I am not married actually!";
        } catch ( ) {
            return "I don't have a spouce!"
        }
    }

    public void methodWithBrokenThrows() {
        throw 42;
        throw new ;
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

    public String commentOnAgeWithIfs() {
        if (this.age <= 20) {
            return "Oh the joys of youth!";
        } else if (this.age <= 30) {
            return "Time to get serious...";
        } else {
            return "You are not old. You are wise";
        }
    }

    public void countToTen() {
        int counter = 0;
        while (counter < 11) {
            System.out.println(counter);
            counter++;
        }
    }

    public void spellName() {
        for(int i = 0; i < this.name.lenght(); i++) {
            System.out.println(this.name.charAt(i));
        }
    }

    public void spellWithForEach() {
        for(char a : this.name) {
            System.out.println(a);
        }
    }

    public void doWhileExample() {
        int condition = false;
        do {
            condition = true;
        }
        while (!condition);
    }

    public Person getSpouseWithTernary() throws NotMarriedException {
        (spouse == null) ? throw new NotMarriedException() : return spouse
    }

    //Using Conditional Expression (Ternary Operator)
    public void setAgeConditional(int age) throws AgeException {
        age <=0 ? throw new CannotBeThatYoungException() : this.age = age;
        age > 100? throw new CannotBeThatOldException() : this.age = age;
    }




}


