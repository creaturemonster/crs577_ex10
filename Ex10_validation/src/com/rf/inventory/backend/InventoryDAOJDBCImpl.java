package com.rf.inventory.backend;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Queries warehouse database
 * 
 * @author v.lakshmanan
 * 
 */
public class InventoryDAOJDBCImpl implements InventoryDAO {
    private static final String DELETE_SQL = "DELETE from STOCK where product_id = ?";
    private static final String QUERY_SQL = "SELECT product_id,quantity FROM STOCK WHERE product_id < 3050";
    private static final String INSERT_SQL = "INSERT into STOCK (quantity,product_id) VALUES (?,?) " +
                                             "ON DUPLICATE KEY UPDATE quantity=?";
    private static final String UPDATE_SQL = "UPDATE STOCK SET quantity=? where product_id = ?";
    private Logger log = LoggerFactory.getLogger(InventoryDAOJDBCImpl.class);
    private Connection conn; // non-pooled connection

    public InventoryDAOJDBCImpl() {
        try {
            Properties prop = new Properties();
            // InputStream is =
            // this.getClass().getClassLoader().getResourceAsStream("database.properties");
            InputStream is = this.getClass().getResourceAsStream(
                    "database.properties");
            if (is == null) {
                log.error("Unable to load database.properties from CLASSPATH.");
                return;
            }
            prop.load(is);
            String dbURL = prop.getProperty("DBURL");
            String driver = prop.getProperty("DRIVER");
            String user = prop.getProperty("USER");
            String passwd = prop.getProperty("PASSWORD");
            Class.forName(driver);
            conn = DriverManager.getConnection(dbURL, user, passwd);
        } catch (Exception e) {
            throw new DataException(e);
        }
    }

    @Override
    public void close() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            // ok
        }
    }

    @Override
    public boolean updateStockCount(int productId, int quantity) {
        try {
            PreparedStatement updateStmt = conn.prepareStatement(UPDATE_SQL);
            updateStmt.setInt(1, quantity);
            updateStmt.setInt(2, productId);
            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataException(e);
        }
    }

    // package friendly for unit testing
    Connection getConnection() {
        return conn;
    }

    @Override
    public boolean addItem(int productId, int quantity) {
        try {
            PreparedStatement insertStmt = conn.prepareStatement(INSERT_SQL);
            insertStmt.setInt(1, quantity);
            insertStmt.setInt(2, productId);
            insertStmt.setInt(3, quantity);
            return insertStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataException(e);
        }
    }

    @Override
    public ItemList getItems() {
        try {
            PreparedStatement getStmt = conn.prepareStatement(QUERY_SQL);
            ItemList items = new ItemList();
            ResultSet rs = getStmt.executeQuery();
            while (rs.next()) {
                Item item = new Item(rs.getInt(1), rs.getInt(2));
                items.getItems().add(item);
            }
            return items;
        } catch (SQLException e) {
            throw new DataException(e);
        }
    }

    @Override
    public void removeItem(int productId) {
        try {
            PreparedStatement deleteStmt = conn.prepareStatement(DELETE_SQL);
            deleteStmt.setInt(1, productId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataException(e);
        }
    }
}
