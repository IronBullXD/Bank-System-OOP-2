import java.util.ArrayList;

public class Bank implements Transaction {

    private ArrayList<Account> accounts = new ArrayList<>();
    private TransactionRepository transactionRepository = new TransactionRepository();
    private TransactionHistoryService historyService =
            new TransactionHistoryService(transactionRepository, new TransactionFilter());

    public boolean addAccount(Account account) {
        if (account == null) {
            System.out.println("Invalid account details.");
            return false;
        }

        if (findAccount(account.getAccountNumber()) != null) {
            System.out.println("Account number already exists.");
            return false;
        }

        accounts.add(account);
        System.out.println("Account created successfully.");
        return true;
    }

    public Account findAccount(String accNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accNumber)) {
                return acc;
            }
        }
        return null;
    }

    public void generateStatement(String accNumber) {
        Account acc = findAccount(accNumber);
        if (acc != null) {
            BankStatement statement = new BankStatement(acc);
            statement.printStatement();
        } else {
            System.out.println("Account not found.");
        }
    }

    @Override
    public void deposit(Account account, double amount) {
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }

        double before = account.getBalance();
        account.deposit(amount);

        if (account.getBalance() > before) {
            historyService.logDeposit(account.getAccountNumber(), amount);
        }
    }

    @Override
    public void withdraw(Account account, double amount) {
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }

        double before = account.getBalance();
        account.withdraw(amount);

        if (account.getBalance() < before) {
            historyService.logWithdraw(account.getAccountNumber(), amount);
        }
    }

    @Override
    public void transfer(Account fromAccount, Account toAccount, double amount) {
        if (fromAccount == null || toAccount == null) {
            System.out.println("Invalid account number.");
            return;
        }

        if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }

        double fromBefore = fromAccount.getBalance();
        fromAccount.withdraw(amount);

        if (fromAccount.getBalance() < fromBefore) {
            toAccount.deposit(amount);
            historyService.logTransfer(fromAccount.getAccountNumber(), toAccount.getAccountNumber(), amount);
            System.out.println("Transfer successful.");
        }
    }

    public void showAllAccounts() {
        for (Account acc : accounts) {
            System.out.println("Account: " + acc.getAccountNumber() +
                    " | Holder: " + acc.getAccountHolder() +
                    " | Balance: " + acc.getBalance());
        }
    }

    public TransactionHistoryService getTransactionHistoryService() {
        return historyService;
    }
}
