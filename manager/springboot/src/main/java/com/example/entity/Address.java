package com.example.entity;

import java.io.Serializable;
import java.util.Objects;


public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    private Integer userId;
    private String username;
    private String useraddress;
    private String phone;

    public Address() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id) &&
                Objects.equals(userId, address.userId) &&
                Objects.equals(useraddress, address.useraddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, useraddress);
    }
    public Address(Integer id, Integer userId, String username, String useraddress, String phone) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.useraddress = useraddress;
        this.phone = phone;
    }
}