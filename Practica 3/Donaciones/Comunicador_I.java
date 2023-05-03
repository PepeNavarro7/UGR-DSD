import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Comunicador_I extends Remote{
    public void setPareja(String id) throws RemoteException;
    public int subtotal() throws RemoteException;
}
