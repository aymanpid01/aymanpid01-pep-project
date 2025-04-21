package DAO;

import Util.ConnectionUtil;
import Model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDao {
  
    public Account getAccountByUsername(String username){
        Connection connection = ConnectionUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Account account = null;
      
        try {

        String sql = "SELECT * FROM account WHERE username = ?";
        PreparedStatement preparedStatment = connection.preparedStatement(sql);
        preparedStatement.setString(1, username);
        rs = preparedStatement.executeQuery();

       if (rs.next()){ //using if-statment as we are expecting one row in result
        account = new Account(rs.getInt("account_id"), rs.getString("username"),
        rs.getString("password"));
       }

      } catch(SQLException e){
        e.printStackTrace();
      }
      return account;
    }
    
    public Account createAccount (Account account){
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Account createdAccount = null;

        try{
            conn = ConnectionUtil.getConnection();
            String sql = "INSERT INTO account (username, password) VALUES (?,?)"
        }
    }
    
}
