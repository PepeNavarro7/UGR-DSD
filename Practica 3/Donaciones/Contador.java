import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
//import java.net.MalformedURLException;
import java.util.Map;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Contador extends UnicastRemoteObject implements Contador_I, Comunicador_I {
    Map<String,Integer> recuento_local;
    final String ID;
    Registry registry;
    Comunicador_I pareja;

    public Contador(String id) throws RemoteException{
        this.recuento_local = new HashMap<>();
        this.ID=id;
        this.registry = LocateRegistry.getRegistry("localhost");
    }
    public void setPareja(String id) throws RemoteException{
        try{
            pareja = (Comunicador_I)registry.lookup(id);
        } catch (Exception e) {
            System.err.println("Comunicador_I exception:");
            e.printStackTrace();
        }
    }

    public void registrar (String nombre) throws RemoteException{
        if(!recuento_local.containsKey(nombre)){
            recuento_local.put(nombre, 0);
            System.out.println("Registrado "+nombre+" en "+ID);
        } else {
            System.out.println(nombre+" ya estaba registrado en "+ID);
        }
    }
    public void donar (String nombre, int cantidad) throws RemoteException{
        if(recuento_local.containsKey(nombre)){
            int aux = recuento_local.get(nombre);
            aux+=cantidad;
            recuento_local.put(nombre, aux);
            System.out.println(nombre+" ha donado "+cantidad);
        } else {
            System.out.println("Para donar hay que estar registrado.");
        } 
    }
    public int total_donado () throws RemoteException{
        int total = 0;
        total+=this.subtotal();
        total+=pareja.subtotal();
        return total;
    }
    public int subtotal() throws RemoteException{
        int subtotal = 0;
        for (int x : recuento_local.values())
            subtotal+=x; 
        return subtotal;
    }
    
}