/**
 * 
 */
package com.rf.inventory.client;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Data type corresponding to XML exchanged with the server. The XML looks like this:
 * <item productId="3212" quantity="4" description="A cool item" />
 */
@XmlRootElement
public class Item {
    private int productId;
    private int quantity;
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
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
