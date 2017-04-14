import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;

public class CalculatorServer
{
  CalculatorServer()
  {
    try
    {
      Calculator c = new CalculatorImplementation();
      LocateRegistry.createRegistry(2020);
      Naming.rebind("//localhost:2020/CalculatorService", c);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  public static void main(String[] args)
  {
    new CalculatorServer();
  }
}
