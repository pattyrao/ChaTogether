package remote.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


public interface ClientInterface extends Remote{
    //This function retrieves a discussion message from the server
    void retrieveMessage(String message) throws RemoteException;
    
    //This function restores a discussion shared file from the server
    void retrieveMessage(String filename,ArrayList<Integer> inc) throws RemoteException;
    
    //This function is used to send messages from the client to the server
    void sendMessage(List<String> list) throws RemoteException;
    
    //This function is used to recover the name of the connected client (client identifier) = = > user name
    String getName()throws RemoteException;
    
    //This deactivation feature enables the client to send messages
    void closeChat(String message) throws RemoteException;
    
    //This feature is used to enable the client to send messages
    void openChat() throws RemoteException;
}
