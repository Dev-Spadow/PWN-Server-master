/*package mysql.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.arlania.model.PlayerRights;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.content.PlayerPanel;
import com.arlania.world.entity.impl.player.Player;

/**
 * Using this class: To call this class, it's best to make a new thread. You can
 * do it below like so: new Thread(new Donation(player)).start();
 */
/*public class Donation implements Runnable {

	public static final String HOST = "51.75.65.161";
	public static final String USER = "admin_store";
	public static final String PASS = "lLTRcXuFuU";
															
	public static final String DATABASE = "admin_store";

	private Player player;
	private Connection conn;
	private Statement stmt;

	/**
	 * The constructor
	 * 
	 * @param player
	 */
	/*public Donation(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			player.lastDonationClaim = System.currentTimeMillis() + 30000;
			String name = player.getUsername().replace("_", " ");
			ResultSet rs = executeQuery("SELECT * FROM payments WHERE player_name='" + name + "' AND status='Completed' AND claimed=0");

			while (rs.next()) {
				int item_number = rs.getInt("item_number");
				@SuppressWarnings("unused")
				double paid = rs.getDouble("amount");
				int quantity = rs.getInt("quantity");

				switch (item_number) {// add products according to their ID in
										// the ACP

				case 18: // example
					player.getInventory().add(19936, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;

				case 19: // example
					player.getInventory().add(16455, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;

				case 20: // example
					player.getInventory().add(19938, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;

				case 21: // example
					player.getInventory().add(19938, quantity);
					player.getInventory().add(19938, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;

				case 22: // example
					player.getInventory().add(19938, quantity);
					player.getInventory().add(19938, quantity);
					player.getInventory().add(19938, quantity);
					player.getInventory().add(19938, quantity);
					player.getInventory().add(19938, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;

				case 23: // example
					player.getInventory().add(19938, quantity);
					player.getInventory().add(19938, quantity);
					player.getInventory().add(19938, quantity);
					player.getInventory().add(19938, quantity);
					player.getInventory().add(19938, quantity);
					player.getInventory().add(19938, quantity);
					player.getInventory().add(19938, quantity);
					player.getInventory().add(19938, quantity);
					player.getInventory().add(19938, quantity);
					player.getInventory().add(19938, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;

				case 87: // example
					player.getInventory().add(19943, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;

				case 88: // example
					player.getInventory().add(15374, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;

				case 89: // example
					final int[] drItems = {18950, 20054, 16141, 16139, 16140, 18961, 19893, 4777, 19754, 19896, 19821};
					player.getInventory().add(drItems[Misc.getRandom(drItems.length - 1)], quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;

				case 90: // example
					player.setRights(PlayerRights.LEGENDARY_DONATOR);
					player.incrementAmountDonated(1000);
					player.getPacketSender().sendRights();
					PlayerPanel.refreshPanel(player);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
				case 91: // example
					player.getInventory().add(6183, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
				case 92: // example
					player.getInventory().add(19055, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
				case 93: // example
					player.getInventory().add(19105, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
				case 94: // example
					player.setRights(PlayerRights.OBSIDIAN_DONATOR);
					player.incrementAmountDonated(200);
					player.getPacketSender().sendRights();
					PlayerPanel.refreshPanel(player);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
				case 95: // example
					player.getInventory().add(14453, quantity);
					player.getInventory().add(14455, quantity);
					player.getInventory().add(14457, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated for 2h double drops scroll Thanks for @red@Contributing@bla@!");
					break;
				case 96: // example
					player.getInventory().add(4770, quantity);
					player.getInventory().add(4771, quantity);
					player.getInventory().add(4772, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
				case 97: // example
					player.getInventory().add(17918, quantity);
					player.getInventory().add(17916, quantity);
					player.getInventory().add(17917, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
				case 98: // example
					player.getInventory().add(5130, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
				case 99: // example
					player.getInventory().add(5132, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
				case 100: // example
					player.getInventory().add(5131, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
				case 101: // example
					player.getInventory().add(19957, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
				case 102: // example
					player.getInventory().add(18865, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
				case 103: // example
					player.getInventory().add(18985, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
				case 104: // example
					player.getInventory().add(14561, quantity);
					player.getInventory().add(14562, quantity);
					player.getInventory().add(14560, quantity);
					player.getInventory().add(14565, quantity);
					player.getInventory().add(14564, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
				case 105: // example
					player.getInventory().add(19886, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
				case 106: // example
					player.getInventory().add(5081, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
					
				case 107: // example
					player.getInventory().add(19106, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendMessage("[<img=10>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;

					default:
						player.sendMessage("No donations was found under your name.");
						break;
				}
				rs.updateInt("claimed", 1);
				rs.updateRow();
			}
			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param host
	 *            the host ip address or url
	 * @param database
	 *            the name of the database
	 * @param user
	 *            the user attached to the database
	 * @param pass
	 *            the users password
	 * @return true if connected
	 */
/*	public boolean connect(String host, String database, String user, String pass) {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database, user, pass);
			return true;
		} catch (SQLException e) {
			System.out.println("Failing connecting to database!");
			return false;
		}
	}

	/**
	 * Disconnects from the MySQL server and destroy the connection and
	 * statement instances
	 */
	/*public void destroy() {
		try {
			conn.close();
			conn = null;
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes an update query on the database
	 * 
	 * @param query
	 * @see {@link Statement#executeUpdate}
	 */
	/*public int executeUpdate(String query) {
		try {
			this.stmt = this.conn.createStatement(1005, 1008);
			int results = stmt.executeUpdate(query);
			return results;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	/**
	 * Executres a query on the database
	 * 
	 * @param query
	 * @see {@link Statement#executeQuery(String)}
	 * @return the results, never null
	 */
	/*public ResultSet executeQuery(String query) {
		try {
			this.stmt = this.conn.createStatement(1005, 1008);
			ResultSet results = stmt.executeQuery(query);
			return results;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
*/