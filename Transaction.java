public interface Transaction {
    void deposit(Account account, double amount);

    void withdraw(Account account, double amount);

    void transfer(Account fromAccount, Account toAccount, double amount);

}
