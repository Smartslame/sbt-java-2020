package ru.sbt.smartslame.reflection.hw;

import java.util.List;

class Person {
    private final String firstName;
    private final String lastName;
    private final Address address;
    private final List<String> phoneNumbers;
    private final int[] hh;

    public Person(String firstName, String lastName, Address address, List<String> phoneNumbers, int[] hh) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumbers = phoneNumbers;
        this.hh = hh;
    }
}
