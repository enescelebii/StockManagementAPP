package StockManagement.app.Service;

import StockManagement.app.Model.Stock;
import StockManagement.app.Repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;


    @Value("${StockPhoto.dir}")
    private String uploadDir;

    @Autowired
    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }



    @Override
    public Stock addStock(Stock stock) {
        // StockCode otomatik oluştur
        if (stock.getStockCode() == null || stock.getStockCode().isEmpty()) {
            String nextCode = generateStockCode();
            stock.setStockCode(nextCode);
        }
        return stockRepository.save(stock);
    }

    @Override
    public String generateStockCode() {

        String uuid = UUID.randomUUID().toString();

        String customCode = uuid.substring(0, 4);

        return "STK-" + customCode.toUpperCase();
    }


    public Stock updateStock(int id, Stock updatedStock) {
        return stockRepository.findById(id)
                .map(existingStock -> {
                    if (updatedStock.getStockCode() != null) {
                        existingStock.setStockCode(updatedStock.getStockCode());
                    }
                    if (updatedStock.getStockName() != null) {
                        existingStock.setStockName(updatedStock.getStockName());
                    }
                    if (updatedStock.getStockPrice() != 0) {
                        existingStock.setStockPrice(updatedStock.getStockPrice());
                    }
                    if (updatedStock.getStockQuantity() != 0) {
                        existingStock.setStockQuantity(updatedStock.getStockQuantity());
                    }
                    if (updatedStock.getStockStatus() != null) {
                        existingStock.setStockStatus(updatedStock.getStockStatus());
                    }
                    if (updatedStock.getStockCategory() != null) {
                        existingStock.setStockCategory(updatedStock.getStockCategory());
                    }
                    if (updatedStock.getStockDescription() != null) {
                        existingStock.setStockDescription(updatedStock.getStockDescription());
                    }
                    if (updatedStock.getStockImagePath() != null) {
                        existingStock.setStockImagePath(updatedStock.getStockImagePath());
                    }
                    return stockRepository.save(existingStock);
                })
                .orElseThrow(() -> new EntityNotFoundException("Stock not found with id: " + id));
    }



    @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteStock(int id) {
        Optional<Stock> stock = stockRepository.findById(id);
        if (stock.isPresent()) {
            stockRepository.delete(stock.get());
            return true;
        }
        else{
            throw new RuntimeException("Stock not found");
        }
    }

    public Page<Stock> getAllStock(Pageable pageable) {
        return stockRepository.findAll(pageable);
    }

    public Stock getStockById(int id) {
        Optional<Stock> stock = stockRepository.findById(id);
        if (stock.isPresent()) {
            return stock.get();
        }else {
            throw new RuntimeException("Stock not found");
        }
    }

    public List<Stock> getAllStockList() {
        return stockRepository.findAll();
    }

    public List<Stock> getStockByName(String name) {
       return stockRepository.findByStockNameContainingIgnoreCase(name);
    }

    public List<Stock> getStocksByStatus(String status) {
        return stockRepository.findByStockStatus(status);
    }
    public List<Stock> getStocksByCategory(String category) {
        return stockRepository.findByStockCategory(category); // Kategoriye göre filtreleme
    }

    public Stock getStockByStockCode(String stockCode) {
        Optional<Stock> stock  = Optional.ofNullable(stockRepository.findByStockCode(stockCode));
        if (stock.isPresent()) {
            return stock.get();
        }else {
            throw new RuntimeException("Stock not found");
        }
    }


    public String saveStockImage(int id, MultipartFile file)throws IOException {
        Optional<Stock> stock = stockRepository.findById(id);
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Dosya boş olamaz");

        }
        String fileName = UUID.randomUUID().toString().substring(0,8) + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir).resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


        stock.get().setStockImagePath(fileName);
        stockRepository.save(stock.get());
        return fileName;

    }

    public Stock sellStock(String stockCode, int quantity) {
        Stock stock = stockRepository.findByStockCode(stockCode);
        if (stock == null) {
            throw new IllegalArgumentException("Stock not found for code: " + stockCode);
        }

        if (stock.getStockQuantity() < quantity) {
            throw new IllegalStateException("Insufficient stock for code: " + stockCode);
        }

        stock.setStockQuantity(stock.getStockQuantity() - quantity);

        stockRepository.save(stock);

        return stock;
    }
}
