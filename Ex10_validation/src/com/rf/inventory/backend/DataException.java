/**
 * 
 */
package com.rf.inventory.backend;

/**
 * Thrown if there is a problem accessing data
 * @author v.lakshmanan
 *
 */
@SuppressWarnings("serial")
public class DataException extends RuntimeException {
    public DataException(Exception e){
        super(e);
    }
}
