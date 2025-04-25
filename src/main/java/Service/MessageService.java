package Service;
import java.util.List;

import DAO.AccountDao;
import DAO.MessageDao;
import Model.Message;

public class MessageService {
    private MessageDao messageDao;
    private AccountDao accountDao;

    public MessageService(){
        messageDao = new MessageDao();
        accountDao = new AccountDao();
    }
    public MessageService(MessageDao messageDao, AccountDao accountDao){
        this.accountDao = accountDao;
        this.messageDao = messageDao;
    }

    public Message createNewMessage(Message message){
        if(message.getMessage_text() == null || 
        message.getMessage_text().isBlank()
        || message.getMessage_text().length() > 255){
           return null; 
        }
        
        if(accountDao.getAccountById(message.getPosted_by()) == null){
        return null;
        }
        return messageDao.addMessage(message);
    }

    public Message getMessageById(int messageId){
        return messageDao.getMessageById(messageId);
    }
   
    public List<Message> getAllMessages(){
        return messageDao.getAllMessages();
    }

    public Message deleteMessage(int messageId){
        return messageDao.deleteMessage(messageId);
    }

    public Message updateMessage(int messageId, String replacementText){
        if(replacementText == null || replacementText.isBlank()
         || replacementText.length()>255){
            return null;
        }
        return messageDao.updateMessage(messageId, replacementText);
    }

    public List<Message> getUserMessages(int accountId){
        return messageDao.userMessages(accountId);
    }
}
