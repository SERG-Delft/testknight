public class MixedTestAndNoTestMethods {

    @Test
    public void test() {
        getAnInt();
        doNothing();
    }

    @Test
    public void testMethod() {
        String noun = "Testing";
        String adverb = "awesome";
        methodUnderTest(noun, adverb);

        /**
         * Assert that method "append" modifies argument "noun" properly.
         * Assert that method "append" modifies argument "adverb" properly.
         */

    }

    public void methodUnderTest(String a, String b) {
        a.append(" is ", b);
    }

    public int getAnInt() {
        return 1;
    }

    public void doNothing() {
        return;
    }

}