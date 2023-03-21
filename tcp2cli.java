import java.io.*;
import java.util.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tcp2cli{
    public static void main(String[] args) {
        if (args.length!=2){
            System.out.println ("Unexpected format. Introduce: tcp2cli ip_address port_number");
            System.exit(-1);
        }
            
        try{
            InetAddress ip_address = InetAddress.getByName(args[0]);
            int port_number = Integer.parseInt (args[1]);
            Socket socket_cli= new Socket(); //CREATES THE SOCKET
            SocketAddress socket_address=new InetSocketAddress(ip_address,port_number);
            socket_cli.connect(socket_address,15*1000);
            DataOutputStream out_data= new DataOutputStream(socket_cli.getOutputStream());
            DataInputStream in_data= new DataInputStream(socket_cli.getInputStream());

            while(true){
                System.out.print ("Write numbers separated with blanks, press intro to end the message: ");
                Scanner read = new Scanner(System.in);
                String string_message = read.nextLine(); //CREATE AN STRING WITH THE ANSWER OF THE USER
                String[] eachnum_string = string_message.split(" ");

                if((eachnum_string[0].trim()).equals("0"))//Check if the first number is a 0
                    break;

                //SEND
                out_data.writeUTF(string_message);

                //RECEIVE
                
                String accumulator = in_data.readUTF();
                System.out.println("The final value of the accumulator is "+ accumulator);

            }//CLOSE THE WHILE



        } catch (SocketTimeoutException e){
            System.out.println("Timeout has happened");
        }catch (IOException e){

        } catch (Exception e){
            
        }
    }
}