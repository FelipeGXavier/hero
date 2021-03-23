package com.example.demo.walking.domain.valueobject;

import org.springframework.util.Assert;

public final class Email {

    private final String value;
    private final String EMAIL_REGEX_PATTERN = "";

    public Email(String value) {
        Assert.isTrue(this.validate(), "Invalid email address");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private boolean validate() {
        return true;
    }
}
