package net.javaguides.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.javaguides.usermanagement.models.User;

// For Database Access Operations
// Step 1 -- Create Format for Connection--- 
public class UserDao {
	
	private String jdbcUrl = "jdbc:mysql://localhost:3306/usermanagement?useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "Ma987soni@1995";
	
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, country) VALUES " +
		        " (?, ?, ?);";
	  
     private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
	 private static final String SELECT_ALL_USERS = "select * from users";
	 private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
	 private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";
	 
	 //Step 2. Create Connection----
	 protected Connection getConnection()
	 {
		 Connection connection = null ;
		 try {
			 Class.forName("com.mysql.cj.jdbc.Driver");
			 connection = DriverManager.getConnection(jdbcUrl,jdbcUsername,jdbcPassword);			 
		 }
		 catch(SQLException e )
		 {
			 e.printStackTrace();
		 }
		 catch(ClassNotFoundException e)
		 {
			 e.printStackTrace();
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		return connection;
		 
	 }
	 
	 //Step 3-- Create or Insert User ---
	 
	 public void insertUser(User user) throws SQLException
	 {
		 try(Connection connection = getConnection(); 
				 PreparedStatement preparedstatement = connection.prepareStatement(INSERT_USERS_SQL);
			)
		 {
			 preparedstatement.setString(1, user.getName());
			 preparedstatement.setString(2, user.getEmail());
			 preparedstatement.setString(3, user.getCountry());
			 preparedstatement.executeUpdate();
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
	 } 
	 
	 // Step 4 Update User----
	 
	 public boolean updateUser(User user) throws SQLException
	 {
		 boolean rowUpdated ;
		 try(Connection connection = getConnection();
				 PreparedStatement preparedstatement = connection.prepareStatement(UPDATE_USERS_SQL);
			)
		 {
			 preparedstatement.setString(1, user.getName());
			 preparedstatement.setString(2, user.getEmail());
			 preparedstatement.setString(3, user.getCountry());
			 preparedstatement.setInt(4, user.getId());
			 
			 rowUpdated = preparedstatement.executeUpdate() > 0 ;
		 }
		 
		return rowUpdated;	 
	 } 
	 
	 //Step 5 --Select User by Id ----
	 
	 public User selectUser(int id) throws SQLException
	 {
		 User user = null ;
		 
		 try(Connection connection = getConnection();
				 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
		){
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next())
			{
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");

				user = new User(id, name, email, country);
			}
		 }
		 catch(Exception e )
		 {
			 e.printStackTrace();
		 }
		 
		return user;
		 
	 }
	 //Step 6 -- Select Users-----
	 
	 public List<User> selectAllUsers() throws SQLException
	 {
		 List< User> users = new ArrayList<>();
		 
		 try(Connection connection = getConnection();
				 PreparedStatement preparedstatement = connection.prepareStatement(SELECT_ALL_USERS))
		 {
			 System.out.println(preparedstatement);
			 ResultSet rs = preparedstatement.executeQuery();
			 while(rs.next())
			 {
				 int id = rs.getInt("id");
				 String name  = rs.getString("name");
				 String email = rs.getString("email");
				 String country = rs.getString("country");
				 users.add(new User(id, name, email, country));
			 }
				 
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		return users;
		 
	 }	 
	 
	 //Step 7 -- Delete Users----
	 public boolean deleteUser(int id)throws SQLException
	 {
		 boolean rowDeleted ;
		 try(Connection connection = getConnection();
				 PreparedStatement preparedstatement = connection.prepareStatement(DELETE_USERS_SQL))
		 {
			 preparedstatement.setInt(1, id);
			 rowDeleted  = preparedstatement.executeUpdate()> 0;
		 }
		 
		return rowDeleted;
		 
	 }
	 private void printSQLException(SQLException e)
	 {
		 for(Throwable ex : e)
		 {
			 if(ex instanceof SQLException)
			 {
				 ex.printStackTrace(System.err);
				 System.err.println("SQLState: " + ((SQLException) ex).getSQLState());
	                System.err.println("Error Code: " + ((SQLException) ex).getErrorCode());
	                System.err.println("Message: " + e.getMessage());
	                Throwable t = e.getCause();
	                while (t != null) {
	                    System.out.println("Cause: " + t);
	                    t = t.getCause();
	                }
	            }
	        }
	    }
}
