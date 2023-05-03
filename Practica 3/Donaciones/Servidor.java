//import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
//import java.rmi.server.UnicastRemoteObject;
//import java.lang.Thread;

public class Servidor{
    final static String ID1 = "Servidor 1", ID2 = "Servidor 2";
    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            Contador_I contador1 = new Contador(ID1);
            Contador_I contador2 = new Contador(ID2); 

            // Registramos los dos servidores contador
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(ID1, contador1);
            registry.rebind(ID2, contador2);

            // Enviamos a cada servidor una referencia al otro, 
            // pero casteándola a la interfaz correcta
            Comunicador_I com1 = (Comunicador_I)contador1, 
                com2 = (Comunicador_I)contador2;
            com1.setPareja(ID2);
            com2.setPareja(ID1);

            System.out.println("Los contadores "+ID1+" y "+ID2+" estan funcionando.");
        } catch (Exception e) {
            System.err.println("Ejemplo exception:");
            e.printStackTrace();
        }
    }
}
