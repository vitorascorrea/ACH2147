import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface PartRepository extends Remote{

  public String getRepositoryName() throws RemoteException;
  public void setRepositoryName(String name) throws RemoteException;

  public ArrayList<Part> getRepositoryParts() throws RemoteException;
  public Part pushNewPart(String name, String description) throws RemoteException;
}
