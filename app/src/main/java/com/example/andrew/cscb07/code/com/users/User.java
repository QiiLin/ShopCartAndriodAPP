package com.example.andrew.cscb07.code.com.users;

import android.content.Context;

import com.example.andrew.cscb07.code.database.DatabaseSelectHelpers;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.security.PasswordHelpers;

/**
 * Created by LTJ on 2017/11/28.
 */



public abstract class User {
    private int id;
    private String name;
    private int age;
    private String address;
    private int roleId;
    private boolean authenticated;

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getRoleId() {
        return roleId;
    }

    /**
     * This authenticate method will take inputed password and compare it to the password if this user
     * from database.
     *
     * @param password A String that user inputted
     * @return true if the password are right
     * @throws InvalidIdException throw if userId is not in the database
     * @throws InvalidIdException throw if the userRole is not in the database
     */
    public final boolean authenticate(String password, Context context)
            throws InvalidIdException {
        String usersPassWord = DatabaseSelectHelpers.getPassword(this.getId(), context);
        return PasswordHelpers.comparePassword(usersPassWord, password);
    }


}

