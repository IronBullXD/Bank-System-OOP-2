import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Menu {

    private Bank bank;
    private Scanner scanner = new Scanner(System.in);
    private AccountAuthentication auth = new AccountAuthentication();
    private TransactionHistoryService historyService;

    //Default pass ni siya and user for the "Assume database"
    private User currentUser = new User("admin", "password123");

    public Menu(Bank bank) {
        this.bank = bank;
        this.historyService = bank.getTransactionHistoryService();
    }

    public void start() {
        
        if (!loginPrompt()) {
            System.out.println("Authentication failed. Exiting system...");
            return; 
        }

        int choice;
        do {
            System.out.println("\n===== BANKING SYSTEM =====");
            System.out.println("1. Create Savings Account");
            System.out.println("2. Create Current Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. View Accounts");
            System.out.println("7. Generate Bank Statement");
            System.out.println("8. Transaction History");
            System.out.println("9. Exit");
            System.out.print("Choose option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    createSavings();
                    break;
                case 2:
                    createCurrent();
                    break;
                case 3:
                    deposit();
                    break;
                case 4:
                    withdraw();
                    break;
                case 5:
                    transfer();
                    break;
                case 6:
                    bank.showAllAccounts();
                    break;
                case 7:
                    generateStatement();
                    break;
                case 8:
                    transactionHistoryMenu();
                    break;
                case 9:
                    System.out.println("Logging out... Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 9);
    }

    private boolean loginPrompt() {
        System.out.println("\n===== SYSTEM LOGIN =====");
        System.out.print("Username: ");
        String inputUsername = scanner.nextLine();
        
        System.out.print("Password: ");
        String inputPassword = scanner.nextLine();

        //AccountAuthentication  to verify 
        if (auth.login(currentUser, inputUsername, inputPassword)) {
            System.out.println("Login successful! Welcome, " + currentUser.getUsername() + ".");
            return true;
        } else {
            System.out.println("Invalid username or password.");
            return false;
        }
    }

    private void generateStatement() {
        System.out.print("Account Number: ");
        String accNo = scanner.nextLine();
        bank.generateStatement(accNo);
    }

    private void createSavings() {
        System.out.print("Account Number: ");
        String accNo = scanner.nextLine();
        System.out.print("Account Holder: ");
        String holder = scanner.nextLine();
        System.out.print("Initial Balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine(); 
        bank.addAccount(new SavingsAccount(accNo, holder, balance));
    }

    private void createCurrent() {
        System.out.print("Account Number: ");
        String accNo = scanner.nextLine();
        System.out.print("Account Holder: ");
        String holder = scanner.nextLine();
        System.out.print("Initial Balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine(); 

        bank.addAccount(new CurrentAccount(accNo, holder, balance));
    }

    private void deposit() {
        System.out.print("Account Number: ");
        String accNo = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); 
        Account account = bank.findAccount(accNo);
        if (account != null) {
            bank.deposit(account, amount);
        } else {
            System.out.println("Account not found.");
        }
    }

    private void withdraw() {
        System.out.print("Account Number: ");
        String accNo = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        Account account = bank.findAccount(accNo);
        if (account != null) {
            bank.withdraw(account, amount);
        } else {
            System.out.println("Account not found.");
        }
    }

    private void transfer() {
        System.out.print("From Account: ");
        String from = scanner.nextLine();
        System.out.print("To Account: ");
        String to = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        Account fromAccount = bank.findAccount(from);
        Account toAccount = bank.findAccount(to);
        if (fromAccount != null && toAccount != null) {
            bank.transfer(fromAccount, toAccount, amount);
        } else {
            System.out.println("Invalid account number.");
        }
    }

    private void transactionHistoryMenu() {
        int choice;
        do {
            System.out.println("\n===== TRANSACTION HISTORY =====");
            System.out.println("1. View All Transactions");
            System.out.println("2. View By Account Number");
            System.out.println("3. Filter Transactions");
            System.out.println("4. Back");
            System.out.print("Choose option: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    historyService.showAllTransactions();
                    break;
                case 2:
                    viewByAccountNumber();
                    break;
                case 3:
                    filterTransactionsMenu();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 4);
    }

    private void viewByAccountNumber() {
        System.out.print("Account Number: ");
        String accNo = scanner.nextLine();
        historyService.showTransactionsByAccount(accNo);
    }

    private void filterTransactionsMenu() {
        int choice;
        do {
            System.out.println("\n===== FILTER TRANSACTIONS =====");
            System.out.println("1. By Type");
            System.out.println("2. By Date Range");
            System.out.println("3. By Minimum Amount");
            System.out.println("4. Back");
            System.out.print("Choose option: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    filterByType();
                    break;
                case 2:
                    filterByDateRange();
                    break;
                case 3:
                    filterByMinimumAmount();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 4);
    }

    private void filterByType() {
        System.out.print("Enter type (Deposit/Withdraw/Transfer): ");
        String type = scanner.nextLine();
        historyService.showTransactionsByType(type);
    }

    private void filterByDateRange() {
        try {
            System.out.print("From date (YYYY-MM-DD): ");
            LocalDate from = LocalDate.parse(scanner.nextLine());
            System.out.print("To date (YYYY-MM-DD): ");
            LocalDate to = LocalDate.parse(scanner.nextLine());

            if (to.isBefore(from)) {
                System.out.println("Invalid date range.");
                return;
            }

            historyService.showTransactionsByDateRange(from, to);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
        }
    }

    private void filterByMinimumAmount() {
        System.out.print("Minimum amount: ");
        double minAmount = scanner.nextDouble();
        scanner.nextLine();
        historyService.showTransactionsByMinAmount(minAmount);
    }
}
