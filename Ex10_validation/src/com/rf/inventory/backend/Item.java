/**
 * 
 */
package com.rf.inventory.backend;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Data type corresponding to XML exchanged with the server. The XML looks like this:
 * <item productId="3212" quantity="4" description="A cool item" />
 */
@XmlRootElement
public class Item {
    
    // TODO: Add validation constraints to the productId field:
    //       1. id must not be null
    //       2. minimum id value is 1
    //       3. maximum id value is 999999999
    // HINT: See slide 10-10
    @NotNull
    @Size(min=1, max=999999999)
    private int productId;
    
    // TODO: Add validation constraints to the quantity field:
    //       1. quantity must not be null
    //       2. minimum quantity is 1
    //       3. maximum quantity is 9999999
    @NotNull
    @Size(min=1, max=999999999)
    private int quantity;
    
    // TODO: Add a validation constraint to the description field:
    //       1. Max length of message is 80 characters
    @Valid
    @Size(max=80)
    private String description;

    public Item() {
        this(-1, -1);
    }
    
    public Item(int productId, int quantity) {
        this(productId, quantity, null);
    }

    public Item(int productId, int quantity, String description) {
        this.productId = productId;
        this.quantity = quantity;
        this.description = description;
    }

    @XmlAttribute
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    @XmlAttribute
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @XmlAttribute
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
