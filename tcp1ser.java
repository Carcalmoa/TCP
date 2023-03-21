import java.io.*;
import java.util.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tcp1ser{
    public static void main(String[] args) {
        if (args.length!=1){
            System.out.println ("Unexpected format. Introduce: tcp1ser port_number");
            System.exit(-1);
        }
    try{
        int accumulator_int = 0;
        int port = Integer.parseInt (args[0]); 
        ServerSocket socket_ser = new ServerSocket(port);//CONNECT TO THE PORT RECEIVED BY ARG
        while(true){ //KEEP THE SERVER OPENED
                //byte[] message = new byte[1000];
                accumulator_int = 0;
	        	Socket socket_cli=socket_ser.accept();
                DataInputStream in_data= new DataInputStream(socket_cli.getInputStream());
                DataOutputStream out_data= new DataOutputStream(socket_cli.getOutputStream());
                
                
            while(true){ //LOOP PARA MANTENER MISMO CLIENTE
                try{//TO END THE LOOP WHEN THE CLIENT IS DISCONNECTED  
                    //System.out.println("datagram size "+ datagram_to_receive.getLength());
                    
                    String received_data_string = in_data.readUTF();
                   
                    //System.out.println("received string "+received_data_string);
                    LinkedList<String> received_data_list = new LinkedList<String>();

                    //CREATE A LINKEDLIST WITH THE NUMBERS OF THE STRING RECEIVED FROM THE CLIENT
                    Pattern p = Pattern.compile("\\d+");
                    Matcher m = p.matcher(received_data_string);
                    while (m.find()) {
                        received_data_list.add(m.group());
                    }
                    //System.out.println(received_data_list);
                    //new String(datagram_to_receive.getData());
                    //CATCH EACH NUMBER OF THE LINKED LIST
                    for (int i=0;i<received_data_list.size();i++){
                        //System.out.println(received_data_list.get(i)+ "geti");
                        int eachnum_int = Integer.parseInt(received_data_list.get(i));
                        accumulator_int = accumulator_int + eachnum_int;
                        System.out.println("Accumulator is "+accumulator_int);

                    }

                    //int cli_port = datagram_to_receive.getPort();//TAKE THE CLIENT PORT FROM THE DATAGRAM USED TO RECEIVE INFO
                    //InetAddress cli_ip = datagram_to_receive.getAddress();//TAKE THE IP FROM THE DATAGRAM USED TO RECEIVE INFO
                    String accumulator_string = Integer.toString(accumulator_int); 
                    //byte[] accumulator = accumulator_string.getBytes();

                    out_data.writeUTF(accumulator_string);

                    }catch (SocketException ex){ 
                        break;

                    }catch (IOException e){
                        break;
                    } 
            }//KEEP THE SAME CLIENT
        }//KEEP THE SERVER OPENED
                        
    }catch (Exception e){
    } 
}
}