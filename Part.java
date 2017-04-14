import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;
import java.util.ArrayList;

public interface Part extends Remote{

  public UUID getPartId() throws RemoteException;

  public String getPartName() throws RemoteException;
  public void setPartName(String name) throws RemoteException;

  public String getPartDescription() throws RemoteException;
  public void setPartDescription(String description) throws RemoteException;

  public ArrayList<Part> getSubParts() throws RemoteException;
  public void pushNewSubPart(String name, String description) throws RemoteException;
  public void pushExistingSubPart(Part part) throws RemoteException;

}
