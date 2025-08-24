package StockManagement.app.Model;

import StockManagement.app.Repository.StockRepository;
import StockManagement.app.Service.StockService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockInitializer implements CommandLineRunner {


    private final StockService stockService;

    private final ObjectMapper objectMapper;

    private final StockRepository stockRepository;

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
                log.info("Başlangıç verileri başarıyla yüklendi.");
            } else {
                log.error("Veri dosyasındaki stoklar boş!");
            }
        } else {
            log.error("initial-stocks.json bulunamadı!");
        }
    }
}

