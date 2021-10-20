package org.project.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerReference {

    private static Server server = null;

    public static void initializeServer() throws RemoteException, NotBoundException {
        if (server == null) {
            Registry reg = LocateRegistry.getRegistry(Server.PORT);
            server = (Server) reg.lookup(Server.NAME);
            System.out.println("SERVER CONNESSO");
        }
    }

    public static Server getServer() throws NotBoundException, RemoteException {
        initializeServer();
        return server;
    }
}
