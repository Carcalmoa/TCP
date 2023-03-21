import java.io.*;
import java.util.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tcp1cli{
    public static void main(String[] args) {
        if (args.length!=2){
            System.out.println ("Unexpected format. Introduce: tcp1cli ip_address port_number");
            System.exit(-1);
        }
            
        try{
            InetAddress ip_address = InetAddress.getByName(args[0]);
                    //System.out.println (ip_address);
            int port_number = Integer.parseInt (args[1]);
            Socket socket_cli= new Socket(ip_address,port_number); //CREATES THE SOCKET
            DataOutputStream out_data= new DataOutputStream(socket_cli.getOutputStream());
            DataInputStream in_data= new DataInputStream(socket_cli.getInputStream());

                    //System.out.println (ser_port_number);
            while(true){
                System.out.print ("Write numbers separated with blanks, press intro or '0' to end the message: ");
                Scanner read = new Scanner(System.in);
                String string_message = read.nextLine(); //CREATE AN STRING WITH THE ANSWER OF THE USER
                LinkedList<String> cutted_numbers_list=new LinkedList<String>();
                LinkedList<String> numbers_list = new LinkedList<String>();

                //CREATE A LINKED LIST WITH ALL THE NUMBERS WRITTEN IN THE TERMINAL
                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(string_message);
                while (m.find()) {
                    numbers_list.add(m.group());
                }

                //System.out.println(numbers_list);

                //LOOK IF THE FIRST NUM OF THE LIST IS A 0
                if (Integer.parseInt(numbers_list.get(0)) == 0){ //IF THE FIRST NUMBER IS 0 ENDS
                    break; //EXIT THE WHILE IF THE NUMB IS 0
                   
                }

                //CUT THE ORIGINAL LIST WHEN FIND A 0 AND CREATE A CUTTED VERSION
                for (int i=0;i<numbers_list.size();i++){
                    //System.out.println(numbers_list.get(i));
                    if (Integer.parseInt(numbers_list.get(i)) != 0)
                        cutted_numbers_list.add(numbers_list.get(i));
                    else
                        break;
                }
                //System.out.println("Cutted list size"+cutted_numbers_list.size());

                //CONVERT THE CUTTED LINKEDLIST INTO BYTE[] FOR SEND 
                String numbers_str = ("");
                for (int j=0;j<cutted_numbers_list.size();j++){
                    int eachnum_int = Integer.parseInt(cutted_numbers_list.get(j)); //CONVERT EACH NUMBER OF THE CUTTED_LIST IN INT
                    //System.out.println(eachnum_int);
                    numbers_str += eachnum_int+" ";//ADD TO AN EMPTY STRING ALL THE NUMBERS ONE BY ONE
                    //System.out.println("String enviada"+numbers_str);
                }
                //byte[] message = new byte[numbers_str.length()];
                //message=numbers_str.getBytes();// CONVERT THE STRING INTO A BYTE[]

                //SEND
               
                out_data.writeUTF(numbers_str);


                //RECEIVE
                //freeport.setSoTimeout(10000);//THE CLIENT WAITS A MAX OF 10 SEC FOR RECEIVING AN ANSWER FROM THE SERVER
                //byte[] result = new byte[1000];
                String accumulator = in_data.readUTF();
                System.out.println("The final value of the accumulator is "+ accumulator);

            }//CLOSE THE WHILE
                //socket_cli.close();
            
            

        } catch (SocketException e){

        } catch (SocketTimeoutException e){
            System.out.println("Timeout has happened");
        }catch (IOException e){

        }   
    
    }
}