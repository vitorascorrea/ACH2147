import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalculatorImplementation extends UnicastRemoteObject implements Calculator
{
    protected CalculatorImplementation() throws RemoteException
    {
        super();
    }
    public long add(long a, long b) throws RemoteException
    {
        return a+b;
    }
}
