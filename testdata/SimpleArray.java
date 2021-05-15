// a class containing an array field to test if loops generate checklists as expected.
public class SimpleArray{

    private String nameOfArray;
    private int[] arrayOfInts;

    public SimpleArray(String nameOfArray, int[] arrayOfInts) {
        this.nameOfArray = nameOfArray;
        this.arrayOfInts = arrayOfInts;
    }

    public String getNameOfArray() {
        return nameOfArray;
    }

    public void setNameOfArray(String nameOfArray) {
        this.nameOfArray = nameOfArray;
    }

    public int[] getArrayOfInts() {
        return arrayOfInts;
    }

    public void setArrayOfInts(int[] arrayOfInts) {
        this.arrayOfInts = arrayOfInts;
    }

    public int[] incrementByOneFor() {
        int[] newArray = new int[getArrayOfInts().length];
        for(int i = 0; i< getArrayOfInts().length; i++){
            newArray[i] = getArrayOfInts()[i] + 1;
        }
        return newArray;
    }

    public int[] incrementByOneDoWhile() {
        int[] newArray = new int[getArrayOfInts().length];
        int i=0;
        do {
            newArray[i] = getArrayOfInts()[i] + 1;
            i++;
        } while(i< getArrayOfInts().length);
        return newArray;
    }

    public int[] incrementByOneWhile() {
        int[] newArray = new int[getArrayOfInts().length];
        int i=0;
        while(i< getArrayOfInts().length) {
            newArray[i] = getArrayOfInts()[i] + 1;
            i++;
        }
        return newArray;
    }

    public int[] incrementByOneForEach() {
        int[] newArray = new int[getArrayOfInts().length];
        int i=0;
        for( int k : getArrayOfInts()) {
            newArray[i] = k + 1;
            i++;
        }
        return newArray;
    }

    public int[] brokenFor(){
        int[] newArray = new int[getArrayOfInts().length];
        for(int i=0; ; i++){
            newArray[i] = getArrayOfInts()[i] + 1;
        }
        return newArray;
    }

    public int[] brokenDoWhile(){
        int[] newArray = new int[getArrayOfInts().length];
        int i=0;
        do {
            newArray[i] = getArrayOfInts()[i] + 1;
            i++;
        } while();
        return newArray;
    }

    public int[] brokenWhile(){

        int[] newArray = new int[getArrayOfInts().length];
        int i=0;
        while() {
            newArray[i] = getArrayOfInts()[i] + 1;
            i++;
        }
        return newArray;
    }

    public int[] brokenForEach(){

        int[] newArray = new int[getArrayOfInts().length];
        int i=0;
        for( int k : ) {
            newArray[i] = k + 1;
            i++;
        }
        return newArray;
    }

}