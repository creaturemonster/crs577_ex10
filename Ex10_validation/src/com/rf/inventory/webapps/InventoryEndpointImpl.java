package com.rf.inventory.webapps;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rf.inventory.backend.InventoryDAO;
import com.rf.inventory.backend.InventoryDAOJDBCImpl;
import com.rf.inventory.backend.Item;
import com.rf.inventory.backend.ItemList;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * RESTful service implemented with JAX-RS 2.0
 * 
 * See https://jersey.java.net
 * See the Jersey User Guide http://jersey.java.net/documentation/latest/user-guide.html
 * See JSR 339 Javadocs http://jcp.org/aboutJava/communityprocess/final/jsr339/index.html
 * @author Mike Woinoski
 */
@Path("/item")
public class InventoryEndpointImpl {
    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(InventoryEndpointImpl.class);

    private InventoryDAO dao = new InventoryDAOJDBCImpl();

    /**
     * Handles HTTP GET. Sends the complete inventory.
     * Request URL will be http://host:8080/inventory/rs/item/all
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_XML)
    public ItemList doGet() {
        ItemList itemList = dao.getItems();
        return itemList;
    }

    /**
     * Handles HTTP GET. Sends details of a single item.
     * Request URL will be http://host:8080/inventory/rs/item/3012
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    // BONUS TODO: (Skip until step 16)
    //             Validate the productId using your custom validation annotation
    public Item doGet( @PathParam("id") int productId) {
        ItemList itemList = dao.getItems();
        Item item = null;
        if (itemList != null) {
            for (Item i : itemList.getItems()) {
                if (i.getProductId() == productId) {
                    item = i;
                    break;
                }
            }
        }
        return item;
    }

    /**
     * Handles HTTP Delete. The request URI (e.g. /item/3012) identifies
     * the productId of the item to be deleted from inventory
     * Request URL will be http://host:8080/inventory/rs/item/3012
     */
    @DELETE
    @Path("/{productId}")
    // TODO: Add validation constraints to the id parameter:
    //       1. id must not be null
    //       2. minimum id value is 1
    //       3. maximum id value is 999999999
    // HINT: See slide 10-5
    // HINT: If you see compiler errors, verify that you imported the annotations
    //       from the package javax.validation.constraints
    public Response doDelete(@NotNull @Size(min=1, max=999999999) @PathParam("productId") int id ) {
        dao.removeItem(id);
        return Response.ok().build(); 
    }

    /**
     * Handles HTTP POST. Creates a new Item with a new unique id.
     * Incoming content should be something like <item productId="3212" quantity="4" />
     * Request URL will be http://host:8080/inventory/rs/item
     */
    @POST 
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    // TODO: Add a constraint so all the properties of the Item parameter are validated
    // HINT: See slide 10-13
    public String doPost(@Valid @BeanParam Item item) throws WebApplicationException {
        dao.addItem(item.getProductId(), item.getQuantity());
        return "<ok/>";
    }

    /**
     * Handles HTTP PUT. Updates the quantity of productId. Incoming content should be
     * something like <item quantity="4" /> The request URI (e.g. /item/3012)
     * identifies productId
     * Request URL will be http://host:8080/inventory/rs/item/3012
     */
    @PUT
    @Path("/{productId}")
    @Consumes(MediaType.APPLICATION_XML) 
    // TODO: 
    // 1. Add validation constraints to the id parameter: not null, min = 1, max = 999999999.
    // 2. Define custom error messages for each validation
    // 3. Ensure that all the properties of the Item parameter are validated
    // HINT: See slide 10-9, 10-13
    public Response doPut(@NotNull @Size(min=1, max=999999999)@PathParam("productId") int id, 
                          Item item )
                    throws WebApplicationException {
        item.setProductId(id);
        dao.updateStockCount(item.getProductId(), item.getQuantity());
        return Response.accepted().build();
    }
}
