package StockManagement.app.Model;

import StockManagement.app.Model.Enums.StockStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Table(name = "Stock")
@Entity
@Data
public class Stock {
    @Id
    @GeneratedValue
    private int id;

    @Column(unique = true)  // stockCode benzersiz olmalı
    private String stockCode;

    @NotBlank(message = "Stock name cannot be blank")
    @Size(min = 1, max = 100, message = "Stock name length should be between 1 and 100")
    private String stockName;

    @Positive(message = "Stock price must be positive")
    private double stockPrice;

    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    private StockStatusEnum stockStatus;

    @Size(max = 100, message = "Category name is too long")
    private String stockCategory;

    @Lob
    private String stockDescription;

    @Column(name = "StockPhotoPath")
    private String stockImagePath; // Fotoğraf verisi yol olarak saklanır


}
