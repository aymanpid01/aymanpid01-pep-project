package DAO;

import Util.ConnectionUtil;
import Model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class AccountDao {
    
    // Registration User story
    public Account getAccountByUsername(String username){
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;
      
        try {

        String sql = "SELECT * FROM account WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();

       if (rs.next()){ //using if-statment as we are expecting one row in result
        account = new Account(rs.getInt("account_id"), rs.getString("username"),
        rs.getString("password"));
       }

      } catch(SQLException e){
        System.out.println(e.getMessage());
      }
      return account;
    }
    
    public Account createAccount (Account account){
        Account createdAccount = null;
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2,account.getPassword());
            // if insert is successful, take the generated accountID from the db and build a new Account object with it 
            int rowsChanged = preparedStatement.executeUpdate();
            if(rowsChanged > 0){ //checks if insert was successful
                ResultSet rs = preparedStatement.getGeneratedKeys(); // gets generated pkey after successful insert
              // read first row from result of created keys
              // retrieve the new accountId and initialize a new Acc obj
                if(rs.next()){
                    int accountId = rs.getInt(1);
                    createdAccount = new Account (accountId, account.getUsername(), account.getPassword());
                }
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
            }
            return createdAccount;
    }
    
    // Login User Story
    public Account getAccountByCreds(String username, String password){
        Account account = null;
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));  
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return account;
    }

    //Get account by id
    public Account getAccountById(int accountId){
        Account account = null;
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setInt(1, accountId);
            ResultSet rs = prepared.executeQuery();
            
            if(rs.next()){
                account = new Account(rs.getInt("account_id"),
                 rs.getString("username"),
                  rs.getString("password"));
                
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return account;
    }
}
