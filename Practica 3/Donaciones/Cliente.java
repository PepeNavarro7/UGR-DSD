import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class Cliente {
    final static String ID1 = "Servidor 1", ID2 = "Servidor 2";
    String nombreCliente;
    char opcionCliente;
    Contador_I servidor;
    
    // Funciones principales
    public Cliente(Contador_I serv){
        this.servidor=serv;
    }
    public void loop() throws RemoteException{
        nombreCliente = intro();
        informacion();
        opcionCliente = preguntar();
        while(opcionCliente != 'O'){
            switch (opcionCliente){
                case 'R': this.registrar(); break;
                case 'D': this.donar(); break;
                case 'T': this.total(); break;
                case 'U': this.cambio(); break;
                case 'I': this.informacion(); break;
            }
            opcionCliente = preguntar();
        }
    }
    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            Contador_I local1 = (Contador_I) registry.lookup(ID1);
            Contador_I local2 = (Contador_I) registry.lookup(ID2);
            Random random = new Random();
            // La asignacion del servidor con el que contacta el cliente es aleatoria
            Contador_I servidor = random.nextInt(2)==0?local1:local2;
            Cliente cliente = new Cliente(servidor);
            cliente.loop();
        } catch (Exception e) {
            System.err.println("Ejemplo_I exception:");
            e.printStackTrace();
        }
    }
    
    // Funciones auxiliares que forman parte del loop principal
    private void total() throws RemoteException{
        int total = servidor.total_donado();
        System.out.println("Se ha donado un total de "+total);
    }
    private void donar() throws RemoteException{
        System.out.println("Cuanto quieres donar?");
        int cantidad = Integer.parseInt(System.console().readLine());
        int aux = servidor.donar(nombreCliente, cantidad); 
        switch(aux){
            case 0: System.out.println("Para donar antes hay que estar registrado"); break;
            case 1: System.out.println(cantidad+" donado en el servidor 1."); break;
            case 2: System.out.println(cantidad+" donado en el servidor 2."); break;
            default: System.out.println("Problema en la donacion -> "+aux);
        }
    }
    private void registrar() throws RemoteException{
        int aux = this.servidor.registrar(nombreCliente);
        switch(aux){
            case -1: System.out.println("El cliente ya estaba registrado en el servidor 1."); break;
            case -2: System.out.println("El cliente ya estaba registrado en el servidor 2."); break;
            case 1: System.out.println("El cliente ha sido registrado en el servidor 1."); break;
            case 2: System.out.println("El cliente ha sido registrado en el servidor 2."); break;
            default: System.out.println("Problema en el registro -> "+aux);
        }
    }

    // Funciones para interactuar con la consola
    private void cambio() {
        System.out.println("Adios, "+nombreCliente);
        nombreCliente = intro(); 
    }
    private char preguntar(){
        System.out.println(nombreCliente+", qué deseas hacer?");
        char c = System.console().readLine().charAt(0);
        c = Character.toUpperCase(c);
        return c;
    }
    private void informacion(){
        System.out.println("I -> Informacion, R -> Registrarme, D -> Donar");
        System.out.println("T -> Total donado, U -> Cambiar de cliente, O -> Salir");
    }
    private String intro(){
        System.out.println("Buenas! Puedes darnos tu nombre?");
        String nombre = System.console().readLine();
        return nombre;
    }
}
