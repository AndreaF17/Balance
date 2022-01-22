package com.classes;

public class Item {
    private TypeOfPayment type;
    private Float value;

    public Item(TypeOfPayment type, Float value) {
        this.type = type;
        this.value = value;
    }

    public TypeOfPayment getType() {
        return type;
    }
    public void setType(TypeOfPayment type) {
        this.type = type;
    }
    public Float getValue() {
        return value;
    }
    public void setValue(Float value) {
        this.value = value;
    }
    
}
