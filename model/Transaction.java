package com.VOLTSBankApp.model;


    import java.time.LocalDateTime;

    public class Transaction {
        private int transactionId;
        private String accountNumber;
        private String transactionType;
        private double amount;
        private double balanceAfter;
        private LocalDateTime transactionDate;
        private String description;

        public Transaction() {
            this.transactionDate = LocalDateTime.now();
        }

        public Transaction(String accountNumber, String transactionType, double amount,
                           double balanceAfter, String description) {
            this.accountNumber = accountNumber;
            this.transactionType = transactionType;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
            this.description = description;
            this.transactionDate = LocalDateTime.now();
        }

        // Getters and Setters
        public int getTransactionId() { return transactionId; }
        public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

        public String getAccountNumber() { return accountNumber; }
        public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

        public String getTransactionType() { return transactionType; }
        public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }

        public double getBalanceAfter() { return balanceAfter; }
        public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }

        public LocalDateTime getTransactionDate() { return transactionDate; }
        public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        @Override
        public String toString() {
            return "Transaction{" +
                    "id=" + transactionId +
                    ", type='" + transactionType + '\'' +
                    ", amount=" + String.format("%.2f", amount) +
                    ", date=" + transactionDate +
                    '}';
        }
    }

