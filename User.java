package jdbcBankingsystem;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connect;
    private Scanner scanner;

    public User(Connection connect,Scanner scanner){
        this.connect=connect;
        this.scanner=scanner;
    }
    public void register(){
        scanner.nextLine();
        System.out.println("Full Name");
        String full_name=scanner.nextLine();
        System.out.println("EMail");
        String email=scanner.nextLine();
        System.out.println("PAssword");
        String password=scanner.nextLine();
        if(user_exist(email)){
            System.out.println("User already exists for this email");
            return;
        }
        String register_query="INSERT INTO user(full_name,email,password) VALUES(?,?,?) ";
        try{
            PreparedStatement preparedStatement= connect.prepareStatement(register_query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int affectedRows=preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Registration Successful");
            }else{
                System.out.println("Registration failed");
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }
    public String login(){
        scanner.nextLine();
        System.out.println("Email");
        String email=scanner.nextLine();
        System.out.println("Password");
        String password=scanner.nextLine();
        String login_query="SELECT * FROM user where email= ? AND password= ?";
        try{
            PreparedStatement preparedStatement=connect.prepareStatement(login_query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return email;
            }else{
                return null;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean user_exist(String email){
        String query="SELECT * FROM user WHERE email=?";
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
