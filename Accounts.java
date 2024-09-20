package jdbcBankingsystem;
import javax.xml.transform.Result;
import java.util.*;
import java.sql.*;


public class Accounts {
    private Connection connect;
    private Scanner scanner;
    public Accounts(Connection connect,Scanner scanner){
        this.connect=connect;
        this.scanner=scanner;
    }
    public long open_account(String email){
        if(!account_exist(email)){
            String open_account_query="INSERT INTO accounts(account_number,full_name,email,balance,security_pin) VALUES(?,?,?,?,?) ";
            scanner.nextLine();
            System.out.println("Enter the Full name");
            String full_name=scanner.nextLine();
            System.out.println("Enter initial amount");
            double balance=scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter the security pin ");
            String security_pin=scanner.nextLine();
            try{
                long account_number=generateAccountNumber();
                PreparedStatement preparedStatement=connect.prepareStatement(open_account_query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,full_name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,security_pin);
                int rowsAffected=preparedStatement.executeUpdate();
                if(rowsAffected>0){
                    return account_number;
                }
                else{
                    throw new RuntimeException("Account creation failed");
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account already Exist");
    }
    public long getAccount_number(String email){
        String query="SELECT account_number from accounts where email=?";
        try{
            PreparedStatement preparedStatement=connect.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number Doesn't exist");

    }
    private long generateAccountNumber(){
        try{
            Statement statement=connect.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT account_number from accounts ORDER BY account_number DESC LIMIT 1");
            if(resultSet.next()){
                long last_account_number=resultSet.getLong("account_number");
                return last_account_number+1;
            }else{
                return 10000100;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }
    public boolean account_exist(String email){
        String query="SELECT account_number from accounts where email=?";
        try{
            PreparedStatement preparedStatement=connect.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
