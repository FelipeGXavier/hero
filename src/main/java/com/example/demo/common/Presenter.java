package com.example.demo.common;

public interface Presenter<I, O> {

    O handle(I input);
}
