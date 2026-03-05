import java.util.ArrayList;

public class Bank implements Transaction {

    private ArrayList<Account> accounts = new ArrayList<>();
    private ArrayList<TransactionRecord> records = new ArrayList<>();

    public void addAccount(Account account) {
        accounts.add(account);
        System.out.println("Account created successfully.");
    }

    public Account findAccount(String accNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accNumber)) {
                return acc;
            }
        }
        return null;
    }

    @Override
    public void deposit(String accNumber, double amount) {
        Account acc = findAccount(accNumber);
        if (acc != null) {
            acc.deposit(amount);
            records.add(new TransactionRecord("Deposit", accNumber, amount));
        } else {
            System.out.println("Account not found.");
        }
    }

    @Override
    public void withdraw(String accNumber, double amount) {
        Account acc = findAccount(accNumber);
        if (acc != null) {
            acc.withdraw(amount);
            records.add(new TransactionRecord("Withdraw", accNumber, amount));
        } else {
            System.out.println("Account not found.");
        }
    }

    @Override
    public void transfer(String fromAcc, String toAcc, double amount) {
        Account sender = findAccount(fromAcc);
        Account receiver = findAccount(toAcc);

        if (sender != null && receiver != null) {
            sender.withdraw(amount);
            receiver.deposit(amount);
            records.add(new TransactionRecord("Transfer", fromAcc, amount));
            System.out.println("Transfer successful.");
        } else {
            System.out.println("Invalid account number.");
        }
    }

    public void showAllAccounts() {
        for (Account acc : accounts) {
            System.out.println("Account: " + acc.getAccountNumber() +
                    " | Holder: " + acc.getAccountHolder() +
                    " | Balance: " + acc.getBalance());
        }
    }
}