import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Servidor_I extends Remote{
    public void registrar () throws RemoteException;
    public void donar () throws RemoteException;
    public int total_donado () throws RemoteException;
}
