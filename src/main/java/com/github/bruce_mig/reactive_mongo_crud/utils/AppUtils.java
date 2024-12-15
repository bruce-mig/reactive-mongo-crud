package com.github.bruce_mig.reactive_mongo_crud.utils;

import com.github.bruce_mig.reactive_mongo_crud.dto.ProductDto;
import com.github.bruce_mig.reactive_mongo_crud.entity.Product;
import org.springframework.beans.BeanUtils;

public class AppUtils {

    public static ProductDto entityToDto(Product product) {
//        ProductDto productDto = new ProductDto();
//        BeanUtils.copyProperties(product, productDto);
        ProductDto productDto = new ProductDto(
                product.getId(),
                product.getName(),
                product.getQty(),
                product.getPrice()
        );
        return productDto;
    }

    public static Product dtoToEntity(ProductDto productDto) {
//        Product product = new Product();
//        BeanUtils.copyProperties(productDto, product);
        Product product = new Product(
                productDto.getId(),
                productDto.getName(),
                productDto.getQty(),
                productDto.getPrice()
        );
        return product;
    }
}
