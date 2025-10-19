package com.VOLTSBankApp.model;

import com.VOLTSBankApp.interfaces.InterestBearing;
import com.VOLTSBankApp.interfaces.Withdrawable;

public class InvestmentAccount extends Account implements InterestBearing, Withdrawable {
    private static final double INTEREST_RATE = 0.05; // 5% monthly
    private static final double MINIMUM_OPENING_BALANCE = 500.00;
    private double accruedInterest;

    public InvestmentAccount() {
        super();
        setAccountType(AccountType.INVESTMENT);
    }

    public InvestmentAccount(String accountNumber, String branch, Customer customer, double initialDeposit) {
        super(accountNumber, branch, customer, AccountType.INVESTMENT);
        this.accruedInterest = 0.0;
        if (initialDeposit >= MINIMUM_OPENING_BALANCE) {
            deposit(initialDeposit);
        }
    }

    @Override
    public void calculateInterest() {
        double interest = getBalance() * INTEREST_RATE;
        this.accruedInterest += interest;
        setBalance(getBalance() + interest);
    }

    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > getBalance()) {
            return false;
        }
        setBalance(getBalance() - amount);
        return true;
    }

    @Override
    public double getAvailableBalance() {
        return getBalance();
    }

    public static double getMinimumOpeningBalance() {
        return MINIMUM_OPENING_BALANCE;
    }

    public double getAccruedInterest() {
        return accruedInterest;
    }

    @Override
    public String getAccountDetails() {
        return String.format("Investment Account\nAccount Number: %s\nBalance: BWP %.2f\n" +
                        "Interest Rate: %.1f%%\nAccrued Interest: BWP %.2f\nWithdrawals: Allowed",
                getAccountNumber(), getBalance(), getInterestRate() * 100, accruedInterest);
    }
}
