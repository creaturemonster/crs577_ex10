package com.rf.inventory.client;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Data type corresponding to XML posted by server. The XML looks like this:
 * <items>
 *    <item productId="3212" quantity="4" />
 *    <item productId="3204" quantity="7" />
 * </items>
 *
 * @author v.lakshmanan
 *
 */
// TODO: Examine this developer-supplied class 
//       (no changes required)
@XmlRootElement(name="items")
public class ItemList {
    private List<Item> items = new ArrayList<>();

    @XmlElement(name="item")
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
