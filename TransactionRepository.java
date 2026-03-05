import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    private ArrayList<TransactionRecord> records = new ArrayList<>();

    public void addRecord(TransactionRecord record) {
        if (record != null) {
            records.add(record);
        }
    }

    public List<TransactionRecord> getAll() {
        return new ArrayList<>(records);
    }

    public List<TransactionRecord> getByAccount(String accountNumber) {
        ArrayList<TransactionRecord> result = new ArrayList<>();

        for (TransactionRecord record : records) {
            if (record.involvesAccount(accountNumber)) {
                result.add(record);
            }
        }

        return result;
    }
}
