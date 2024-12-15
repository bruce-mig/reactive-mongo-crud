package com.github.bruce_mig.reactive_mongo_crud.service;

import com.github.bruce_mig.reactive_mongo_crud.dto.ProductDto;
import com.github.bruce_mig.reactive_mongo_crud.repository.ProductRepository;
import com.github.bruce_mig.reactive_mongo_crud.utils.AppUtils;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Flux<ProductDto> getAllProducts() {
        return repository.findAll()
                .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> getProductById(String id) {
        return repository.findById(id)
                .map(AppUtils::entityToDto);
    }

    public Flux<ProductDto> getProductInRange(double min, double max) {
        return repository.findByPriceBetween(Range.closed(min,max));
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono) {
        return productDtoMono.map(AppUtils::dtoToEntity)
                .flatMap(repository::save)
                .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id) {
         return repository.findById(id)
                 .flatMap(p -> productDtoMono.map(AppUtils::dtoToEntity)
                         .doOnNext(e -> e.setId(id)))
                 .flatMap(repository::save)
                 .map(AppUtils::entityToDto);
    }

    public Mono<Void> deleteProductById(String id) {
        return repository.deleteById(id);
    }
}
