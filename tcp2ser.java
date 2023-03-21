import java.io.*;
import java.util.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tcp2ser{
    public static void main(String[] args) {
        if (args.length!=1){
            System.out.println ("Unexpected format. Introduce: tcp2ser port_number");
            System.exit(-1);
        }
        try{
            int port = Integer.parseInt (args[0]); 
            ServerSocket socket_ser = new ServerSocket(port);//CONNECT TO THE PORT RECEIVED BY ARG  
            while(true){//FOR ACCEPTING NEW CLIENTS ALL THE TIME
                Socket socket_received=socket_ser.accept();
                System.out.println("New Client is connected");
                ClientConnection c= new ClientConnection(socket_received);
                Thread t= new Thread(c);
                t.start();
            }
        }catch (Exception e){
        } 
    }


    static class ClientConnection implements Runnable{
        Socket socket_cli;
        ClientConnection(Socket received_socket){
                socket_cli= received_socket;
        }
        public void run(){
            try{
                while(true){ //KEEP THE SERVER OPENED    	
                    int accumulator_int = 0;
                    DataInputStream in_data= new DataInputStream(socket_cli.getInputStream());
                    DataOutputStream out_data= new DataOutputStream(socket_cli.getOutputStream());
                                    
                    try{ //TO END THE LOOP WHEN THE CLIENT IS DISCONNECTED 
                        while(true){  //LOOP FOR RECEIVING AGAIN FROM THE SAME CLIENT
                            String received_data_string = in_data.readUTF();//SAVE THE STRING WITH THE NUMBERS THAT THE USER INTRODUCES
                            String [] eachnum_string = received_data_string.split(" ");//SEPARATE EACH NUMBER OF THE STRING

                            for (int i=0;i<eachnum_string.length;i++){
                                int eachnum_int = Integer.parseInt(eachnum_string[i].trim());
                                accumulator_int = accumulator_int + eachnum_int;
                                System.out.println("Accumulator is "+accumulator_int);
                            }
                            String accumulator_string = Integer.toString(accumulator_int); 
                            out_data.writeUTF(accumulator_string);

                        }
                    }catch (SocketException ex){ 
                            System.out.println("Client is disconnected");
                            break;
                    }catch (IOException e){
                            break;
                    } 
                }
            }catch (Exception e){
            }
        }
    }   
}