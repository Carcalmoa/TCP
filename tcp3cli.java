import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;


public class tcp3cli{
    public static void main(String[] args) {
        if (args.length!=2 && args.length!=3){
            System.out.println ("Unexpected format. Introduce: tcp3cli ip_address port_number [-u]");
            System.exit(-1);
        }

        
        try{
            InetAddress ip_address = InetAddress.getByName(args[0]);
            int port_number = Integer.parseInt (args[1]);
            //System.out.println("1");
            
        //UDP
            if(args.length==3){

                while(true){

                    DatagramChannel channel = DatagramChannel.open();
                    InetSocketAddress serverAddress= new InetSocketAddress(ip_address, port_number);

                    System.out.print ("Write numbers separated with blanks, press intro or '0' to end the message: ");
                    Scanner read = new Scanner(System.in);
                    int number = read.nextInt(); //READ THE NUMBER INTRODUCED
                    
                    if(number==0)//CHECK IF THE NUMBER IS 0
                        System.exit(-1);;
                            
                    //SEND
                    ByteBuffer message = ByteBuffer.allocate(4);
                    message.clear();
                    message.putInt(number);
                    message.flip();
                    channel.send(message,serverAddress); //SEND THE DATAGRAM WITH THE MESSAGE


                    //RECEIVE
                    ByteBuffer result= ByteBuffer.allocate(4);
                    result.clear();
                    channel.receive(result); //RECEIVE THE RESULT OF THE ACCUMULATOR
                    result.flip();
                    int accumulator=result.getInt();
                    System.out.println("The final value of the UDP accumulator is "+ accumulator);
                    channel.close();
                } 
            }
       
            //System.out.println("2");
            SocketChannel socket_cli= SocketChannel.open(); //CREATES THE SOCKET
            SocketAddress socket_address=new InetSocketAddress(ip_address,port_number);
            socket_cli.connect(socket_address);
            //System.out.println("3");
            

            while(true){
                System.out.print ("Write numbers separated with blanks, press intro to end the message: ");
                Scanner read = new Scanner(System.in);
                int number = read.nextInt(); //READ THE NUMBER INTRODUCED
                
                if(number==0)//CHECK IF THE NUMBER IS 0
                    break;

                //System.out.println(number);    

                //SEND THE NUMBER
                ByteBuffer message = ByteBuffer.allocate(4);
                message.clear();
                message.putInt(number);
                message.flip();
                socket_cli.write(message);
                //System.out.println("enviado");  

                //RECEIVE THE VALUE OF THE ACCUMULATOR
                ByteBuffer result= ByteBuffer.allocate(4);
                result.clear();
                socket_cli.read(result);
                result.flip();
                int accumulator=result.getInt();
                System.out.println("The final value of the TCP accumulator is "+ accumulator);

            }//CLOSE THE WHILE



        } catch (SocketTimeoutException e){
            System.out.println("Timeout has happened");
        }catch (IOException e){

        } catch (Exception e){
            
        }
    }
}