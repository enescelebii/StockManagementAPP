package StockManagement.app.DTO;

import StockManagement.app.Model.Enums.StockStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Getter, Setter, toString, equals, hashCode metodlarını otomatik olarak oluşturur
@NoArgsConstructor // Parametresiz constructor oluşturur
@AllArgsConstructor // Parametreli constructor oluşturur
public class StockDTO {
    private int id;
    private String stockCode;
    private String stockName;
    private Double stockPrice;
    private int stockQuantity;
    private StockStatusEnum stockStatus;
    private String stockCategory;
    private String stockDescription;
    private String stockImagePath;
}
