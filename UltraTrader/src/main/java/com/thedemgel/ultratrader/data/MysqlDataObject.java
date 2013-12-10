
package com.thedemgel.ultratrader.data;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.util.ConfigString;
import com.thedemgel.ultratrader.util.ShopAction;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import snaq.db.ConnectionPool;

public class MysqlDataObject extends DataObject {
	private ConnectionPool logpool;

	public MysqlDataObject() {
		super();
		try {
			Class c = Class.forName("com.mysql.jdbc.Driver");
			Driver driver = (Driver) c.newInstance();
			DriverManager.registerDriver(driver);
		} catch (ClassNotFoundException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Class not found: {0}", ex);
		} catch (InstantiationException | IllegalAccessException | SQLException ex) {
			Logger.getLogger(MysqlDataObject.class.getName()).log(Level.SEVERE, null, ex);
		}
    }

	@Override
	public void initLogger(UltraTrader plugin) {
		String url = "jdbc:mysql://"
			+ plugin.getConfig().getString(ConfigString.LOGGING_DATABASE_URL) + ":"
			+ plugin.getConfig().getInt(ConfigString.LOGGING_DATABASE_PORT)
			+ "/" + plugin.getConfig().getString(ConfigString.LOGGING_DATABASE_DBNAME)
			+ "?zeroDateTimeBehavior=convertToNull";

		logpool = new ConnectionPool("ultralog",
			1,
			10,
			30,
			180000,
			url,
			plugin.getConfig().getString(ConfigString.LOGGING_DATABASE_USERNAME),
			plugin.getConfig().getString(ConfigString.LOGGING_DATABASE_PASSWORD));


		String initTable = "CREATE TABLE IF NOT EXISTS `ultralog` ("
					+ "`id` int(11) NOT NULL AUTO_INCREMENT,"
					+ "`player` varchar(45) NOT NULL,"
					+ "`shopid` int(11) NOT NULL,"
					+ "`amount` decimal(20, 5) DEFAULT 0,"
					+ "`time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
					+ "`action` varchar(35) DEFAULT NULL,"
					+ "`message` varchar(300) DEFAULT NULL,"
					+ " PRIMARY KEY (`id`)"
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8";

		try (Connection con = logpool.getConnection(3000)) {
			Statement statement = con.createStatement();
			statement.execute(initTable);
		} catch (SQLException ex) {
			Logger.getLogger(MysqlDataObject.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void doLog(Shop shop, Player player, EconomyResponse resp, ShopAction action, String message) {
		try (Connection con = logpool.getConnection(3000)) {
			PreparedStatement statement = con.prepareStatement("INSERT INTO `ultralog` (`player`, `shopid`, `amount`, `action`, `message`) VALUES (?, ?, ?, ?, ?)");
			statement.setString(1, player.getName());
			statement.setInt(2, shop.getId());
			statement.setBigDecimal(3, BigDecimal.valueOf(resp.amount));
			statement.setString(4, action.name());
			statement.setString(5, message);
			statement.executeUpdate();
		} catch (SQLException ex) {
			Logger.getLogger(MysqlDataObject.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void save(Shop shop) {
	}

	@Override
	public void load(int shopid) {
	}

	@Override
	public void initShops() {
	}



}
