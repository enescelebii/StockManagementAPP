package StockManagement.app.Service;

import StockManagement.app.Model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

public interface StockService {
    Stock addStock(Stock stock);

     Stock updateStock(int id, Stock updatedStock);

     Stock getStockById(int id);

     Page<Stock> getAllStock(Pageable pageable);

     List<Stock> getStockByName(String stockName);

     boolean deleteStock(int id);

     List<Stock> getStocksByStatus(String status);

     List<Stock> getStocksByCategory(String category);

     Stock getStockByStockCode(String stockCode);

     String saveStockImage(int id, MultipartFile file) throws IOException;

     List<Stock> getAllStockList();

     Stock sellStock(String stockCode, int quantity);

     String generateStockCode();
}
