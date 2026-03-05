import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionFilter {

    public List<TransactionRecord> filterByType(List<TransactionRecord> records, String type) {
        ArrayList<TransactionRecord> result = new ArrayList<>();

        for (TransactionRecord record : records) {
            if (record.getType().equalsIgnoreCase(type)) {
                result.add(record);
            }
        }

        return result;
    }

    public List<TransactionRecord> filterByDateRange(List<TransactionRecord> records, LocalDate from, LocalDate to) {
        ArrayList<TransactionRecord> result = new ArrayList<>();

        for (TransactionRecord record : records) {
            LocalDate recordDate = record.getDate().toLocalDate();
            boolean isOnOrAfterFrom = recordDate.isEqual(from) || recordDate.isAfter(from);
            boolean isOnOrBeforeTo = recordDate.isEqual(to) || recordDate.isBefore(to);

            if (isOnOrAfterFrom && isOnOrBeforeTo) {
                result.add(record);
            }
        }

        return result;
    }

    public List<TransactionRecord> filterByMinAmount(List<TransactionRecord> records, double minAmount) {
        ArrayList<TransactionRecord> result = new ArrayList<>();

        for (TransactionRecord record : records) {
            if (record.getAmount() >= minAmount) {
                result.add(record);
            }
        }

        return result;
    }
}
