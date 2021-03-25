package com.example.demo.walking.domain.valueobject;

import org.springframework.util.Assert;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Email {

    private final String value;
    private final String EMAIL_REGEX_PATTERN = "^(.+)@(.+)$";
    private final Pattern pattern = Pattern.compile(EMAIL_REGEX_PATTERN);

    public Email(String value) {
        Assert.notNull(value, "Invalid email address");
        Assert.isTrue(this.validate(value), "Invalid email address");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private boolean validate(String value) {
        var matcher = this.pattern.matcher(value);
        return matcher.matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

}
