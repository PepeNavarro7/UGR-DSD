//import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
//import java.rmi.server.UnicastRemoteObject;
//import java.lang.Thread;

public class Servidor{
    final static String ID1 = "ID1", ID2 = "ID2";
    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            Contador_I contador1 = new Contador(ID1);
            Contador_I contador2 = new Contador(ID2); 

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(ID1, contador1);
            registry.rebind(ID2, contador2);

            Comunicador_I com1 = (Comunicador_I)contador1, com2 = (Comunicador_I)contador2;
            com1.setPareja(ID2);
            com2.setPareja(ID1);

            System.out.println("Los contadores "+ID1+" e "+ID2+" estan colgados y funcionando.");
        } catch (Exception e) {
            System.err.println("Ejemplo exception:");
            e.printStackTrace();
        }
    }
}
