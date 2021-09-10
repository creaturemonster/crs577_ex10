package com.rf.inventory.webapps;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

/**
 * By default, JAX-RS does not send bean validation errors to client; instead,
 * the client gets only the HTTP status. This a best practice for security: we
 * want to give an attacker as little information as possible about our system.
 * 
 * But for this exercise, we want the client to get the entire validation error.
 * There's no JAX-RS standard technique to configure this, so we extend the
 * Jersey ResourceConfig class instead of the standard JAX-RS Application, and
 * then we set a Jersey-specific property.
 */
@ApplicationPath("") // deploy fails on GlassFish without @ApplicationPath
public class InventoryApplication extends ResourceConfig {
    // ResourceConfig extends Application
    
    public InventoryApplication() {
        // We want validation errors to be sent to the client.
        // Note that ServerProperties is Jersey-specific 
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
    }
    
}
