package jdbcBankingsystem;
import java.sql.*;
import java.util.Scanner;
import static java.lang.Class.forName;



public class Bankingapp {
    private static final String url = "jdbc:mysql://localhost:3306/bankingsystem";
    private static final String username = "root";
    private static final String password = "";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try () {
            Connection connect = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);
            User user = new User(connect, scanner);
            Accounts accounts = new Accounts(connect, scanner);
            AccountManager accountManager = new AccountManager(connect, scanner);
            String email;
            long account_number;
            while (true) {
                System.out.println("Welcome to banking system");
                System.out.println();
                System.out.println("1.Register");
                System.out.println("2.Login");
                System.out.println("3.Exit");
                System.out.println("Enter your chocie");
                int choice1 = scanner.nextInt();
                switch (choice1) {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("User logged in");
                            if (!accounts.account_exist(email)) {
                                System.out.println();
                                System.out.println("1.Open a new Bank account");
                                System.out.println("2.Exit");
                                if (scanner.nextInt() == 1) {
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account created successfully");
                                    System.out.println("Your account number is" + account_number);

                                } else {
                                    break;
                                }
                            }
                            account_number = accounts.getAccount_number(email);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1 Debit Money");
                                System.out.println("2 Credit Money");
                                System.out.println("3 Transfer money");
                                System.out.println("4 check balance");
                                System.out.println("5 log out");
                                System.out.println("Enter your choice");
                                choice2 = scanner.nextInt();
                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter valid choice");
                                        break;

                                }


                            }
                        } else {
                            System.out.println("Incorrect Email or password");
                        }
                    case 3:
                        System.out.println("Thanku for using banking system");
                        System.out.println("Closing app");
                        return;

                    default:
                        System.out.println("Enter valid choice");
                        break;
                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}