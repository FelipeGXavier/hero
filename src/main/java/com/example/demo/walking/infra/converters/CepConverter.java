package com.example.demo.walking.infra.converters;

import com.example.demo.walking.domain.valueobject.CEP;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CepConverter implements AttributeConverter<CEP, String> {

    @Override
    public String convertToDatabaseColumn(CEP cep) {
        return cep == null ? null : cep.getValue();
    }

    @Override
    public CEP convertToEntityAttribute(String cep) {
        return new CEP(cep);
    }
}
