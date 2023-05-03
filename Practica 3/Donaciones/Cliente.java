import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente {
    final static String ID1 = "ID1", ID2 = "ID2";
    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String nombre = args[0];
            Registry registry = LocateRegistry.getRegistry("localhost");
            //System.out.println("Buscando el objeto remoto 1");
            Contador_I local1 = (Contador_I) registry.lookup(ID1);
            Contador_I local2 = (Contador_I) registry.lookup(ID2);
            //System.out.println("Invocando el objeto remoto");

            // Ahora hacemos uso de la instancia local llamando a sus metodos
            local1.registrar(nombre);
            local1.donar(nombre,100);
            local1.registrar("Paco1");
            local1.donar("Paco1",100);
            local1.donar("Paco1",100);
            System.out.println("Donado1 -> "+local1.total_donado());

            local2.registrar(nombre);
            local2.donar(nombre,100);
            local2.registrar("Paco2");
            local2.donar("Paco2",100);
            local2.donar("Paco2",100);
            System.out.println("Donado2 -> "+local1.total_donado());
        } catch (Exception e) {
            System.err.println("Ejemplo_I exception:");
            e.printStackTrace();
        }
    }
}
