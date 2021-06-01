//A class containing compilation errors
// for testing incorrect ternary operator usage

class BrokenClass {

    private int a = 24;
    private int b = 42;

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