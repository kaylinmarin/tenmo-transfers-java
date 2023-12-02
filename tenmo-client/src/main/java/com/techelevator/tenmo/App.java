package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.Arrays;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final UserService userService = new UserService();
    private final TransferService transferService = new TransferService(accountService);

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
        else {
            // user has just logged in, set auth tokens on services
            accountService.setAuthToken(currentUser.getToken());
            transferService.setAuthToken(currentUser.getToken());
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
            } else if (menuSelection == 6) {
                approveTransfer();
            } else if  (menuSelection == 7) {
                rejectTransfer();
            }else if (menuSelection == 0) {
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
        int currentUserId = currentUser.getUser().getId();
        int currentAccountId = accountService.getAccountByUserId(currentUserId).getAccount_id();
        Transfer[] completedTransfers = transferService.getCompletedTransfers(currentAccountId);
        consoleService.printTransfers(completedTransfers);



	}

    private Transfer[] getPendingTransfers() {
        int currentUserId = currentUser.getUser().getId();
        int currentAccountId = accountService.getAccountByUserId(currentUserId).getAccount_id();
        Transfer[] transfers = transferService.getPendingTransfers(currentAccountId);
        return transfers;
    }

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
        Transfer[] transfers = getPendingTransfers();
        consoleService.printTransfers(transfers);
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        User[] users = userService.getAllUsers();
        consoleService.printUsers(userService.getAllUsers());

        int selectedUserId = consoleService.promptForInt("Enter Id of user you are sending to (Select 0 to cancel): ");
        if (selectedUserId == 0) {
           return;
        }
         if (selectedUserId == currentUser.getUser().getId()) {
           System.out.println("Invalid selection: Cannot transfer to yourself.");
           return;
         }

         Account targetAccount = accountService.getAccountByUserId(selectedUserId);
         if (targetAccount == null) {
           System.out.println("Invalid user id.");
           return;
         }

         // notes on BigDecimal.compareTo(BigDecimal):
        // BigDecimal cannot use conventional comparison operators (<, <=, ==, >, >=)
        // so, we have to use compareTo
        // compareTo is an oldschool comparison function
        // the way these work, is they have two values, left and right, that they compare
        // left is the value you called compareTo on, right is the value you passed to compareTo
        // compareTo returns one of three numbers, indicating the result of the comparison
        // -1 means the left number was LESS THAN the right number
        // 0 means the left number was EQUAL TO the right number
        // 1 means the left number was GREATER THAN the right number
        // so, for a comparison like >=, you would need to check if the result is equal to 0 or 1
        // alternatively, test the opposite; check if the result is not equal to -1
         BigDecimal amount = consoleService.promptForBigDecimal("Enter the amount you would like to send $");

         if (amount.compareTo(new BigDecimal("0")) == -1 || amount.compareTo(new BigDecimal("0")) == 0 ) {
             System.out.println("Invalid amount. Must be more than $0.00");
             return;
         }
        Account sourceAccount = accountService.getAccountByUserId(currentUser.getUser().getId());
        if (sourceAccount.getBalance().compareTo(amount) == -1 ) {
            System.out.println("Insufficient funds to transfer.");
        }

         Transfer transfer = new Transfer();
         transfer.setTypeId(2);
         transfer.setFromAccountId(sourceAccount.getAccount_id());
         transfer.setToAccountId(targetAccount.getAccount_id());
         transfer.setAmount(amount);
         transfer.setStatusId(2);

         Transfer result = transferService.create(transfer);
         if (result == null) {
             System.out.println("Transfer rejected.");
         }
         else System.out.println("Transfer successful.");


         //boolean if successful print approved
         //if not, print rejected

	}

	private void requestBucks() {
		// TODO Auto-generated method stub
        User[] users = userService.getAllUsers();
        consoleService.printUsers(userService.getAllUsers());

        int selectedUserId = consoleService.promptForInt("Enter Id of user you would like to request from (Select 0 to cancel): ");
        if (selectedUserId == 0) {
            return;
        }

        if (selectedUserId == currentUser.getUser().getId()) {
            System.out.println("Invalid selection: Cannot transfer to yourself.");
            return;
        }
        if (accountService.getAccountByUserId(selectedUserId) == null) {
            System.out.println("Invalid user id.");
            return;
        }

        BigDecimal amount = consoleService.promptForBigDecimal("Enter the amount you would like to request: $");
        if (amount.compareTo(new BigDecimal("0")) == -1 || amount.compareTo(new BigDecimal("0")) == 0 ) {
            System.out.println("Invalid amount. Must be more than $0.00");
            return;
        }
        Account sourceAccount = accountService.getAccountByUserId(selectedUserId);
        if (sourceAccount == null) {
            System.out.println("Invalid user id.");
            return;
        }
        Account targetAccount = accountService.getAccountByUserId(currentUser.getUser().getId());


        // fetch from and to accounts using account service and user ids

         Transfer transfer = new Transfer();
         transfer.setTypeId(1);
         transfer.setFromAccountId(sourceAccount.getAccount_id());
         transfer.setToAccountId(targetAccount.getAccount_id());
         transfer.setAmount(amount);
         transfer.setStatusId(1);

        Transfer result = transferService.create(transfer);
        if (result == null) {
            System.out.println("Transfer rejected.");
        }
        else System.out.println("Transfer pending.");

    }
    private void approveTransfer() {
        viewPendingRequests();
        int transferId = consoleService.promptForInt("Please select the request id you would like to approve: ");
        Transfer[] validTransfers = getPendingTransfers();
        boolean isValid = false;
        // write a loop over the valid transfers
        for (Transfer validTransfer: validTransfers) {
            //  if transfer id matches the valid transfer id, set is valid to true
            if (transferId == validTransfer.getId()) {
                isValid = true;
            }
        }
        // after loop: if is valid is false, exit
        if(!isValid) {
            System.out.println("Invalid id. Please make another selection.");
            return;
        }
        // otherwise we can make the transfer approval call
        boolean wasSuccessful = transferService.approveTransfer(transferId);
        if (wasSuccessful) {
            System.out.println("Request approved!");
        }
        else {
            System.out.println("Request failed");
        }
    }
    private void rejectTransfer() {
        viewPendingRequests();
        int transferId = consoleService.promptForInt("Please select the request id you would like to reject: ");
        Transfer[] validTransfers = getPendingTransfers();
        boolean isValid = false;
        // write a loop over the valid transfers
        for (Transfer validTransfer: validTransfers) {
            //  if transfer id matches the valid transfer id, set is valid to true
            if (transferId == validTransfer.getId()) {
                isValid = true;
            }
        }

        // after loop: if is valid is false, exit
        if(!isValid) {
            System.out.println("Invalid id. Please make another selection.");
            return;
        }

        // otherwise we can make the transfer approval call
        boolean wasSuccessful = transferService.rejectTransfer(transferId);
        if (wasSuccessful) {
            System.out.println("Request rejected!");
        }
        else {
            System.out.println("Request failed");
        }
    }

}
