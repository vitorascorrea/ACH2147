import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PartRepositoryImplementation extends UnicastRemoteObject implements PartRepository{

  String name;
  ArrayList<Part> parts = new ArrayList<Part>();

  protected PartRepositoryImplementation() throws RemoteException{
    super();
  }

  public String getRepositoryName() throws RemoteException{
    return this.name;
  }

  public void setRepositoryName(String name) throws RemoteException{
    this.name = name;
  }

  public ArrayList<Part> getRepositoryParts() throws RemoteException{
    return this.parts;
  }

  public Part pushNewPart(String name, String description) throws RemoteException{
    Part newPart = new PartImplementation();
    newPart.setPartName(name);
    newPart.setPartDescription(description);
    this.parts.add(newPart);
    return newPart;
  }
}
