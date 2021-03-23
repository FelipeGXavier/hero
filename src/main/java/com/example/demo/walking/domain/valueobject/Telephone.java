package com.example.demo.walking.domain.valueobject;

import org.springframework.util.Assert;

public final class Telephone {

    private String value;
    private int MAX_LENGTH_TELEPHONE = 11;

    public Telephone(String value) {
        Assert.isTrue(this.validate(), "Invalid telephone number");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private boolean validate() {
        return true;
    }
}
