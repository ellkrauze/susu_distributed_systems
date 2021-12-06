package game;

import java.io.IOException;
import java.rmi.*;

public interface ClientInterface extends Remote {
    public void tell(String name) throws RemoteException;
    public void logout() throws RemoteException;
    public String getName() throws RemoteException;
    public void unmute() throws IOException;
}