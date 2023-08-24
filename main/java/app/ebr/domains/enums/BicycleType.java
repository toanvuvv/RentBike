
package app.ebr.domains.enums;

public enum BicycleType {

    NORMAL(0),
    ELECTRONIC(1),
    COUPLE(2);

    private final int value;

    BicycleType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}