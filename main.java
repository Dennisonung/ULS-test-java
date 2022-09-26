import java.util.Scanner;
import java.io.*; 
import java.nio.charset.*;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.apache.commons.io.*;
import at.favre.lib.crypto.bcrypt.*;

class Main {
  
  public static void main(String[] args) throws IOException{
    main2();
  }

  public static void main2() throws IOException {
    File tempFile = new File("./users/0.json");
    if(tempFile.exists()) {
      tempFile.delete();
    }
    Scanner input = new Scanner(System.in);
    System.out.println("Enter the name of the file you want to read:\n1. Login\n2. Register");
    int option = input.nextInt();
    if (option == 1) {
      UserLogin();
    } else if(option == 2) {
      UserCreate();
    } else {
      input.close();
      System.out.println("Invalid option");
      main2();
    }
  } 

  public static void UserLogin() throws IOException {
    Scanner input2 = new Scanner(System.in);
    System.out.println("Enter your username: ");
    String username = input2.nextLine();
    File tempFile = new File("./users/" + username + ".json");
    if (!tempFile.exists()) {
      System.out.println("User does not exist");
      input2.close();
      main2();
    }
    System.out.println("Enter your password: ");
    File FiletoUser = new File("./users/" + username + ".json");
    String ActualFileForUser = FileUtils.readFileToString(FiletoUser, StandardCharsets.US_ASCII);
    JSONTokener tokener = new JSONTokener(ActualFileForUser);
    JSONObject object = new JSONObject(tokener);
    BCrypt.Result result = BCrypt.verifyer().verify(input2.nextLine().toString().intern().toCharArray(), object.getString("password").toString().intern());
    if (result.verified) {
      System.out.println("Success you've logged in");
    } else {
      System.out.println("Password Failed, \nPlease Try Again");
    }
    // String PasswordEntered = input2.nextLine();
    // String PasswordGotten = object.getString("password");
    // if (PasswordGotten.toString().intern() == PasswordEntered.toString().intern()) {
    //   System.out.println("Success you've logged in");
    // } else {
      // System.out.println(PasswordEntered.getClass().getName());
      // System.out.println("Password Failed, \n Please Try Again");
    // }
  }


  public static void UserCreate() throws IOException {
    Scanner input3 = new Scanner(System.in);
    System.out.println("Enter 0 to go back.\nNew Username: ");
    String Username = input3.nextLine();
    if (Username == "0") {
      input3.close();
      main2();
    }
    File tempFile = new File("./users/" + Username + ".json");
    if (tempFile.exists()) {
      System.out.println("Username is taken.");
      input3.close();
      UserCreate();
    }
    System.out.println("New Password: ");
    JSONObject NewUser = new JSONObject();
    NewUser.put("password", BCrypt.withDefaults().hashToString(12, input3.nextLine().toCharArray()));
    //NewUser.put("password",input3.nextLine());
    
    try (PrintWriter out = new PrintWriter(new FileWriter("./users/" + Username + ".json"))) {
      out.write(NewUser.toString());
    } catch (Exception e) {
        e.printStackTrace();
    }
    input3.close();

  }
}