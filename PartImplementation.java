import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.UUID;

public class PartImplementation extends UnicastRemoteObject implements Part{

  UUID id = UUID.randomUUID();
  String name;
  String description;
  ArrayList<Part> subParts = new ArrayList<Part>();
  ArrayList<Integer> subPartsQuant = new ArrayList<Integer>();

  protected PartImplementation() throws RemoteException{
    super();
  }

  public UUID getPartId() throws RemoteException{
    return this.id;
  }

  public String getPartName() throws RemoteException{
    return this.name;
  }

  public void setPartName(String name) throws RemoteException{
    this.name = name;
  }

  public String getPartDescription() throws RemoteException{
    return this.description;
  }

  public void setPartDescription(String description) throws RemoteException{
    this.description = description;
  }

  public ArrayList<Part> getSubParts() throws RemoteException{
    return this.subParts;
  }

  public int getSubPartQuant(int index) throws RemoteException{
    return this.subPartsQuant.get(index);
  }

  public void pushExistingSubPart(Part part, int quant) throws RemoteException{
    this.subParts.add(part);
    this.subPartsQuant.add(quant);
  }

  public void pushNewSubPart(String name, String description, int quant) throws RemoteException{
    Part newSubPart = new PartImplementation();
    newSubPart.setPartName(name);
    newSubPart.setPartDescription(description);
    this.subParts.add(newSubPart);
    this.subPartsQuant.add(quant);
  }
}
