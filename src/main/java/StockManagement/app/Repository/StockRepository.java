package StockManagement.app.Repository;

import StockManagement.app.Model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    Stock findByStockName(String name);
    Stock findByStockCode(String stockCode);
    List<Stock> findByStockStatus(String stockStatus);
    List<Stock> findByStockCategory(String stockCategory);
    Page<Stock> findByStockNameContainingIgnoreCase(String stockName, Pageable pageable);
}
