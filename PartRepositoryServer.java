import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;

public class PartRepositoryServer{

  PartRepositoryServer(String repositoryName, String port){
    try{
      PartRepository pr = new PartRepositoryImplementation();
      pr.setRepositoryName(repositoryName);

      LocateRegistry.createRegistry(Integer.parseInt(port));
      Naming.rebind("//localhost:" + port + "/" + repositoryName, pr);
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }

  public static void main(String[] args){
    try{
      new PartRepositoryServer(args[0], args[1]);
      System.out.println("Repositório " + args[0] + " executando na porta " + args[1]);
    }catch (Exception e){
      if(args.length <= 1){
        System.out.println("Digite um nome e uma porta para o Repositório");
      }
    }

  }
}
