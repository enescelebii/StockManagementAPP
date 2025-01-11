package StockManagement.app.Service;

import StockManagement.app.Exception.GlobalExceptionHandler;
import StockManagement.app.Model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

public interface StockService {
    public Stock addStock(Stock stock);

    public Stock updateStock(int id, Stock updatedStock);

    public Stock getStockById(int id);

    public Page<Stock> getAllStock(Pageable pageable);

    public Stock getStockByName(String stockName);

    public boolean deleteStock(int id);

    public List<Stock> getStocksByStatus(String status);

    public List<Stock> getStocksByCategory(String category);

    public Stock getStockByStockCode(String stockCode);

    public String saveStockImage(int id, MultipartFile file) throws IOException;

    public List<Stock> getAllStockList();

    public Stock sellStock(String stockCode, int quantity);

    public String generateStockCode();
}
