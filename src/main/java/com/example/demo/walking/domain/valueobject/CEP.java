package com.example.demo.walking.domain.valueobject;

import org.springframework.util.Assert;

import java.util.Objects;

public final class CEP {

    private final String value;
    private final int CEP_LENGTH = 8;

    public CEP(String value) {
        Assert.notNull(value, "CEP can't be null");
        Assert.isTrue(
                value.length() == CEP_LENGTH, "CEP must have eight characters without slashes");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CEP cep = (CEP) o;
        return Objects.equals(value, cep.value);
    }
}
