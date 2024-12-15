package com.github.bruce_mig.reactive_mongo_crud.controller;

import com.github.bruce_mig.reactive_mongo_crud.dto.ProductDto;
import com.github.bruce_mig.reactive_mongo_crud.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> getProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> getProductById(@PathVariable String id){
        return service.getProductById(id);
    }

    @GetMapping("/price-range")
    public Flux<ProductDto> getProductBetweenRange(
            @RequestParam("min") double min,
            @RequestParam("max") double max){
        return service.getProductInRange(min, max);
    }

    @PostMapping
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono){
        return service.saveProduct(productDtoMono);
    }

    @PutMapping("/update/{id}")
    public Mono<ProductDto> updateProduct(
            @RequestBody Mono<ProductDto> productDtoMono,
            @PathVariable String id){
        return service.updateProduct(productDtoMono, id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteProductById(@PathVariable String id){
        return service.deleteProductById(id);
    }

}
