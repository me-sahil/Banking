import java.util.*;
import java.time.*;
import java.sql.*;

public class bank {
    public static void main(String args[]){
    Scanner sc= new Scanner(System.in);
    int c=1000;
    Hashtable<Integer, String> cname = new Hashtable<>();
    Hashtable<Integer, String> cage = new Hashtable<>(); //For DOB
    Hashtable<Integer, Integer> cdeposit = new Hashtable<>();
    Hashtable<Integer, String> cacctype = new Hashtable<>();
    Hashtable<Integer, Integer> cageyrs = new Hashtable<>(); //For Age (Automatically Calculated As per DOB)
    Hashtable<Integer, Integer> cinterest = new Hashtable<>();
    Hashtable<Integer, String> cdateofopen = new Hashtable<>(); //Date of Account Opening



        
    try{
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("\nConnecting to Online Bank Services....");
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/19bcs3867", "root", "");
        System.out.println("\nData Updated Successfully....");
        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM bank";
        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()){
            //Retrieving data
            c= rs.getInt("CustomerID");
            String name = rs.getString("CustomerName");
            String age = rs.getString("DOB");
            int blnc = rs.getInt("Balance");
            String acty = rs.getString("AccountType");
            int agee = rs.getInt("Age");
            int intrst = rs.getInt("Interest");
            String accopen = rs.getString("DateOfAccountOpening");
   
            //Initialize Existing Data to Program
            cname.put(c, name );
            cage.put(c, age );
            cdeposit.put(c, blnc );
            cacctype.put(c,acty);
            cageyrs.put(c,agee);
            cinterest.put(c,intrst);
            cdateofopen.put(c,accopen);
         }
         
         rs.close();        
        

    }catch(Exception e){
        System.out.println(e);
    }







    while(true){
        System.out.println("\n\n<<<<<---MENU--->>>>>");
        System.out.println("1. Add Customer "+"\n2. Store Records To Table/Database"+"\n3. Withdrawal "+"\n4. Deposit "+
        "\n5. Remove Customer"+"\n6. Display Customer Information Based on ID"+"\n7. Exit");
        System.out.print("Enter your choice : ");
        int choice = sc.nextInt();
        System.out.println("");
        String temp_choice;
        boolean user_found;
        LocalDate today = LocalDate.now();
 


        switch(choice){

            // CASE 1 - ADD CUSTOMERS

            case 1:{
                
                do{
                    

                boolean condition = cname.containsKey(c);
                if(condition == true)
                {
                    c++;
                }

                for(int i=c;i<c+1;i++){
                System.out.println("Automatically Generated Account Number :-"+ c);
                System.out.println("Customer Name :");
                String name = sc.nextLine();
                name+=sc.nextLine();
                System.out.println("DOB (DD/MM/YYYY) :");
                try{
                System.out.print("DD: ");  int DD =sc.nextInt();
                System.out.print("MM: ");  int MM =sc.nextInt();
                System.out.print("YYYY: ");  int YYYY =sc.nextInt();
                String dob = (DD+"/"+MM+"/"+YYYY);
                 
                System.out.println("Balance :");
                Integer balance = sc.nextInt();
                System.out.println("Account Type :");
                String acctype = sc.nextLine();
                acctype+=sc.nextLine();

                LocalDate dateofbirth = LocalDate.of(YYYY,MM,DD);
                int ageinyears = Period.between(dateofbirth, today).getYears();
                
                Integer interest = (balance*6)/100;
                LocalDate localD= LocalDate.now();            

                cname.put(c, name );
                cage.put(c, dob );
                cdeposit.put(c, balance );
                cacctype.put(c,acctype);
                cageyrs.put(c,ageinyears);
                cinterest.put(c,interest);
                cdateofopen.put(c,localD.toString());

                System.out.println("Added Successfully!!! \nAccount ID : "+c+"\nPlease save it for future reference....\n");
            }
            catch(Exception e){
                System.out.println("Please Check Values and Try Again Later !!!");
                System.exit(0);
            }
                
                }
                 
                System.out.println("Do you want to add More Customers? (yes/no)");
                    temp_choice = sc.next();
                    temp_choice.toLowerCase();
                if(temp_choice.equals("yes")||temp_choice.equals("y")){
                    System.out.println("\nAdding more Customers\n\n");
                }
                else if(temp_choice.equals("no")||temp_choice.equals("n")){
                    System.out.println("\n Leaving to Main Menu.......\n\n");
                    break;
                }         
                else{
                    System.out.println("\nPlease Choose Valid Option.....\nLeaving to Main Menu");
                    break;
                }     


            }
            while(temp_choice.equals("yes") || temp_choice.equals("y"));
            c++;
            }
            break;


            // CASE 2 - SAVE RECORDS TO TABLE

            case 2:{
               
               
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    System.out.println("Connecting to Online Bank Services....");
                    Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/19bcs3867", "root", "");
                    System.out.println("\nData Updated Successfully....");
                    String clrtable =("TRUNCATE TABLE bank");
                    PreparedStatement clrt=con.prepareStatement(clrtable);
                    clrt.execute();
                    for(int j=0;j<=c;j++){
                        if(cname.containsKey(j)){
                    String n = cname.get(j);
                    String ag = cage.get(j);
                    Integer dp = cdeposit.get(j);
                    String act =cacctype.get(j);
                    Integer ageyrs = cageyrs.get(j);
                    Integer intr = cinterest.get(j);
                    String lcldate = cdateofopen.get(j);
                    String stmnt = ("INSERT INTO bank (CustomerID, CustomerName, DOB, Balance, AccountType, Age, Interest, DateOfAccountOpening)"+"\n"+
                     "VALUES ("+j+", "+"'"+n+"', '"+ag+"', "+dp+", "+"'"+act+"',"+ageyrs+", "+intr+", '"+lcldate+"');");
                    PreparedStatement st=con.prepareStatement(stmnt);
                    st.execute();
                }}
                    con.close();
                    
                    

                }catch(Exception e){
                    System.out.println(e);
                }




            }
            break;


            // CASE 3 - TO WITHDRAWAL BALANCE

            case 3:{
                System.out.print("Enter Customer ID : ");
                Integer id = sc.nextInt();
                user_found = cname.containsKey(id);
                if(user_found==true){
                System.out.println("Customer Name : " + cname.get(id));
                System.out.println("Current Balance : " + cdeposit.get(id));
                System.out.print("Enter Amount to Withdrawal : ");
                Integer w = sc.nextInt();
                Integer x = cdeposit.get(id);
                if(x >= w){
                    System.out.println("Withdrawl Successfully!!!");
                    Integer amount = x - w;
                    System.out.println("Balance Left in Account : " + amount);
                    Integer interest = (amount*6)/100;
                    cinterest.put(id,interest);
                    cdeposit.put(id,amount);
                }
                else{
                    System.out.println("Unsufficient Amount to Withdrawl");
                }

                System.out.println("Do You Want To Update It TO Database ?? (yes/no)");
                temp_choice = sc.next();
                temp_choice.toLowerCase();
                if(temp_choice.equals("yes") || temp_choice.equals("y")){
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    System.out.println("Connecting to Online Bank Services....");
                    Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/19bcs3867", "root", "");
                    System.out.println("\nData Updated Successfully....");
                    if(cname.containsKey(id)){
                    Integer dp = cdeposit.get(id);
                    String stmnt = ("UPDATE bank SET Balance="+dp+"\n"+
                     "WHERE CustomerID="+id);
                    PreparedStatement st=con.prepareStatement(stmnt);
                    st.execute();
                }
                    con.close();
                                       

                }catch(Exception e){
                    System.out.println(e);
                } break;}
                else if(temp_choice.equals("no") || temp_choice.equals("n")){
                    System.out.println("Not Updating to the DataBase"); break;
                }
                else{
                    System.out.println("Please Choose a coorect option... Not Updated To Database....");
                    break;
                }




            }
            else{
                System.out.println("No user with this Account ID was Found");
            }


            }
            break;


            // CASE 4 - TO DEPOSIT BALANCE

            case 4:{
                System.out.print("Enter Customer ID : ");
                Integer id = sc.nextInt();
                user_found = cname.containsKey(id);
                if(user_found==true){

                System.out.println("Customer Name : " + cname.get(id));
                System.out.println("Current Balance : " + cdeposit.get(id));
                System.out.print("Enter Amount to Deposit : ");
                Integer w = sc.nextInt();
                Integer x = cdeposit.get(id);
                Integer amount = w+x;
                System.out.println(" Updated Balance in Account : " + amount);
                cdeposit.put(id,amount);
                Integer interest = (amount*6)/100;
                cinterest.put(id,interest);

                System.out.println("Do You Want To Update It TO Database ?? (yes/no)");
                temp_choice = sc.next();
                temp_choice.toLowerCase();
                if(temp_choice.equals("yes") || temp_choice.equals("y")){
                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        System.out.println("Connecting to Online Bank Services....");
                        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/19bcs3867", "root", "");
                        System.out.println("\nData Updated Successfully....");
                        if(cname.containsKey(id)){
                        Integer dp = cdeposit.get(id);
                        String stmnt = ("UPDATE bank SET Balance="+dp+"\n"+
                         "WHERE CustomerID="+id);
                        PreparedStatement st=con.prepareStatement(stmnt);
                        st.execute();
                    }
                        con.close();
                                           
    
                    }catch(Exception e){
                        System.out.println(e);
                    } break;}
                    else if(temp_choice.equals("no") || temp_choice.equals("n")){
                        System.out.println("Not Updating to the DataBase"); break;
                    }
                    else{
                        System.out.println("Please Choose a coorect option... Not Updated To Database....");
                    break;
                    }


            }
            else{
                System.out.println("No user with this Account ID was Found");
            }



            }
            break;


            // CASE 5 - TO REMOVE CUSTOMERS

            case 5:{
                System.out.print("Enter ID of CUstomer to Remove : ");
                Integer id = sc.nextInt();
                user_found = cname.containsKey(id);
                if(user_found==true){
                cname.remove(id);
                cage.remove(id);
                cdeposit.remove(id);
                cacctype.remove(id);
                cageyrs.remove(id);
                cinterest.remove(id);
                cdateofopen.remove(id);
                System.out.println("User with Account ID "+id+" was removed Successfully");

                    
                System.out.println("Do You Want To Update It TO Database ?? (yes/no)");
                temp_choice = sc.next();
                temp_choice.toLowerCase();
                if(temp_choice.equals("yes") || temp_choice.equals("y")){
                    try{
                    Class.forName("com.mysql.jdbc.Driver");
                    System.out.println("Connecting to Online Bank Services....");
                    Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/19bcs3867", "root", "");
                    System.out.println("\nData Updated Successfully....");
                    String clrtable =("TRUNCATE TABLE bank");
                    PreparedStatement clrt=con.prepareStatement(clrtable);
                    clrt.execute();
                    for(int j=0;j<=c;j++){
                        if(cname.containsKey(j)){
                    String n = cname.get(j);
                    String ag = cage.get(j);
                    Integer dp = cdeposit.get(j);
                    String act =cacctype.get(j);
                    Integer ageyrs = cageyrs.get(j);
                    Integer intr = cinterest.get(j);
                    String lcldate = cdateofopen.get(j);
                    String stmnt = ("INSERT INTO bank (CustomerID, CustomerName, DOB, Balance, AccountType, Age, Interest, DateOfAccountOpening)"+"\n"+
                    "VALUES ("+j+", "+"'"+n+"', '"+ag+"', "+dp+", "+"'"+act+"',"+ageyrs+", "+intr+", '"+lcldate+"');");
                    PreparedStatement st=con.prepareStatement(stmnt);
                    st.execute();
                }}
                    con.close();
                    
                    

                }catch(Exception e){
                    System.out.println(e);
                } break;}
                    else if(temp_choice.equals("no") || temp_choice.equals("n")){
                        System.out.println("Not Updating to the DataBase"); break;
                    }
                    else{
                        System.out.println("Please Choose a coorect option... Not Updated To Database....");
                    break;
                    }


            }
            
            else{
                System.out.println("No user with this Account ID was Found");
            }

            }
            break;



            case 6:{
                System.out.print("Enter Customer ID : ");
                Integer id = sc.nextInt();
                user_found = cname.containsKey(id);
                if(user_found==true){
                              
                System.out.println("Customer Name : " + cname.get(id) + "\nD.O.B : "+ cage.get(id) 
                +"\nAge : "+cageyrs.get(id)+"\nBalance : "+ cdeposit.get(id)+"\nAccount Type : "+ cacctype.get(id)+
                "\nInterest (6% p.a) : "+cinterest.get(id)+"\nDate of Account Opening ="+cdateofopen.get(id));
            }
            else{
                System.out.println("No user with this Account ID was Found");
            }
            }
            break;

            // CASE 7 - TO EXIT THE MENU

            case 7:{
                System.out.println("Exit Successfully!!!!");
                System.exit(0);
            }

            break;
            default:
            System.out.println("Enter Correct Choice !!!");

        }}} }