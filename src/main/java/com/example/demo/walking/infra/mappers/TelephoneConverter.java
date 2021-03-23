package com.example.demo.walking.infra.mappers;

import com.example.demo.walking.domain.valueobject.Telephone;

import javax.persistence.AttributeConverter;

public class TelephoneConverter implements AttributeConverter<Telephone, String> {

    @Override
    public String convertToDatabaseColumn(Telephone telephone) {
        return telephone == null ? null : telephone.getValue();
    }

    @Override
    public Telephone convertToEntityAttribute(String number) {
        return new Telephone(number);
    }
}
