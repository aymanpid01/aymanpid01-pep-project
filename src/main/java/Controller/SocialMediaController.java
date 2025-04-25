package Controller;

import static org.mockito.Mockito.never;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;



/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService = new AccountService();
    private MessageService messageService = new MessageService();
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getUserMessagesHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // creates an account object initialized with the JSON from the req body that is converted to account
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account createdAccount = accountService.register(account);
        
        if (createdAccount != null){
            ctx.json(mapper.writeValueAsString(createdAccount));

        } else{
            ctx.status(400);
        }
    }
    private void loginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        // create an account initialized with the JSON from the req body 
        Account account = objectMapper.readValue(ctx.body(), Account.class);
        Account verifiedLoginAccount = accountService.login(account);

        if(verifiedLoginAccount != null){
            ctx.json(objectMapper.writeValueAsString(verifiedLoginAccount));
        } else {
            ctx.status(401);
        }

    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addMessage = messageService.createNewMessage(message);
        if(addMessage != null){
            ctx.json(mapper.writeValueAsString(addMessage)); // status = 200 OK
        }else{
            ctx.status(400);
        }
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        Integer messageId = Integer.valueOf(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId); 
        if(message != null){
            ctx.json(objectMapper.writeValueAsString(message));
        }else{
            ctx.status(200);
            ctx.result("");
        }
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException{
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException{
        Integer messageId = Integer.valueOf(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId);
        if(deletedMessage != null){
            ctx.json(deletedMessage);
        }else{
            ctx.status(200);
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message jsonBodyMessage  = objectMapper.readValue(ctx.body(), Message.class);
        String replacementText = jsonBodyMessage.getMessage_text(); 

        Message updatedMessage = messageService.updateMessage(messageId, replacementText);
        if (updatedMessage != null) {
            ctx.json(updatedMessage);
        } else {
            ctx.status(400); // Invalid message text or message ID doesn't exist
        }
    }

    private void getUserMessagesHandler(Context ctx) throws JsonProcessingException{
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List <Message> userMessages = messageService.getUserMessages(accountId);
        ctx.json(userMessages);
    }
}