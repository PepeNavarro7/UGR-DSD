import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Contador_I extends Remote{
    public void registrar (String nombre) throws RemoteException;
    public void donar (String nombre, int cantidad) throws RemoteException;
    public int total_donado () throws RemoteException;
}
