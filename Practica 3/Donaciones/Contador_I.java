import java.rmi.Remote;
import java.rmi.RemoteException;

// Interfaz con la que el cliente interactúa con los servidores
public interface Contador_I extends Remote{
    public int registrar (String nombre)                throws RemoteException;
    public int donar (String nombre, int cantidad)      throws RemoteException;
    public int total_donado ()                          throws RemoteException;
}
