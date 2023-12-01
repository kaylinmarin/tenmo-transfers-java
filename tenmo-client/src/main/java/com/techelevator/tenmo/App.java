package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.UserService;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final UserService userService = new UserService();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }


    //JILL EDITED THIS
	private void viewCurrentBalance() {
        int userId = currentUser.getUser().getId();
        accountService.setAuthToken(currentUser.getToken());
        Account account = accountService.getAccountByUserId(userId);
        System.out.println("The current balance is : $" + account.getBalance());


    // TODO Auto-generated method stub

}


	private void viewTransferHistory() {
		// TODO Auto-generated method stub

	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        User[] users = userService.getAllUsers();
        consoleService.printUsers(userService.getAllUsers());

        int selection = consoleService.promptForInt("Enter Id of user you are sending to (Select 0 to cancel): ");
        if (selection == 0) {
           return;
        }
         if (selection == currentUser.getUser().getId()) {
           System.out.println("Invalid selection: Cannot transfer to yourself.");
           return;
         }
         //everything is catching here. Need to resolve grabbing account by user id
        //maybe I need account Id, not user id. to retrieve the account?
         if (userService.getUserId(selection) == null) {
           System.out.println("Invalid user id.");
           return;
         }

         BigDecimal amount = consoleService.promptForBigDecimal("Enter the amount you would like to send");
         if (amount.compareTo(new BigDecimal("0")) == -1 || amount.compareTo(new BigDecimal("0")) == 0 ) {
             System.out.println("Invalid amount. Must be more than $0.00");
             return;
         }

//         Transfer transfer = new Transfer();
//         transfer.setTypeId(2);
//         transfer.setFromUserId(currentUser.getUser().getId());
//         transfer.setToUserId(selection);
//         transfer.setAmount(amount);

	}

	private void requestBucks() {
		// TODO Auto-generated method stub
        User[] users = userService.getAllUsers();
        consoleService.printUsers(userService.getAllUsers());

        

		
	}

}
