package StockManagement.app.Repository;

import StockManagement.app.Model.Stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    List<Stock> findByStockName(String name);
    Stock findByStockCode(String stockCode);
    List<Stock> findByStockStatus(String stockStatus);
    List<Stock> findByStockCategory(String stockCategory);
    //Page<Stock> findByStockNameContainingIgnoreCase(String stockName, Pageable pageable);
    List<Stock> findByStockNameContainingIgnoreCase(String stockName);
}
