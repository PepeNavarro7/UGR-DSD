import java.rmi.Remote;
import java.rmi.RemoteException;

// Interfaz que permite la comunicación entre los servidores
public interface Comunicador_I extends Remote{
    public void setPareja(String id)                throws RemoteException;
    public int subtotal()                           throws RemoteException;
    public boolean tieneCliente(String cliente)     throws RemoteException;
    public int numClientes()                        throws RemoteException;
    public void registra(String cliente)            throws RemoteException;
    public String getID()                           throws RemoteException;
    public void dona(String cliente, int cantidad)  throws RemoteException;
}
