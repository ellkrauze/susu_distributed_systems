import java.rmi.RemoteException;

public class AdditionImplementation implements AdditionInterface{
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }       
}
