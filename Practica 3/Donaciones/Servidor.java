import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.lang.Thread;

public class Servidor implements Servidor_I{
    public void registrar () throws RemoteException{

    }
    public void donar () throws RemoteException{

    }
    public int total_donado () throws RemoteException{
        return 0;
    }
    public static void main(String[] args) {
        final String NOMBRE_REMOTO = "DSD_RMI";

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            Servidor_I prueba = new Servidor();
            Servidor_I stub = (Servidor_I) UnicastRemoteObject.exportObject(prueba, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(NOMBRE_REMOTO, stub);
            System.out.println("Ejemplo bound");
        } catch (Exception e) {
            System.err.println("Ejemplo exception:");
            e.printStackTrace();
        }
    }
}
