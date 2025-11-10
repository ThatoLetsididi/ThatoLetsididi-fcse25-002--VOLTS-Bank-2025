package com.VOLTSBankApp.interfaces;

public interface Withdrawable {
    boolean withdraw(double amount);
    double getAvailableBalance();
}

