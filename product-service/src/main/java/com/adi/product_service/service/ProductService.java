package com.adi.product_service.service;

import com.adi.product_service.model.Product;
import com.adi.product_service.repo.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

    public ResponseEntity<List<Product>> getProduct() {
        List<Product> response = repo.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Product> getProduct(int id) {
        Product response = repo.findById(id).get();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Product addProduct(Product newProduct, MultipartFile newProductImage) throws IOException {
        newProduct.setImageName(newProductImage.getOriginalFilename());
        newProduct.setImageType(newProductImage.getContentType());
        newProduct.setImageData(newProductImage.getBytes());

        return repo.save(newProduct);
    }

    @Transactional
    public List<Product> searchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }

    public void updateActiveStatus(Integer productId, boolean isActive) {
        Product product = repo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setActive(isActive);
        repo.save(product);
    }
}
