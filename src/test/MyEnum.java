package test;

public enum MyEnum {
	
	NumberZero(0),
    NumberOne(1),
    NumberTwo(2),
    NumberThree(3);

    private final int value;

    MyEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
