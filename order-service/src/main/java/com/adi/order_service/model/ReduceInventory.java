package com.adi.order_service.model;

public class ReduceInventory {
    private Integer productId;
    private Integer quantity;

    // Constructors
    public void ReduceInventoryRequest() {}

    public void ReduceInventoryRequest(Integer productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and setters
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
