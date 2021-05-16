public class MixedTestAndNoTestMethods {

    @Test
    public void test() {
        getAnInt();
        doNothing();
        /**
         * Assert that "getAnInt" returns the proper "int".
         */}

    public int getAnInt() {
        return 1;
    }

    public void doNothing() {
        return;
    }

}