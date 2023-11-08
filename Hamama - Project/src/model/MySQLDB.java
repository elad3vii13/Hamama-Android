package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class MySQLDB {
	static Connection con;
	static boolean failedToConnect = false;
	
	// Mysql credentials
	static final String REMOTE_USER="elad2021";
	static final String REMOTE_PSW= "DeaLnaiDeL";
	static final String LOCAL_USER="root";
	static final String LOCAL_PSW= "1313";
	
	// Url variables by default it will run on docker
	static final String DOCKER_CONTAINER_DOMAIN = "mysqldb";	
	static final String DEV_DOMAIN = "localhost";
	static final int DEFAULT_MYSQL_PORT = 3306;
	static final boolean RUNS_ON_DOCKER = false;
	
	public static String urlBuilder()
	{
		if(RUNS_ON_DOCKER) {
			return "jdbc:mysql://" + DOCKER_CONTAINER_DOMAIN + ":" + DEFAULT_MYSQL_PORT + "/sotz?serverTimezone=Asia/Jerusalem";
		} else {
			return "jdbc:mysql://" + DEV_DOMAIN + ":" + DEFAULT_MYSQL_PORT + "/sotz?serverTimezone=Asia/Jerusalem";
		}
	}

	public MySQLDB(){
		String connectionURL = urlBuilder();
		System.out.println(connectionURL);
		
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con= DriverManager.getConnection(connectionURL, "root", "1313");
        } catch (Exception e) {
			System.out.println("error in connecting to the DB");
			con = (Connection)null;	
        }
	}
	
	public MySQLDB(boolean isProduction, boolean retry){
		if (con!= null || (failedToConnect &&!retry)) {
			return;
		}

		failedToConnect = false;
		
		if (isProduction) {
			con = connectToCloudDbByIP(REMOTE_USER, REMOTE_PSW);
		} else {
			con = connectToLocalDB(LOCAL_USER, LOCAL_PSW);
		}
	}
	
	private static Connection connectToLocalDB(String user, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url= String.format(urlBuilder());
			
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException | ClassNotFoundException e) { 
			e.printStackTrace();
			System.out.println("error in connecting to the DB");
			System.out.println(e.getMessage());
			failedToConnect = true;
			
			return null;
		} 
	}

	private static Connection connectToCloudDbByIP(String user, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver"); 
			String url= String.format("jdbc:mysql://35.205.18.21:3306/sotz"); 
			
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException | ClassNotFoundException e) { 
			e.printStackTrace();
			System.out.println("error in connecting to the DB");
			failedToConnect = true;
			
			return null;
		} 
	}
	
	public boolean IsDbConnected() {
		return con != null;
	}
	
	public void updateLastLogin(long lastLogin, String name) {
       String sqlString = "UPDATE users SET lastLogin = '" + lastLogin + "'" + " WHERE nickname = '" + name + "'";
       
       try {
    	   Statement statement = con.createStatement();
	       statement.executeUpdate(sqlString);
	       statement.close();
       } catch(SQLException ex) {
    	   System.out.println("SQLException: " + ex.getMessage());
       }	
	}
	
	public boolean UserExists(String nickname){
		try {
		    Statement statement = con.createStatement();
            String queryString = "SELECT * FROM users WHERE nickname='" + nickname + "'";
            ResultSet rs = statement.executeQuery(queryString);
            
            return((rs != null) && (rs.next()));
		} catch (Exception e) {
			System.out.println("error in querying the DB");
			
			return true;			
	    }
	}
	
	public void AddNewUser(User user){
       String sqlString = "INSERT INTO users" + " (nickname, password, role)" 
							+ "VALUES ('"
							+ user.getNickName() + "', '" 
        					+ user.getPassword()  + "', '" 
							+ user.getRole() + "')";
        try {
        		Statement statement = con.createStatement();
	            statement.executeUpdate(sqlString);
	            statement.close();
            } catch(SQLException ex) {
            	System.out.println("SQLException: " + ex.getMessage());
            }
	}

	public boolean ModifyUser(User user){
	    try {
		        String updString = "UPDATE users SET password =?, role = ?"
		        		+ "   WHERE nickname='" + user.getNickName() + "'" ;
				PreparedStatement statement = con.prepareStatement(updString);
				statement.setString(3, user.getPassword());
				statement.setString(4, user.getRole());
	            statement.executeUpdate();
			    statement.close();
			    
			    return true;
	    	} catch(SQLException ex) {
	    		System.out.println("SQLException: " + ex.getMessage());
	    		
				return false;
			}			
	}
	
	public User UserAuthenticate(String name, String pass){
		try{
		    Statement statement = con.createStatement();
            String queryString = "SELECT * FROM users WHERE nickname='" + name + "' and password='" + pass + "'" ;
            ResultSet rs = statement.executeQuery(queryString);
            
            if (rs.next() == true) {
            	User u = new User();
            	u.setId(rs.getInt("id"));
            	u.setNickName(rs.getString("nickname"));
            	u.setRole(rs.getString("role"));
            	u.setPassword(pass);
            	
            	return u;
            } else {
            	return null;
            }
 		} 
		catch (Exception e) 
		{
 			System.out.println("error in querying the DB");
 			
			return null;			
		}
	}
	
	public List<User> getAllUsers() {
	    Statement statement;
        ResultSet rs;
        
		try {
				statement = con.createStatement();
		        String queryString;
		        queryString = "SELECT * FROM users";
				rs = statement.executeQuery(queryString);
				List<User> result = new ArrayList<User>();
				
				while(rs.next()) {
					User u = new User();
					u.setNickName(rs.getString(rs.findColumn("nickname")));
					u.setPassword(rs.getString(rs.findColumn("password")));
					u.setRole(rs.getString(rs.findColumn("role")));
					u.setId(rs.getInt(rs.findColumn("id")));
					u.setLastLogin(rs.getLong(rs.findColumn("lastLogin")));
					result.add(u);
				}
				
				return result;
		} catch (SQLException e) 
		{
			System.out.println("error in querying the DB");
			e.printStackTrace();
			
			return null;
		}
	}
	
	public List<String> getAllUsersNickNames() {
	    Statement statement;
        ResultSet rs;
        
		try {
			statement = con.createStatement();
	        String queryString;
	        queryString = "SELECT nickname FROM users";
	        queryString += " order by nickname DESC";
			rs = statement.executeQuery(queryString);
			List<String> users = new ArrayList<String>();
			
			while (rs.next())
				users.add(rs.getString(rs.findColumn("nickname")));
			
			return users;
		} catch (SQLException e) 
		{
			System.out.println("error in querying the DB");
			e.printStackTrace();
			
			return null;
		}
	}
	
	public boolean DeleteUser(String nickName) {
		try{
	        String delString = "DELETE FROM users  WHERE nickname=?";
			PreparedStatement statement = con.prepareStatement(delString);
			statement.setString(1, nickName);
            statement.executeUpdate();
            
            return true;
		} catch (Exception e) {
			System.out.println("error deleting the user");
			
			return false;
		}
	}
	
	public void Close() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con = null;
		}
	}
	
	public void addUser (String name) {
		String sqlString = "INSERT INTO  users" + " (name)" 
				+ "VALUES ('"+ name + "')";
		
		try {
			Statement statement = con.createStatement();
			statement.executeUpdate(sqlString);
			statement.close();
		} catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}			
	}

	public void addMeasure (Measure measure) {
		String sqlString = "INSERT INTO  measures" + " (time, value, sid)" 
				+ "VALUES ("+ measure.getTime()+ "," + measure.getValue() +", " + measure.getSid() +")";
		try {
			Statement statement = con.createStatement();
			statement.executeUpdate(sqlString);
			statement.close();
		} catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}			
	}

	public void addLogEntry (Log log) {

		String sqlString = "INSERT INTO  log" + " (time, priority, message, sid)" 
				+ "VALUES ("+ log.getTime()+ ",'" + log.getpriority() +"', '" + log.getMessage()+ "', " + log.getSid() + ")";
		try {
			Statement statement = con.createStatement();
			statement.executeUpdate(sqlString);
			statement.close();
		} catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}			
	}
	
	public ArrayList<Measure> getMeasures(int sid, long from, long to){ 
		Statement statement; 
		String	sqlString = "SELECT * FROM measures where sid=" + sid +
									" and time between " + from + " and " + to + " order by time ASC";
		try {
			statement = con.createStatement(); ResultSet rs=statement.executeQuery(sqlString);
			ArrayList<Measure> result = new ArrayList<Measure>();
			
			while(rs.next()) {
				Measure m = new Measure();
				m.setTime(rs.getLong(rs.findColumn("time")));
				m.setValue(rs.getDouble(rs.findColumn("value")));
				result.add(m);
			}
			
			statement.close(); 
			
			return result;
		} 
		catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage()); 
			
			return null; 
		}

	} 
	
	public HashMap<Integer, String> getAllSensorsNames(){
		Statement statement; 
		String	sqlString = "SELECT * FROM sensors";
		
		try {
			statement = con.createStatement(); 
			ResultSet rs=statement.executeQuery(sqlString);
			HashMap<Integer, String>result = new HashMap<Integer, String>();
			
			while (rs.next()) {
				result.put(rs.getInt("id"), rs.getString("displayName"));
			}
			
			return result;
		} catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage()); 
			
			return null; 
		}		
	}
		
	public ArrayList<Log> getLogEntries(String sid, long from, long to, String priority){ 
		Statement statement; 
		String filter ="where time between " + from + " and " + to;
		
		if (sid != null) {
			filter += " and sid=" + sid;
		}
		
		if (priority != null) {
			filter += " and priority= '" + priority + "'";
		}
		
		String	sqlString = "SELECT * FROM log " + filter + " order by time ASC";
		
		try {
			statement = con.createStatement(); ResultSet rs=statement.executeQuery(sqlString);
			ArrayList<Log> result = new ArrayList<Log>();
			
			while(rs.next()) {
				Log m = new Log();
				m.setTime(rs.getLong(rs.findColumn("time")));
				m.setSid(rs.getInt(rs.findColumn("sid"))); 
				m.setpriority(rs.getString(rs.findColumn("priority")));
				m.setMessage(rs.getString(rs.findColumn("message")));
				result.add(m);
			}
			
			statement.close(); 
			
			return result;
		} catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage()); 
			
			return null; 
		}
	}
	
	public Sensor getSensorProperties(int sid) {
		Statement statement; 
		String	sqlString = "SELECT * FROM sensors where id=" + sid;
		
		try {
			statement = con.createStatement(); 
			ResultSet rs=statement.executeQuery(sqlString);
			
			if (rs.next()) {
				Sensor s = new Sensor();
				s.setId(rs.getInt("id"));
				s.setName(rs.getNString("name"));
				s.setUnits(rs.getNString("units"));
				
				return s;
			} else {
				return null;
			}
		} catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage()); 
			
			return null; 
		}
	}
	
	public ArrayList<Sensor> getAllSensors() {
		Statement statement; 
		String	sqlString = "SELECT * FROM sensors";
		
		try {
			statement = con.createStatement(); 
			ResultSet rs=statement.executeQuery(sqlString);
			ArrayList<Sensor> result = new ArrayList<Sensor>();
			
			while (rs.next()) {
				Sensor s = new Sensor();
				s.setId(rs.getInt("id"));
				s.setDisplayName(rs.getString("displayName"));
				s.setName(rs.getNString("name"));
				s.setUnits(rs.getNString("units"));
				result.add(s);
			}
			
			return result;
		}
		catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage()); 
			
			return null; 
		}
	}
}
