package com.adi.inventory_service.controller;

import com.adi.inventory_service.model.Inventory;
import com.adi.inventory_service.model.InventoryRequest;
import com.adi.inventory_service.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//GET	/inventory/{productId}	🔍 Get current stock for a product
//POST	/inventory/reduce	➖ Reduce stock after order placed
//POST	/inventory/add	➕ Add stock (admin/restock use case)
//GET	/inventory/all	📄 View all inventory records

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService service;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getAvailableQuantity(@RequestHeader("X-Role") String role, @PathVariable int productId) {
        if (!role.equals("true")) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Access denied");
            response.put("message", "You do not have permission to perform this action.");

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(response);
        }

        Integer quantity = service.getAvailableQuantity(productId);
        return ResponseEntity.ok(quantity);
    }

    @PostMapping("/reduce")
    public ResponseEntity<?> reduceQuantity(@RequestHeader("X-Role") String role, @RequestBody InventoryRequest request) {
        if (!role.equals("true")) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Access denied");
            response.put("message", "You do not have permission to perform this action.");

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(response);
        }

        String res = service.reduceQuantity(request);

        if (res.equals("Success")) {
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        } else if (res.equals("Product not found")) {
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addQuantity(@RequestHeader("X-Role") String role, @RequestBody InventoryRequest request) {
        if (!role.equals("true")) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Access denied");
            response.put("message", "You do not have permission to perform this action.");

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(response);
        }

        String res = service.addQuantity(request);

        if (res.equals("Quantity Added")) {
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllQuantity(@RequestHeader("X-Role") String role) {
        if (!role.equals("true")) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Access denied");
            response.put("message", "You do not have permission to perform this action.");

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(response);
        }

        List<Inventory> allQuantity = service.getAvailableQuantity();
        return ResponseEntity.ok(allQuantity);
    }

}