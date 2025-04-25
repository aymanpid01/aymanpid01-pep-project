package Service;
import Model.Account;
import DAO.AccountDao;



public class AccountService {
    private AccountDao accountDao;

    public AccountService(){
        accountDao = new AccountDao();   
    }
    public AccountService(AccountDao accountDao){
        this.accountDao = accountDao;
    }

/* 
 * TODO: User registration 
 * @param account - the account to register
 * @return the created account with account_id or
 *  null if registration fails 
 */

 public Account register(Account account){
   // checking if user is blank
    String username = account.getUsername();
    if (username == null || username.isBlank() ) {
        return null;
    }
    // checking if password is 4 chars or more
    
    String password = account.getPassword();
    if (password == null || password.length() < 4 ){
        return null;
    }

    if(accountDao.getAccountByUsername(username) != null){
        return null;
    } 
    return accountDao.createAccount(account);
 }

 // TODO login
 public Account login(Account account){
    String username = account.getUsername();
    String password = account.getPassword();
    if(username == null || username.isBlank() || password == null || password.isBlank()){
        return null;
    }

    return accountDao.getAccountByCreds(username, password);
 }

}
