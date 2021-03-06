package org.project.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Questa classe gestisce la connessione tra client e server utilizzando RMI.
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class ServerReference {

    /**
     * Referenza del server.
     */
    private static Server server = null;

    /**
     * Utilizzato per reperire il server dal registro RMI.
     *
     * @throws RemoteException   RemoteException
     * @throws NotBoundException NotBoundException
     */
    public static void initializeServer() throws RemoteException, NotBoundException {
        if (server == null) {
            try {
                //Se il server è su un altro pc, al posto di "localhost" mettere l'IP del pc dove sta girando il server
                server = (Server) Naming.lookup("rmi://localhost:" + Server.PORT + "/" + Server.NAME);
                System.out.println("SERVER CONNESSO");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metodo pubblico che restituisce la referenza del server e viene inizializzata se a null.
     *
     * @return referenza del server
     * @throws NotBoundException NotBoundException
     * @throws RemoteException   RemoteException
     */
    public static Server getServer() throws NotBoundException, RemoteException {
        initializeServer();
        return server;
    }
}
