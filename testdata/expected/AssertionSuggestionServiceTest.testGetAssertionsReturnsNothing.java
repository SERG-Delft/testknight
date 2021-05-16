public class MixedTestAndNoTestMethods {

    @Test
    public void test() {
        getAnInt();
        doNothing();
    }

    public int getAnInt() {
        return 1;
    }

    public void doNothing() {
        return;
    }

}