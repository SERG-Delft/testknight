//A class containing compilation errors
//for testing purposes.
class BrokenClass {

    private int a = 24;
    private int b = 42;

    public int incompleteCondition() {
        if () {
            return a + b;
        } else {
            return b;
        }
    }

    public int conditionalWithLiteral() {
        if (true) {
            return a;
        }
        return b;
    }

    public int incompleteConditionalExpression(){
        int a=5;
        int b=10;
        int c = null ? a:b;
        return c;
    }

    public int conditionalExpressionWithLiteral() {
        return true ? a : b;
    }



}