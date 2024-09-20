package jdbcBankingsystem;
import java.math.BigDecimal;
import java.util.Scanner;
import java.sql.*;


public class AccountManager {
    private Connection connect;
    private Scanner scanner;
    AccountManager(Connection connect, Scanner scanner){
        this.connect=connect;
        this.scanner=scanner;
    }
    public void credit_money(long account_number)throws SQLException{
        scanner.nextLine();
        System.out.println("Enter Amount ");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security pin");
        String security_pin=scanner.nextLine();
        try{
            connect.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement=connect.prepareStatement("SELECT * FROM accounts where account_number=?  and security_pin=?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet=preparedStatement.executeQuery();
                if(resultSet.next()){
                    String credit_query="UPDATE accounts SET balance = balance+? where account_number=?";
                    PreparedStatement preparedStatement1=connect.prepareStatement(credit_query);
                    preparedStatement.setDouble(1,amount);
                    preparedStatement.setLong(2,account_number);
                    int rowAffected= preparedStatement.executeUpdate();
                    if(rowAffected>0){
                        System.out.println("Rs" +amount+"credited Succesfully");
                        connect.commit();
                        connect.setAutoCommit(true);
                        return;

                    }else{
                        System.out.println("transcation failed");
                        connect.rollback();
                        connect.setAutoCommit(true);
                    }
                }
                else{
                    System.out.println("Invalid security pin");
                }

            }
            }catch (SQLException e){
            e.printStackTrace();
        }
        connect.setAutoCommit(true);
    }
    public void get_Balance(long account_number){
        scanner.nextLine();
        System.out.println("Enter Security pin");
        String security_pin=scanner.nextLine();
        try{
            PreparedStatement preparedStatement=connect.prepareStatement("SELECT balance FROM accounts where account_number=? AND security_pin=?");
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,security_pin);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance=resultSet.getDouble("balance");
                System.out.println("Balance"+balance);
            }else{
                System.out.println("Invalid pin");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void debit_money(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.println("Enter amount");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin");
        String security_pin=scanner.nextLine();
        try{
            connect.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement=connect.prepareStatement("SELECT * FROM accounts where account_number=? and security_pin=?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet=preparedStatement.executeQuery();
                if(resultSet.next()){
                    double current_balance=resultSet.getDouble("balance");
                    if(amount<=current_balance){
                        String debit_query="UPDATE accounts SET balance=balance-? where account_number=?";
                        PreparedStatement preparedStatement1=connect.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);
                        int rowsAffected=preparedStatement1.executeUpdate();
                        if(rowsAffected>0){
                            System.out.println("Rs."+amount+"debited Successfully");
                            connect.commit();
                            connect.setAutoCommit(true);
                            return;

                        }else{
                            System.out.println("Transcation failed");
                            connect.rollback();
                            connect.setAutoCommit(true);
                        }
                    }
                    else{
                        System.out.println("Insufficenet balance");

                    }
                }else{
                    System.out.println("Invalid pin");
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        connect.setAutoCommit(true);
    }
public void transfer_money(long sender_account_number) throws SQLException{
        scanner.nextLine();
    System.out.println("Enter Recevier Account number");
    long receiver_account_number=scanner.nextLong();
    System.out.println("Enter Amount");
    double amount=scanner.nextDouble();
    scanner.nextLine();
    System.out.println("Enter Secuiryt pin");
    String securiyt_pin=scanner.nextLine();
    try{
        connect.setAutoCommit(false);
        if(sender_account_number!=0 && receiver_account_number!=0){
            PreparedStatement preparedStatement=connect.prepareStatement("SELECT * from accounts where account_number=? AND security_pin=?");
            preparedStatement.setLong(1,sender_account_number);
            preparedStatement.setString(2,securiyt_pin);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                double current_balance=resultSet.getDouble("balance");
                if(amount<=current_balance){
                    String debit_query="UPDATE accounts SET balance=balance-? where account_number=?";
                    String credit_query="UPDATE accounts SET balance=balance+? where account_number=?";
                    PreparedStatement creditPreparedStatement=connect.prepareStatement(credit_query);
                    PreparedStatement debitPreparedStatement=connect.prepareStatement(debit_query);
                    creditPreparedStatement.setDouble(1,amount);
                    creditPreparedStatement.setLong(2,receiver_account_number);
                    debitPreparedStatement.setDouble(1,amount);
                    debitPreparedStatement.setLong(2,sender_account_number);
                    int rowsAffected1=debitPreparedStatement.executeUpdate();
                    int rowsAffected2=creditPreparedStatement.executeUpdate();
                    if(rowsAffected2>0 && rowsAffected1>0){
                        System.out.println("Transaction Successful");
                        System.out.println("Rs"+amount+"Transfereed Successfully");
                        connect.commit();
                        connect.setAutoCommit(true);
                        return ;
                    }else{
                        System.out.println("Transaction Failed");
                        connect.rollback();
                        connect.setAutoCommit(true);
                    }
                }
                else{
                    System.out.println("Insufficent balance");
                }
            }
            else{
                System.out.println("invalid security pin");
            }
        }
        else{
            System.out.println("Invalid account_number");
        }
    }catch(SQLException e){
        e.printStackTrace();
    }
       connect.setAutoCommit(true);
}

}
