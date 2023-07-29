package com.example.medicinereminder;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Medicine implements Serializable { // Implement Serializable
    @PrimaryKey(autoGenerate = true)
    private long id; // Change to long data type
    private String name;
    private long dueDate;

    // Add the getters and setters for the fields

    public long getId() { // Change the return type to long
        return id;
    }

    public void setId(long id) { // Change the parameter type to long
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDueDate() { // changed to return Long
        return dueDate;
    }

    public void setDueDate(Long dueDate) { // changed to accept Long
        this.dueDate = dueDate;
    }
}
