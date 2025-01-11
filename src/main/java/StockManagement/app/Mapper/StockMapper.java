package StockManagement.app.Mapper;

import StockManagement.app.DTO.StockDTO;
import StockManagement.app.Model.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StockMapper {

    @Mapping(source = "stockName", target = "stockName")
    @Mapping(source = "stockCode", target = "stockCode")
    @Mapping(source = "stockCategory", target = "stockCategory")
    @Mapping(source = "stockPrice", target = "stockPrice")
    @Mapping(source = "stockQuantity", target = "stockQuantity")
    @Mapping(source = "stockStatus", target = "stockStatus")
    @Mapping(source = "stockDescription", target = "stockDescription")
    @Mapping(source = "stockImagePath", target = "stockImagePath")
    StockDTO toDTO(Stock stock);

    @Mapping(source = "stockName", target = "stockName")
    @Mapping(source = "stockCode", target = "stockCode")
    @Mapping(source = "stockCategory", target = "stockCategory")
    @Mapping(source = "stockPrice", target = "stockPrice")
    @Mapping(source = "stockQuantity", target = "stockQuantity")
    @Mapping(source = "stockStatus", target = "stockStatus")
    @Mapping(source = "stockDescription", target = "stockDescription")
    @Mapping(source = "stockImagePath", target = "stockImagePath")
    Stock toEntity(StockDTO stockDTO);
}