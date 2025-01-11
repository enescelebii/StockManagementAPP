package StockManagement.app.Model;

import StockManagement.app.Repository.StockRepository;
import StockManagement.app.Service.StockService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StockInitializer implements CommandLineRunner {

    @Autowired
    private StockService stockService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StockRepository stockRepository;

    @Override
    public void run(String... args) throws Exception {
        // JSON dosyasını oku
        InputStream inputStream = getClass().getResourceAsStream("/data/initial-stocks.json");
        if (inputStream != null) {
            List<Stock> stocks = objectMapper.readValue(inputStream, new TypeReference<>() {});
            if (!stocks.isEmpty()) {

                // Stocklar için stockCode UUID ile otomatik olarak set ediliyor
                for (Stock stock : stocks) {
                    if (stock.getStockCode() == null || stock.getStockCode().isEmpty()) {
                        String nextCode = stockService.generateStockCode();
                        stock.setStockCode(nextCode);
                    }
                }

                stockRepository.saveAll(stocks); // Verileri veritabanına kaydediyoruz
                System.out.println("Başlangıç verileri başarıyla yüklendi.");
            } else {
                System.err.println("Veri dosyasındaki stoklar boş!");
            }
        } else {
            System.err.println("initial-stocks.json bulunamadı!");
        }
    }
}

