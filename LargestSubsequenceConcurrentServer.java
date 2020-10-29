package assignment2;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LargestSubsequenceConcurrentServer extends Thread {
    // This server acts as a multi-threaded server. 
    // A number of clients can connect at once.
    private final Socket clientSocket; // client socket for each thread connected
    int clientIdNumber;
    List<Integer> numList = new ArrayList();
    
    // Constructor
    LargestSubsequenceConcurrentServer(Socket clientSocket, int clientIdNumber) {
        this.clientSocket = clientSocket;
        this.clientIdNumber = clientIdNumber;
    } // end constructor

    // run method for thread
    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
            
            System.out.println("New Server Thread Running for Client");

            try {
                numList = (ArrayList)(in.readObject());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LargestSubsequenceConcurrentServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Client:" + clientIdNumber + "\nEntry For " + numList + " Added");

            System.out.println("Unordered List: " + numList);
            Collections.sort(numList);
            System.out.println("Ordered List: " + numList);

            ArrayList<Integer> result = new ArrayList();
            result = getSubsequence(numList);

            int resultSize = 0;
            resultSize = result.size();

            try {
                out.writeObject(resultSize + " Elements In Sub-Sequence\nSub-Sequence Is: " + result.toString());
                out.flush();
                out.close();
                System.out.println("\nSerialization Successful... Check your Client output..\n");
            } catch (Exception e) {
                System.err.println("Write Error " + e.getMessage());
            }
            System.out.println(resultSize + " Elements In Sub-Sequence\nSub-Sequence Is: " + result.toString());

        } catch (IOException e) {
            System.out.println("IOException:" + e.getMessage());
        }
    } // end run

    public static ArrayList<Integer> getSubsequence(List<Integer> array) {
        ArrayList<Integer> holder = new ArrayList<>();
        ArrayList<Integer> subsequence = new ArrayList<>();
        // The int needs initialised
        // To avoid conflicting with other ints in the array 
        // the boolean "first" ensures that the initalised number is never used
        // thus no conflict can occur
        int previousNum = -999999999;
        boolean first = true;
        
        for(Integer num : array){
            if(first){
                previousNum = num;
                holder.add(previousNum);
                first = false;
            } else {
                if(previousNum == (num - 1)){
                    holder.add(num);
                    previousNum = num;
                } else { 
                    holder = new ArrayList<Integer>();
                    previousNum = num;
                    holder.add(num);
                }
            }
            if(subsequence.size() < holder.size()){
                subsequence = holder;
            }
        }
        System.out.println(subsequence);
        return subsequence;
    }

    public static void main(String[] args) {
        // determine if port to listen on is specified by user else use default
        int portNumber = 4444; // default port number

        if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]); // user specified port number
        }
        int clientIdNumber = 0;
        System.out.println("Largest Number Concurrent Server Started");
        // create serverSocket to listen on	
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Accepted from " + clientSocket.getInetAddress());
                clientIdNumber++; // give id number to connecting client
                // spawn a new thread to handle new client
                LargestSubsequenceConcurrentServer concurrentServerThread = new LargestSubsequenceConcurrentServer(clientSocket, clientIdNumber);
                System.out.println("About to start new thread");
                concurrentServerThread.start();
            } // end while true
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        } // end catch
    }
}
