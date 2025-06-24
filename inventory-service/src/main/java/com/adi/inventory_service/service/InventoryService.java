package com.adi.inventory_service.service;

import com.adi.inventory_service.model.Inventory;
import com.adi.inventory_service.model.InventoryRequest;
import com.adi.inventory_service.repo.InventoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepo repo;

    public Integer getAvailableQuantity(int productId) {
        Inventory prod = repo.findByProductId(productId);
        return prod.getQuantity();
    }

    public List<Inventory> getAvailableQuantity() {
        return repo.findAll();
    }

    public String reduceQuantity(InventoryRequest request) {
        Inventory product = repo.findByProductId(request.getProductId());
        if (product == null) {
            return "Product not found";
        }

        int updatedQuantity = product.getQuantity() - request.getQuantity();
        if (updatedQuantity >= 0) {
           product.setQuantity(updatedQuantity);
           repo.save(product);
           return "Success";
        } else {
            return "Quantity cannot be reduced below 0";
        }
    }

    public String addQuantity(InventoryRequest request) {
        Inventory product = repo.findByProductId(request.getProductId());

        if (product == null) {
            return "Product not found";
        }

        int updatedQuantity = product.getQuantity() + request.getQuantity();
        product.setQuantity(updatedQuantity);
        repo.save(product);

        return "Quantity added";
    }
}
