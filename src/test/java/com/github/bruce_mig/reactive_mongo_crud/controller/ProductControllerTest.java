package com.github.bruce_mig.reactive_mongo_crud.controller;

import com.github.bruce_mig.reactive_mongo_crud.dto.ProductDto;
import com.github.bruce_mig.reactive_mongo_crud.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService service;

    @Test
    public void addProductTest(){
        // Given
        ProductDto productDto = new ProductDto("102", "mobile", 1, 1000);
        Mono<ProductDto> productDtoMono = Mono.just(productDto);

        // When
        when(service.saveProduct(any(Mono.class)))
                .thenReturn(productDtoMono);

        webTestClient.post().uri("/api/v1/products")
                .body(productDtoMono, ProductDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductDto.class)
                .isEqualTo(productDto);
    }

    @Test
    public void getProductTest(){
        // Given
        Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("102", "mobile", 1, 1000),
                new ProductDto("103", "TV", 1, 500));

        // When
        when(service.getAllProducts()).thenReturn(productDtoFlux);

        // Then
        Flux<ProductDto> responseBody = webTestClient.get().uri("/api/v1/products")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new ProductDto("102", "mobile", 1, 1000))
                .expectNext(new ProductDto("103", "TV", 1, 500))
                .verifyComplete();
    }

    @Test
    public void getProductByIdTest(){
        // Given
        ProductDto productDto = new ProductDto("102", "mobile", 1, 1000);
        Mono<ProductDto> productDtoMono = Mono.just(productDto);

        // When
        when(service.getProductById(any()))
                .thenReturn(productDtoMono);

        // Then
        Flux<ProductDto> responseBody = webTestClient.get().uri("/api/v1/products/102")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNextMatches(p -> p.getName().equals("mobile"))
                .verifyComplete();
    }

    @Test
    public void updateProductTest(){
        // Given
        ProductDto productDto = new ProductDto("102", "mobile", 1, 1000);
        Mono<ProductDto> productDtoMono = Mono.just(productDto);

        // When
        when(service.updateProduct(any(Mono.class),anyString()))
                .thenReturn(productDtoMono);

        webTestClient.put().uri("/api/v1/products/update/102")
                .body(productDtoMono, ProductDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductDto.class)
                .isEqualTo(productDto);

    }

    @Test
    public void deleteProductTest(){

        given(service.deleteProductById(anyString()))
                .willReturn(Mono.empty());

        webTestClient.delete().uri("/api/v1/products/delete/102")
                .exchange()
                .expectStatus().isNoContent();

    }
}
