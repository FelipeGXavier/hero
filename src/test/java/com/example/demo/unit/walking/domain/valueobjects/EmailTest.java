package com.example.demo.unit.walking.domain.valueobjects;

import com.example.demo.walking.domain.valueobject.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {"user.nameteste.com", "user.name#teste.com", "@teste.com"})
    @DisplayName("Test invalid email addresses")
    public void testInvalidAddresses(String address) {
        var ex = assertThrows(IllegalArgumentException.class, () -> new Email(address));
        assertEquals("Invalid email address", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"user@user.com", "user.name@teste.com", "u@u.com"})
    @DisplayName("Test valid email addresses")
    public void testValidAddresses(String address) {
        var email = new Email(address);
        assertEquals(email.getValue(), address);
    }

    @DisplayName("Two CEP objects with same value should be equals")
    @Test
    public void testEmailEquals() {
        var val = "u@u.com.br";
        var email1 = new Email(val);
        var email2 = new Email(val);
        assertEquals(email1, email2);
        assertEquals(email1.getValue(), val);
        assertEquals(email2.getValue(), val);
    }

    @DisplayName("Two CEP objects with same value should be equals")
    @Test
    public void testEmailNotEquals() {
        var email1 = new Email("u@u.com.br");
        var email2 = new Email("a@a.com.br");
        assertNotEquals(email1, email2);
    }

    @Test
    @DisplayName("Null email should throw and exception")
    public void testNullAddress() {
        var ex = assertThrows(IllegalArgumentException.class, () -> new Email(null));
        assertEquals("Invalid email address", ex.getMessage());
    }

}
