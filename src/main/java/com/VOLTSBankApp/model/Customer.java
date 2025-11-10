package com.VOLTSBankApp.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int customerId;
    private String firstName;
    private String surname;
    private String address;
    private String phoneNumber;
    private String email;
    private String password;
    private List<Account> accounts;

    public Customer() {
        this.accounts = new ArrayList<>();
    }

    public Customer(String firstName, String surname, String address,
                    String phoneNumber, String email, String password) {
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

    // Getters and Setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getFullName() { return firstName + " " + surname; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Account> getAccounts() { return accounts; }
    public void setAccounts(List<Account> accounts) { this.accounts = accounts; }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public boolean removeAccount(Account account) {
        return this.accounts.remove(account);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", numberOfAccounts=" + accounts.size() +
                '}';
    }
}