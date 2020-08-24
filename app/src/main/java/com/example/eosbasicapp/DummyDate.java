package com.example.eosbasicapp;

import java.util.ArrayList;

public class DummyDate {

    public static ArrayList<Contact> contacts = new ArrayList<>();

    static {
        contacts.add(new Contact("곽용우","010-3744-0644","kkalbuyw@gmail.com"));
        contacts.add(new Contact("윤무원","010-5510-3499","sample@gmail.com"));
        contacts.add(new Contact("송재용","010-1234-5678","asdf1234@gmail.com"));
    }
}
