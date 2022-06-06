package StorageManagementSystem.records;

import java.sql.Timestamp;

public record ComplaintRepr(int id_complaint, int id_product, String name, Timestamp complaint_date, double quantity, String description) {
}
