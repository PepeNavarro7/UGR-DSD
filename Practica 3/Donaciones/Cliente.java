import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente {
    public static void main(String args[]) {
        final String NOMBRE_REMOTO = "DSD_RMI";

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            //System.out.println("Buscando el objeto remoto");
            Registry registry = LocateRegistry.getRegistry("localhost");
            Servidor_I instancia_local = (Servidor_I) registry.lookup(NOMBRE_REMOTO);
            System.out.println("Invocando el objeto remoto");
            // Ahora hacemos uso de instancia_local llamando a sus metodos
        } catch (Exception e) {
            System.err.println("Ejemplo_I exception:");
            e.printStackTrace();
        }
    }
}
