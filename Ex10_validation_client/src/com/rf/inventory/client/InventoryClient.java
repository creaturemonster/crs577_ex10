/**
 *
 */
package com.rf.inventory.client;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Demonstrates the JAX-RS 2.0 Client API to consume messages from
 * a RESTful web service
 */
public class InventoryClient {
    // To intercept messages with Fiddler,
    // change the host name in the URL from localhost to the actual host name
    private static final String BASE_URL = "http://localhost:8080/ex10_validation/rs/item";

    private final Client client;

    public InventoryClient(){
        client = ClientBuilder.newClient();

        // To intercept messages with Fiddler, uncomment the following lines
        //System.setProperty("http.proxyHost", "127.0.0.1");
        //System.setProperty("http.proxyPort", "8888");
    }

    public void sendRequests() {

        try {
            // TODO: Replace invalid productId with valid productId; for example, 3012
            int productId = 3012;
            Item item = client.target(BASE_URL)
                                      .path("/{productId}")
                                      .resolveTemplate("productId", productId)
                                      .request()
                                      .get(Item.class);
            if (item != null) {
                System.out.println("GET response contains Item " + item.getProductId());
            }
            else {
            	System.out.println("No response from GET");
            }
        }
        catch (BadRequestException e) {
            System.err.println("Problem with GET request: " + e + ": " +
                    e.getResponse().readEntity(String.class));
        }

        try {
            // TODO: Fix invalid values
            int productId = 3012;
            int qty = 100;
            String desc = "item description";
            Item item = new Item(productId, qty, desc);

            Entity<Item> itemEntity = Entity.xml(item);
            String response = client.target(BASE_URL)
                                    .request(MediaType.APPLICATION_XML)
                                    .post(itemEntity, String.class);
            System.out.println("Got response " + response
                    + " from POST request with XML for " + item);
        }
        catch (BadRequestException e) {
            System.err.println("Problem with POST request: " + e + ": " +
                    e.getResponse().readEntity(String.class));
        }

        try {
            // TODO: Fix invalid values
            int productId = 3012;
            int qty = 100;
            String desc = "item description";
            Item item = new Item(productId, qty, desc);

            Entity<Item> itemEntity = Entity.xml(item);
            Response response = client.target(BASE_URL)
                                      .path("/{productId}")
                                      .resolveTemplate("productId", productId)
                                      .request(MediaType.APPLICATION_XML)
                                      .put(itemEntity, Response.class);

            if (response.getStatus() < 400) {
                System.out.println("Got response " + response +
                        " from PUT request product " + productId);
            }
            else {
                System.out.println("Got error response " + response +
                        " from PUT request product " + productId + ": " +
                        response.readEntity(String.class));
            }
        }
        catch (Exception e) {
            System.err.println("Problem with PUT request: " + e);
        }

    }

    public static void main(String[] args) {
        new InventoryClient().sendRequests();
    }
}
