package com.rf.inventory.backend;

import junit.framework.TestCase;


/**
 * @author ROI
 * 
 */
public class InventoryDAOTest extends TestCase {
    protected InventoryDAOJDBCImpl dao;
    
    @Override
    protected void setUp() throws Exception {
        dao = new InventoryDAOJDBCImpl();
        dao.getConnection().setAutoCommit(false);
    }

    @Override
    protected void tearDown() throws Exception {
        dao.getConnection().rollback();
        dao.close();
    }

    public void testUpdate(){
        boolean updated = dao.updateStockCount(3012, 5);
        assertTrue(updated);
        ItemList itemList = dao.getItems();
        for (Item item : itemList.getItems()){
            if (item.getProductId() == 3012){
                assertTrue(item.getQuantity() == 5);
                return;
            }
        }
        fail("Could not find 3012");
    }
    
    public void testGetAll(){
        ItemList itemList = dao.getItems();
        assertTrue(itemList.getItems().size() > 0);
    }
    
    public void testDelete(){
        dao.removeItem(3012);
        ItemList itemList = dao.getItems();
        for (Item item : itemList.getItems()){
            if (item.getProductId() == 3012){
                fail("Item not removed");
            }
        }
    }
    
    public void testInsert(){
        dao.addItem(12, 3);
        ItemList itemList = dao.getItems();
        for (Item item : itemList.getItems()){
            if (item.getProductId() == 12){
                assertTrue(item.getQuantity() == 3);
                return; // ok
            }
        }
        fail("Item not inserted");
    }
}
