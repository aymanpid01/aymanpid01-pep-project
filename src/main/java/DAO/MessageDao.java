package DAO;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {

    public Message addMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        Message newMessage = null;

        try{
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?,?,?)";
            PreparedStatement prepared = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            prepared.setInt(1,message.getPosted_by());
            prepared.setString(2, message.getMessage_text());
            prepared.setLong(3, message.getTime_posted_epoch());
            int addedrows = prepared.executeUpdate();
            if(addedrows>0){
                ResultSet rs = prepared.getGeneratedKeys();
                if(rs.next()){
                    int messageID = rs.getInt(1);
                    newMessage = new Message(messageID, message.getPosted_by(),
                     message.getMessage_text(), message.getTime_posted_epoch());
                }
            } 
           
        } catch(SQLException e){
            System.out.println(e.getMessage());
            }
            return newMessage;
    }

    public Message getMessageById(int messageId){
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;

        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                message = new Message(rs.getInt("message_id"), 
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return message;
    }

    public List<Message> getAllMessages(){
        List<Message> allMessages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message";
            PreparedStatement prepared = connection.prepareStatement(sql);
            ResultSet rs = prepared.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),rs.getInt("posted_by"), rs.getString("message_text"),rs.getLong("time_posted_epoch"));
                allMessages.add(message);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return allMessages;
    }

    public Message deleteMessage(int messageId){
        Connection connection = ConnectionUtil.getConnection();
        Message deletedMessage = null;    
        try{
            // first retrieve message data before deletion
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setInt(1, messageId);
            ResultSet rs = prepared.executeQuery();

            if(rs.next()){
                deletedMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            String delSql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(delSql);
            deleteStatement.setInt(1, messageId);
            deleteStatement.executeUpdate();
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return deletedMessage;
    }

    public Message updateMessage(int messageId, String replacementText){
        Connection connection = ConnectionUtil.getConnection();
        Message updatedMessage = null;

        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setInt(1, messageId);
            ResultSet rs = prepared.executeQuery();
            if(rs.next()){
                String sqlUpdate = "UPDATE message SET message_text = ? WHERE message_id =?";
                prepared = connection.prepareStatement(sqlUpdate);
                prepared.setInt(2, messageId);
                prepared.setString(1, replacementText);
                prepared.executeUpdate();

                updatedMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), replacementText,
                rs.getLong("time_posted_epoch"));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return updatedMessage;
    }

    public List<Message> userMessages(int accountId){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> userMessages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,accountId);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                userMessages.add(message);
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return userMessages;
    }

}
    

