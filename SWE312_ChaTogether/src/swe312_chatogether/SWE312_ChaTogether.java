/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swe312_chatogether;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import remote.server.Server;

/**
 *
 * @author Administrator
 */
public class SWE312_ChaTogether {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
           try {
            LocateRegistry.createRegistry(4321);
            Naming.rebind("rmi://localhost:4321/remote",new Server());
            System.out.println("Server Started ...");
        } catch (MalformedURLException | RemoteException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
}
