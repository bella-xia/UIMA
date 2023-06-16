package com.uima.joanne.testrepoapp;

import com.google.firebase.database.Exclude;

/**
 * Simple class to represent generic client information.
 */

public class Client {

    private static long nextId = 0;

    public String name;
    public String street;
    public int zip;
    private String id;

    public Client() {
        // for calls to DataSnapshot.getValue(Client.class)
    }

    public Client(String nm, String st, int z) {
        this.name = nm;
        this.street = st;
        this.zip = z;
        this.id = String.format("c-%04d", ++nextId);
    }

    public String toString() {
        return this.name + ": " + this.street + ", " + this.zip;
    }

    public String getId() {
        return this.id;
    }
}
