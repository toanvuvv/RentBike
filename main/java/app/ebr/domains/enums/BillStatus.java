package app.ebr.domains.enums;

public enum BillStatus {

    PENDING(0),
    PAID(1);

    private int value;

    BillStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}
