package StockManagement.app.Controller;

import StockManagement.app.DTO.StockDTO;
import StockManagement.app.Mapper.StockMapper;
import StockManagement.app.Model.Stock;
import StockManagement.app.Service.StockServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/stocks")
public class StockController {

    @Value("${upload.dir}")
    private String uploadDir;

    private final StockServiceImpl stockService;
    private final StockMapper stockMapper;

    private static final String ERROR_ACTION = "error";


    @Autowired
    public StockController(StockServiceImpl stockService, StockMapper stockMapper) {
        this.stockService = stockService;
        this.stockMapper = stockMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<StockDTO> addStock(@RequestBody @Validated StockDTO stockDTO) {
        Stock stock = stockMapper.toEntity(stockDTO);
        Stock createdStock = stockService.addStock(stock);
        StockDTO createdStockDTO = stockMapper.toDTO(createdStock);
        return new ResponseEntity<>(createdStockDTO, HttpStatus.CREATED);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable int id, @RequestBody Stock updatedStock) {
        try {
            Stock existingStock = stockService.getStockById(id);

            // Update only non-null fields
            if (updatedStock.getStockName() != null) existingStock.setStockName(updatedStock.getStockName());
            if (updatedStock.getStockCategory() != null) existingStock.setStockCategory(updatedStock.getStockCategory());
            if (updatedStock.getStockPrice() != 0) existingStock.setStockPrice(updatedStock.getStockPrice());
            if (updatedStock.getStockQuantity() != 0) existingStock.setStockQuantity(updatedStock.getStockQuantity());
            if (updatedStock.getStockDescription() != null) existingStock.setStockDescription(updatedStock.getStockDescription());

            Stock savedStock = stockService.updateStock(id , existingStock);
            return ResponseEntity.ok(savedStock);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping("/{stockCode}/{quantity}")
    public ResponseEntity<Stock> sellStock(@PathVariable String stockCode, @PathVariable int quantity) {
        try {
            Stock stock = stockService.sellStock(stockCode, quantity);
            return ResponseEntity.ok(stock);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Stok bulunamadı
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Yetersiz stok
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Genel hata
        }
    }



    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getFile(@PathVariable int id) {
        try {
            // Stok bilgisini alın
            Stock stock = stockService.getStockById(id);
            if (stock == null || stock.getStockImagePath() == null) {
                log.error("Hata: Stok bulunamadı veya resim yolu eksik (ID: {})", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Dosya yolunu belirle (static/img/StockPhotos altında olmalı)
            Path file = Paths.get("src/main/resources/static/img/StockPhotos/" + stock.getStockImagePath());
            log.info("Dosya yolu: {}", file.toAbsolutePath());

            // Dosya var mı ve okunabilir mi kontrolü
            if (!Files.exists(file) || !Files.isReadable(file)) {
                log.error("Hata: Dosya bulunamadı veya okunabilir değil (Yol: {})", file.toAbsolutePath());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Kaynak oluştur
            Resource resource = new UrlResource(file.toUri());

            // Dosya türünü belirle
            String fileType = Files.probeContentType(file);
            MediaType mediaType = fileType != null ? MediaType.parseMediaType(fileType) : MediaType.APPLICATION_OCTET_STREAM;

            // Yanıt döndür
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(resource);

        } catch (MalformedURLException e) {
            log.error("Hata: URL oluşturulurken bir sorun oluştu: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            log.error("Hata: Dosya okuma sırasında bir sorun oluştu: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadImage(@PathVariable int id,
                                              @RequestParam("file") MultipartFile file) {
        try {
            // Service sınıfındaki saveStockImage metodunu çağır
            String fileName = stockService.saveStockImage(id, file);

            // Başarılı bir şekilde dosya kaydedildiyse, dosya adı döndürülür
            return ResponseEntity.ok("File uploaded successfully: " + fileName);

        } catch (IOException e) {
            // Hata durumunda 500 kodu ile mesaj dönülür
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Dosya boşsa 400 hatası dönülür
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }


    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getAllStocks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        // Desteklenen sıralama alanları
        List<String> validSortFields = List.of("id", "stockName", "stockCategory", "stockQuantity", "stockPrice");

        try {
            // Sıralama alanı kontrolü
            if (!validSortFields.contains(sortBy)) {
                return ResponseEntity.badRequest()
                        .body(Map.of(ERROR_ACTION, "Geçersiz sıralama alanı: " + sortBy));
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

            Page<Stock> stockPage = stockService.getAllStock(pageable);

            List<StockDTO> stockDTOs = stockPage.getContent()
                    .stream()
                    .map(stockMapper::toDTO) // MapStruct ya da manuel dönüşüm
                    .toList();

            // Yanıta metadata bilgileri ekleniyor
            Map<String, Object> response = new HashMap<>();
            response.put("stocks", stockDTOs);
            response.put("currentPage", stockPage.getNumber()); // Sayfa numarası
            response.put("totalItems", stockPage.getTotalElements()); // Toplam öğe sayısı
            response.put("totalPages", stockPage.getTotalPages()); // Toplam sayfa sayısı

            // 200 OK ile döndürülüyor
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Hatalı sortBy değeri gibi durumlar
            return ResponseEntity.badRequest()
                    .body(Map.of(ERROR_ACTION, "Geçersiz sıralama alanı: " + sortBy));
        } catch (Exception e) {
            // Genel hata yönetimi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(ERROR_ACTION, "Bir hata oluştu"));
        }
    }




    @GetMapping("/list")
    public ResponseEntity<List<StockDTO>> getAllStocksList() {
        // Servis katmanından tüm stokları alır
        List<Stock> stocks = stockService.getAllStockList();

        // Eğer stok bulunamazsa 204 No Content döner
        if (stocks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Stokları DTO'ya dönüştürür
        List<StockDTO> stockDTOs = stocks.stream()
                .map(stockMapper::toDTO)
                .toList();

        // 200 OK ve stok listesini döner
        return ResponseEntity.ok(stockDTOs);
    }


    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> getStock(@PathVariable int id) {
        Stock stock = stockService.getStockById(id);
        if (stock != null) {
            log.info("Stock found: {}", stock); // Burada stock nesnesini logla
            StockDTO stockDTO = stockMapper.toDTO(stock);
            log.info("StockDTO: {}", stockDTO); // DTO çıktısını logla
            return ResponseEntity.ok(stockDTO);
        } else {
            log.info("Stock not found for id: {}", id); // Eğer stock null ise, bu mesajı alırsınız
        }
        return ResponseEntity.notFound().build();
    }



    // Search endPoint
    @GetMapping("/search")
    public ResponseEntity<List<StockDTO>> searchStocks(
            @RequestParam String stockname)
    {
        List<Stock> stocksByName = stockService.getStockByName(stockname);

        if (stocksByName.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<StockDTO> stockDTOs = stocksByName.stream().map(stockMapper::toDTO).toList();
        return ResponseEntity.ok(stockDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStock(@PathVariable int id) {
        boolean isDeleted = stockService.deleteStock(id);
        return isDeleted ? ResponseEntity.ok("Delete is success") : ResponseEntity.notFound().build();
    }

}
