package com.rf.inventory.backend;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

/**
 * Data type corresponding to XML exchanged with the server. The XML looks like this:
 * <items>
 *    <item productId="3212" quantity="4" />
 *    <item productId="3204" quantity="7" />
 * </items>
 * 
 * @author v.lakshmanan
 *
 */

@XmlRootElement(name="items")
public class ItemList {
    
    private List<Item> items = new ArrayList<Item>();

    @XmlElement(name="item")
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
