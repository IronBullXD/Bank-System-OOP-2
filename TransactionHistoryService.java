import java.time.LocalDate;
import java.util.List;

public class TransactionHistoryService {

    private TransactionRepository repository;
    private TransactionFilter filter;

    public TransactionHistoryService(TransactionRepository repository, TransactionFilter filter) {
        this.repository = repository;
        this.filter = filter;
    }

    public void logDeposit(String accountNumber, double amount) {
        repository.addRecord(new TransactionRecord("Deposit", accountNumber, amount));
    }

    public void logWithdraw(String accountNumber, double amount) {
        repository.addRecord(new TransactionRecord("Withdraw", accountNumber, amount));
    }

    public void logTransfer(String fromAccountNumber, String toAccountNumber, double amount) {
        repository.addRecord(new TransactionRecord("Transfer", fromAccountNumber, toAccountNumber, amount));
    }

    public void showAllTransactions() {
        printRecords(repository.getAll(), "No transactions found.");
    }

    public void showTransactionsByAccount(String accountNumber) {
        printRecords(repository.getByAccount(accountNumber), "No transactions found for account: " + accountNumber);
    }

    public void showTransactionsByType(String type) {
        List<TransactionRecord> filtered = filter.filterByType(repository.getAll(), type);
        printRecords(filtered, "No transactions found for type: " + type);
    }

    public void showTransactionsByDateRange(LocalDate from, LocalDate to) {
        List<TransactionRecord> filtered = filter.filterByDateRange(repository.getAll(), from, to);
        printRecords(filtered, "No transactions found from " + from + " to " + to);
    }

    public void showTransactionsByMinAmount(double minAmount) {
        List<TransactionRecord> filtered = filter.filterByMinAmount(repository.getAll(), minAmount);
        printRecords(filtered, "No transactions found with amount >= " + minAmount);
    }

    private void printRecords(List<TransactionRecord> records, String emptyMessage) {
        if (records.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }

        for (TransactionRecord record : records) {
            record.display();
        }
    }
}
