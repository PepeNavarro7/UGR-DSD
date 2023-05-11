import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Contador extends UnicastRemoteObject implements Contador_I, Comunicador_I {
    Map<String,Integer> recuento_local;
    final private String ID;
    final Registry registry;
    Comunicador_I pareja;

    // Métodos para poner el contador en marcha
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

    // Métodos que usa el cliente -> interfaz Contador_I
    public int registrar (String nombre) throws RemoteException{
        int aux = 0;
        if(this.tieneCliente(nombre)){
            System.out.println(this.ID+"-> "+nombre+" ya estaba registrado en "+this.getID());
            aux=Character.getNumericValue(this.getID().charAt(9))*-1;
        } else if (pareja.tieneCliente(nombre)){
            System.out.println(this.ID+"-> "+nombre+" ya estaba registrado en "+pareja.getID());
            aux=Character.getNumericValue(pareja.getID().charAt(9))*-1;
        } else {
            if(this.numClientes()>pareja.numClientes()){
                pareja.registra(nombre);
                aux=Character.getNumericValue(pareja.getID().charAt(9));
            } else{
                this.registra(nombre);
                aux=Character.getNumericValue(this.getID().charAt(9));
            }
        }
        return aux;
    }
    public int donar (String nombre, int cantidad) throws RemoteException{
        int aux = -1;
        if(!this.tieneCliente(nombre) && !pareja.tieneCliente(nombre)){
            System.out.println(this.ID+"-> Para donar hay que estar registrado.");
            aux=0;
        } else if(this.tieneCliente(nombre)){
            this.dona(nombre,cantidad);
            aux=Character.getNumericValue(this.getID().charAt(9));
        } else {
            pareja.dona(nombre,cantidad);
            aux=Character.getNumericValue(pareja.getID().charAt(9));
        } 
        return aux;
    }
    public int total_donado () throws RemoteException{
        int total = 0;
        total+=this.subtotal();
        total+=pareja.subtotal();
        System.out.println(this.ID+"-> Se ha donado un total de "+total);
        return total;
    }

    // Métodos entre los contadores -> Interfaz Comunicador_I
    public int subtotal() throws RemoteException{
        int subtotal = 0;
        for (int x : recuento_local.values())
            subtotal+=x; 
        System.out.println(this.ID+"-> Subtotal de "+subtotal);
        return subtotal;
    }
    public int numClientes() throws RemoteException{
        return this.recuento_local.keySet().size();
    }
    public boolean tieneCliente(String cliente) throws RemoteException{
        return recuento_local.containsKey(cliente);
    }
    public String getID() throws RemoteException{
        return this.ID;
    }
    public void registra(String cliente) throws RemoteException{
        this.recuento_local.put(cliente,0);
        System.out.println(this.ID+"-> "+cliente+" se ha registrado en este servidor.");
    }
    public void dona(String cliente, int cantidad) throws RemoteException{
        int aux = recuento_local.get(cliente);
        aux+=cantidad;
        recuento_local.put(cliente, aux);
        System.out.println(this.ID+"-> "+cliente+" ha donado "+cantidad+" en este servidor");
    }
}