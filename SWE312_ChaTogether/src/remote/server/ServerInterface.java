package remote.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import remote.client.ClientInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;


public interface ServerInterface extends Remote{    
    //This feature is used to distribute messages to all connected clients
    void broadcastMessage(String message,List<String> list) throws RemoteException;
    
    //This feature is used to distribute shared files to all connected clients
    void broadcastMessage(ArrayList<Integer> inc,List<String> list,String filename) throws RemoteException;
    
    //This function provide backup of files that users upload
    void backUp(ArrayList<Integer> inc,String filename) throws RemoteException;
    
    //This function retrieves the name of the connected client
    Vector<String> getListClientByName(String name) throws RemoteException;
    
    //This function adds a connected client to the list of connected clients
    void addClient(ClientInterface client) throws RemoteException;
    
    //This function is used to prevent clients from sending messages, but can receive messages
    void blockClient(List<String> clients) throws RemoteException;
    
    
    //This function can completely delete the list of chat clients (kick out)
    void removeClient(String clients) throws RemoteException;
    
    //This function is used to activate the client in the chat room, for example, in the case of "block"
    void reactiveClient(List<String> clients) throws RemoteException;
    
    //This function verifies whether username already exists in the server, because username is an identifier in the chat room
    boolean checkUsername(String username) throws RemoteException;
    
     //The function checks the number of available tokens before assigning them.
    public Semaphore check()throws RemoteException;  
    
    // The function is used to get the updated semaphore
    public Semaphore getSemaphore()throws RemoteException; 
    
    //The function is used to release a token
    public void release()throws RemoteException; 
}