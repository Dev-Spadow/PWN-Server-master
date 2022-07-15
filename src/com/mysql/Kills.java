/*package com.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.arlania.world.entity.impl.player.Player;

 
public class Kills implements Runnable {

	public static final String HOST = "109.106.250.201";
	public static final String USER = "equinox3_kills_admin";
	public static final String PASS = "tFyJZtwfkY";
	public static final String DATABASE = "equinox3_kills";

	public static final String TABLE = "hs_users";
	
	private Player player;
	private Connection conn;
	private Statement stmt;
	
	public Kills(Player player) {
		this.player = player;
	}
	
	public boolean connect(String host, String database, String user, String pass) {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}
			
			String name = player.getUsername().replace(" ", "_");
			
			PreparedStatement stmt1 = prepare("DELETE FROM "+TABLE+" WHERE username=?");
			stmt1.setString(1, name);
			stmt1.execute();
				
			PreparedStatement stmt2 = prepare("INSERT INTO "+TABLE+" (username, rights, mode, kills) VALUES (?, ?, ?, ?)");
			stmt2.setString(1, name);
			stmt2.setInt(2, player.getRights().ordinal());

			stmt2.setInt(3, 0); // game mode number
			stmt2.setInt(4, player.getKcSum()); // KILLS HERE
			stmt2.executeUpdate(); //Lets try this way
			
			
			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public PreparedStatement prepare(String query) throws SQLException {
		return conn.prepareStatement(query);
	}
	
	public void destroy() {
        try {
    		conn.close();
        	conn = null;
        	if (stmt != null) {
    			stmt.close();
        		stmt = null;
        	}
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
	
	public static String generateQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO "+TABLE+" (");
		sb.append("username, ");
		sb.append("rights, ");
		sb.append("mode, ");
		sb.append("kills, ");
		sb.append("VALUES (?, ?, ?, ?)");
		return sb.toString();
	}
	
	public ResultSet executeQuery(String query) {
        try {
        	this.stmt = this.conn.createStatement(1005, 1008);
            ResultSet results = stmt.executeQuery(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
	
}*/