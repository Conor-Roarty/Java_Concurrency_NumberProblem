package assignment2;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@SuppressWarnings("serial")
public class LargestSubsequenceClient implements Serializable {
        public static void main(String[] args) {
        String hostName = "localhost"; // default host name
        int hostPort = 4444; // default host port
        // assign host machine name and port to connect to
        if (args.length != 0) {
            if (args[0] != null) {
                hostName = args[0]; // user specified machine
            }
            if (args[1] != null) {
                hostPort = Integer.parseInt(args[1]); // user specified port
            }
        }

        System.out.println("Connecting to Find-Largest-Number Server");// Print Inital Task Of Connecting. If It Fails Another Message Will Be Printed.
        // connect to server and extract input and output streams
        try (Socket serverSocket = new Socket(hostName, hostPort);
            // create output stream with specified socket of server and client. This will send array objects from client to server.   
            ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
            // create input stream with specified socket of server and client. This will take input\replies from server to client.
            ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream())) {

            // create client input stream for user input
            Scanner scanner = new Scanner(System.in);
            // Initialise The ArrayList
            List<Integer> numList = new ArrayList<>();
            // Create Boolean For Exiting The Loop - This is needed to check the input at different sections without 
            boolean exit = true;
            do{
                System.out.print("Type a single integer value: ");
                if(scanner.hasNext("q") || scanner.hasNext("Q")){
                    exit = false;
                } else{
                    while (!scanner.hasNextInt() && exit) {
                        if(scanner.hasNext("q") || scanner.hasNext("Q")){
                            exit = false;
                        } else {
                            System.out.print("Invalid input: Integer Required (Try again): ");
                            scanner.next();
                        }
                    }
                    if(scanner.hasNextInt()){
                        int newNumber = scanner.nextInt();
                        numList.add(newNumber);
                    }
                }
            }while( exit );
            
            try {
                if(!numList.isEmpty()){
                    out.writeObject(numList);
                    out.flush();
                    System.out.println("\nSerialization Successful... Checkout your specified output file..\n");
                } else {
                    System.out.println("\nNo Values Entered.");
                }
            } catch (IOException e) {
                System.out.println("\n" + e.toString() + "\n");
            }
            // Only print reply if something was actually sent to the server
            if(!numList.isEmpty()){    
                String serverReply;// Initialise String To Assign Servers Output Reply
                serverReply = (String)in.readObject();// Reader and Assign Servers Output Reply To String
                System.out.println(serverReply);// Print Servers Output Reply To Client Side
            } else{
                System.out.println("\nRun Again To Enter Integers\n");
            }
            // Close All Stream Regardless Of If Anything was Sent Or Not.
            out.close();
            in.close();
            serverSocket.close();
            
        } catch (Exception e) {
            System.err.println("Client Exception:  " + e.getMessage());
        }
    } // end main
}