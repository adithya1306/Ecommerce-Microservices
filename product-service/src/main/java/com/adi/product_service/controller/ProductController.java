package com.adi.product_service.controller;

import com.adi.product_service.model.Product;
import com.adi.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("")
    public ResponseEntity<List<Product>> getProducts() {
        return service.getProduct();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        return service.getProduct(id);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable int id){
        Product product = service.getProduct(id).getBody();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(product.getImageData(), headers, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> addProduct(@RequestHeader("X-Role") String role, @RequestPart Product product, @RequestPart MultipartFile newProductImage) {
        if (!role.equals("true")) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Access denied");
            response.put("message", "You do not have permission to perform this action.");

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(response);
        }

        Product newProduct = null;
        try {
            newProduct = service.addProduct(product,newProductImage);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(newProduct,HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = service.searchProducts(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // To update the status of the product after each order

    @PutMapping("/update-active")
    public ResponseEntity<Void> updateActiveStatus(
            @RequestParam Integer productId,
            @RequestParam boolean isActive
    ) {
        service.updateActiveStatus(productId, isActive);
        return ResponseEntity.ok().build();
    }
}
