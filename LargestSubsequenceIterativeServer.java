package assignment2;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class LargestSubsequenceIterativeServer {
        public static void main(String[] args) {
        // determine if port to listen on is specified by user else use default
        int portNumber = 4444; // default port number
        if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]); // user specified port number
        }
        System.out.println("Largest Number Iterative Server Started");
        int clientIdNumber = 0;
        List<Integer> numList = new ArrayList();
        // create serverSocket to listen on	
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                // accept client connection
                Socket clientSocket = serverSocket.accept();
                try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                     ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
                    
                    System.out.println("Client Accepted");
                    clientIdNumber++; // give id number to connecting client
                    
                    numList = (ArrayList)in.readObject();
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
                } catch (IOException e) {
                    System.err.println("IOException:" + e.getMessage());
                }
            } // end while true
        } catch (Exception e) {
            System.err.println("Exception:" + e.getMessage());
        } // end catch
    } // end main

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
}
