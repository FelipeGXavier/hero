package com.example.demo.walking.infra.mappers;

import com.example.demo.walking.domain.valueobject.Email;

import javax.persistence.AttributeConverter;

public class EmailConverter implements AttributeConverter<Email, String> {
    @Override
    public String convertToDatabaseColumn(Email email) {
        return email == null ? null : email.getValue();
    }

    @Override
    public Email convertToEntityAttribute(String email) {
        return new Email(email);
    }
}
