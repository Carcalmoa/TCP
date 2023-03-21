import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class tcp3ser{
    public static void main(String[] args) {
        if (args.length!=1){
            System.out.println ("Unexpected format. Introduce: tcp3ser port_number");
            System.exit(-1);
        }
        try{

            Selector selector= Selector.open(); //create the selector
            int port = Integer.parseInt (args[0]); 
            InetSocketAddress socket_address= new InetSocketAddress(port);
            //System.out.println(socket_address);

            //UDP
            DatagramChannel channel_UDP = DatagramChannel.open();
            channel_UDP.socket().bind(socket_address);
            channel_UDP.configureBlocking(false);//channel in non blocking mode for being use
            SelectionKey key_UDP=channel_UDP.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            int accumulator_UDP=0;
            
            //TCP
            ServerSocketChannel channel_TCP =ServerSocketChannel.open();//create the channel
            channel_TCP.bind(socket_address);//the channel starts listening from this direction
            channel_TCP.configureBlocking(false);//channel in non blocking mode for being use
            channel_TCP.register(selector, SelectionKey.OP_ACCEPT);//Registers this channel with the given selector, returning a selection key when the channel accepts a new connection
            Map<SocketChannel, Integer> channels = new HashMap<>();
            

            while(true){
                int readyChannels = selector.select();
                if (readyChannels==0) continue;
                //System.out.println("1");
                Set<SelectionKey> setOfKeys = selector.selectedKeys();//list of selected keys 
                //System.out.println("2");
                Iterator<SelectionKey> keyIterator = setOfKeys.iterator();//iterator is used to loop trough collections
                //System.out.println("3");
                while(keyIterator.hasNext()){
                    //System.out.println("entra en el hasnext");
                    SelectionKey key = keyIterator.next();
                    if(!key.isValid())
                        continue;
                    if(key.isAcceptable()){
                        //System.out.println("entra en acceptable");
                        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                        SocketChannel socket_cli = serverChannel.accept();
                        //System.out.println("acceptable1");
                        socket_cli.configureBlocking(false);
                        //System.out.println("acceptable2");
                        socket_cli.register(selector,SelectionKey.OP_READ);
                        //System.out.println("acceptable3");
                        channels.put(socket_cli,0);
                        //System.out.println("ultima linea del acceptable");
                    }
                    else if(key.isReadable()){
                       
                        //System.out.println("entra en readable");
                        SocketChannel socket_readable=null; 
                        try{
                        //UDP
                            //RECEIVE
                            DatagramChannel datagram_receive = (DatagramChannel) key_UDP.channel();
                            ByteBuffer number_received_UDP= ByteBuffer.allocate(4);
                            number_received_UDP.clear();
                            SocketAddress socket_send= datagram_receive.receive(number_received_UDP);
                            number_received_UDP.flip();
                            int number_UDP=number_received_UDP.getInt();
                            accumulator_UDP = accumulator_UDP + number_UDP;
                            System.out.println("Accumulator of the UDP connection is "+accumulator_UDP);

                            //SEND
                            ByteBuffer result_UDP = ByteBuffer.allocate(4);
                            result_UDP.clear();
                            result_UDP.putInt(accumulator_UDP);
                            result_UDP.flip();
                            channel_UDP.send(result_UDP,socket_send);
                        }catch(Exception e3){
                            try{
                                //TCP
                                //RECEIVE
                                socket_readable = (SocketChannel) key.channel();
                                int accumulator_TCP= channels.get(socket_readable);
                                ByteBuffer number_received_TCP= ByteBuffer.allocate(4);
                                number_received_TCP.clear();
                                socket_readable.read(number_received_TCP);
                                number_received_TCP.flip();
                                int number_TCP=number_received_TCP.getInt();
                                //System.out.println("numero recibido "+number);
                                accumulator_TCP = accumulator_TCP + number_TCP;
                                System.out.println("Accumulator of the TCP connection is "+accumulator_TCP);

                                //SEND
                                ByteBuffer result_TCP = ByteBuffer.allocate(4);
                                result_TCP.clear();
                                result_TCP.putInt(accumulator_TCP);
                                result_TCP.flip();
                                socket_readable.write(result_TCP);
                                channels.put(socket_readable, accumulator_TCP);
                            }catch(Exception e4){
                                channels.remove(socket_readable);
                                socket_readable.close();
                            }
                        }
                        
                         
                    }
                    keyIterator.remove();
                }
            }


        }catch (SocketException ex){ 
            System.out.println("Client is disconnected");
        }catch (IOException e){

        }catch (Exception e){
        }
    }
}
            
   