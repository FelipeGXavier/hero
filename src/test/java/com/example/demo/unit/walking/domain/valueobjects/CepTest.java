package com.example.demo.unit.walking.domain.valueobjects;

import com.example.demo.walking.domain.valueobject.CEP;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CepTest {

    @DisplayName("Two CEP objects with same value should be equals")
    @Test
    public void testCepEquals() {
        var val = "89160000";
        var cep1 = new CEP(val);
        var cep2 = new CEP(val);
        assertEquals(cep1, cep2);
        assertEquals(cep1.getValue(), val);
        assertEquals(cep2.getValue(), val);
    }

    @DisplayName("Two CEP objects with different values should not be equals")
    @Test
    public void testCepNotEquals() {
        var cep1 = new CEP("89160000");
        var cep2 = new CEP("89160001");
        assertNotEquals(cep1, cep2);
    }

    @DisplayName("CEP without minimum length should throw an exception")
    @Test
    public void testCepWithoutMinLength() {
        var ex = assertThrows(IllegalArgumentException.class, () -> new CEP("8916000"));
        var message = "CEP must have eight characters without slashes";
        assertEquals(ex.getMessage(), message);
    }

    @DisplayName("CEP exceeding eight characters should throw an exception")
    @Test
    public void testCepExceedingLength() {
        var ex = assertThrows(IllegalArgumentException.class, () -> new CEP("891600000"));
        var message = "CEP must have eight characters without slashes";
        assertEquals(ex.getMessage(), message);
    }

    @DisplayName("CEP with null value should throw an exception")
    @Test
    public void testNulLCep() {
        var ex = assertThrows(IllegalArgumentException.class, () -> new CEP(null));
        var message = "CEP can't be null";
        assertEquals(ex.getMessage(), message);
    }

    @DisplayName("CEP with invalid characters should throw and exception")
    @Test
    public void testInvalidCep() {
        var ex = assertThrows(IllegalArgumentException.class, () -> new CEP("1234567A"));
        var message = "CEP must have eight characters without slashes";
        assertEquals(ex.getMessage(), message);
    }
}
