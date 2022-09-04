package remote.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import remote.client.ClientInterface;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Server extends UnicastRemoteObject implements ServerInterface{
    private final ArrayList<ClientInterface> clients; //liste contient tous les clients mais qui ne sont pas bloqué 
    private final ArrayList<ClientInterface> blockedClients; //liste contient tous les clients bloqués
    Semaphore s = new Semaphore(10);//Initiate semaphore
    //constructer
    public Server() throws RemoteException{
        super();
        this.clients = new ArrayList<>();
        blockedClients = new ArrayList<>();
    }
    
    //This function is used to distribute messages to all connected clients or client preset lists (private parsing)
    @Override
    public synchronized void broadcastMessage(String message,List<String> list) throws RemoteException {
        if(list.isEmpty()){
            int i= 0;
            while (i < clients.size()){
                clients.get(i++).retrieveMessage(message);
            }
        }else{
            for (ClientInterface client : clients) {
                for(int i=0;i<list.size();i++){
                    if(client.getName().equals(list.get(i))){
                        client.retrieveMessage(message);
                    }
                }
            }
        }
    }
    
    //This function is used to distribute files to all connected clients or client preset lists (private resolution)
    @Override
    public synchronized void broadcastMessage(ArrayList<Integer> inc, List<String> list,String filename) throws RemoteException {
        if(list.isEmpty()){
            int i= 0;
            while (i < clients.size()){
                clients.get(i++).retrieveMessage(filename,inc);
            }
        }else{
            for (ClientInterface client : clients) {
                for(int i=0;i<list.size();i++){
                    if(client.getName().equals(list.get(i))){
                        client.retrieveMessage(filename,inc);
                    }
                }
            }
        }
    }
    
    //This function provide backup of files that users upload
    @Override
    public synchronized void backUp (ArrayList<Integer> inc,String filename) throws RemoteException {
         try {
                    FileOutputStream out;
                    String separator;
                    if(System.getProperty("os.name").startsWith("Linux") || System.getProperty("os.name").startsWith("MacOS")) separator = "/";
                    else separator = "\\";
                    out = new FileOutputStream(System.getProperty("user.home") + separator + filename);
                    String[] extension = filename.split("\\.");
                    for (int i = 0; i<inc.size(); i++) {
                        int cc = inc.get(i);
                        if(extension[extension.length - 1].equals("txt")||
                           extension[extension.length - 1].equals("java")||
                           extension[extension.length - 1].equals("php")||
                           extension[extension.length - 1].equals("c")||
                           extension[extension.length - 1].equals("cpp")||
                           extension[extension.length - 1].equals("xml")||
                           extension[extension.length - 1].equals("exe")||
                           extension[extension.length - 1].equals("png")||
                           extension[extension.length - 1].equals("jpg")||
                           extension[extension.length - 1].equals("jpeg")||
                           extension[extension.length - 1].equals("pdf")||
                           extension[extension.length - 1].equals("jar")||
                           extension[extension.length - 1].equals("rar")||
                           extension[extension.length - 1].equals("zip")
                                )
                        out.write((char)cc);
                        else{
                            out.write((byte)cc);
                        }
                    }
                    out.flush();
                    out.close();
                    JOptionPane.showMessageDialog(new JFrame(),"The backup file saved at " + System.getProperty("user.home") + separator + filename,"File Saved",JOptionPane.INFORMATION_MESSAGE);
                } catch (FileNotFoundException ex) {
                    System.out.println("Error: " + ex.getMessage());
                } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }             
    }
        
    //This function adds a connected client to the list of clients on the server
    @Override
    public synchronized void addClient(ClientInterface client) throws RemoteException {
        this.clients.add(client);
    }
    
    //This function retrieves the name of the connected client
    @Override
    public synchronized Vector<String> getListClientByName(String name) throws RemoteException {
        Vector<String> list = new Vector<>();
        for (ClientInterface client : clients) {
            if(!client.getName().equals(name)){
                list.add(client.getName());
            }
        }
        return list;
    }
    
    //This function is used to prevent clients from sending messages, but can receive messages
    @Override
    public synchronized void blockClient(List<String> clients){
        for(int j=0;j<this.clients.size();j++){
            for(int i=0;i<clients.size();i++){
                try {
                    if(this.clients.get(j).getName().equals(clients.get(i))){
                        this.clients.get(j).closeChat(clients + " you are blocked by admin");
                        blockedClients.add(this.clients.get(j));
                    }
                } catch (RemoteException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
    }
    
    
    //This function can completely delete a single chat client (kick out)
    @Override
    public synchronized void removeClient(String clients){
        for(int j=0;j<this.clients.size();j++){
            try {
                if(this.clients.get(j).getName().equals(clients)){
                    this.clients.remove(j);
                }
            } catch (RemoteException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    //This function is used to activate the client in the chat room, for example, in the case of "block"
    @Override
    public synchronized void reactiveClient(List<String> clients) throws RemoteException {
        for(int j=0;j<this.blockedClients.size();j++){
            for(int i=0;i<clients.size();i++){
                try {
                    if(this.blockedClients.get(j).getName().equals(clients.get(i))){
                        this.blockedClients.get(j).openChat();
                        this.blockedClients.remove(j);
                    }
                } catch (RemoteException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
    }
    //This function verifies whether username already exists in the server, because username is an identifier in the chat room
    @Override
    public boolean checkUsername(String username) throws RemoteException {
        boolean exist = false;
        for(int i=0;i<clients.size();i++){
            if(clients.get(i).getName().equals(username)){
                exist = true;
            }
        }
        for(int i=0;i<blockedClients.size();i++){
            if(blockedClients.get(i).getName().equals(username)){
                exist = true;
            }
        }
        return exist;
    }
    
    //The function checks the number of available tokens before assigning them.
    @Override
    public synchronized Semaphore check() throws RemoteException {
          
            if(s.availablePermits()!=0){
              try {
                 s.acquire();
            } catch (Exception ex) {
             System.out.println("Error: " + ex.getMessage());
            }
           
        }
        return s;
    }
    // The function is used to get the updated semaphore
    @Override
    public synchronized Semaphore getSemaphore()
    {
        return s;
    }
    
    //The function is used to release a token
    @Override
    public synchronized void release()
    {
        s.release();
    }
}