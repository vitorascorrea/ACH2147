import java.rmi.Naming;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.UUID;
import java.rmi.registry.LocateRegistry;


public class PartRepositoryClient{

  public static void main(String[] args){

    try{
      PartRepository pr = (PartRepository) Naming.lookup("//localhost:" + args[1] + "/" + args[0]);
      Part foundedPart = null;
      Scanner sc = new Scanner(System.in);
      while(true){
        System.out.println();
        System.out.println("Repositório: " + pr.getRepositoryName());
        if(foundedPart != null){
          System.out.println("Peça selecionada: " + foundedPart.getPartName() + " - " + foundedPart.getPartId());
        }
        System.out.println();
        System.out.println("O que deseja fazer? (insira o número correspondente)");
        if(foundedPart != null){
          System.out.println("[0 - Manipular peça selecionada]");
        }
        System.out.println("[1 - Listar peças do repositório]");
        System.out.println("[2 - Buscar ou selecionar uma peça no repositório]");
        System.out.println("[3 - Adicionar uma peça ao repositório]");
        System.out.println("[4 - Alterar o nome do repositório]");
        System.out.println("[5 - Conectar em outro repositório]");
        System.out.println("[6 - Encerrar a sessão]");
        System.out.println();
        int option = sc.nextInt();
        System.out.println();
        switch (option) {
            case 0:
              if(foundedPart != null){
                System.out.println("O que você deseja fazer com a peça selecionada?");
                System.out.println("[1 - Alterar o nome da peça]");
                System.out.println("[2 - Alterar a descrição da peça]");
                System.out.println("[3 - Manipular subpeças da peça]");
                System.out.println("[4 - Retornar ao menu anterior]");
                int partOption = sc.nextInt();
                if(partOption == 1){
                  System.out.println("Nome atual da peça: " + foundedPart.getPartName());
                  System.out.print("Novo nome da peça: ");
                  String newPartName = sc.next();
                  foundedPart.setPartName(newPartName);
                }else if(partOption == 2){
                  System.out.println("Descrição atual da peça: " + foundedPart.getPartDescription());
                  System.out.print("Nova descrição da peça: ");
                  String newPartDescription = sc.next();
                  foundedPart.setPartDescription(newPartDescription);
                }else if(partOption == 3){
                  while(true){
                    System.out.println("O que deseja fazer com a lista de subpeças da peça " + foundedPart.getPartName() + " - " + foundedPart.getPartId());
                    System.out.println("[1 - Listar subpeças]");
                    System.out.println("[2 - Adicionar uma nova subpeça]");
                    System.out.println("[3 - Adicionar uma peça existente como subpeça]");
                    System.out.println("[4 - Retornar ao menu anterior]");
                    int subPartOption = sc.nextInt();
                    if(subPartOption == 1){
                      if(foundedPart.getSubParts().size() > 0){
                        System.out.println("As subpeças são:");
                        System.out.println("ID                   -   Nome da Subpeça   -   Descrição da Subpeça");
                        for (Part subPart : foundedPart.getSubParts()) {
                          System.out.println(subPart.getPartId() + " - " + subPart.getPartName() + " - " + subPart.getPartDescription());
                        }
                      }else{
                        System.out.println("Não existem subpeças para esta peça");
                      }
                    }else if(subPartOption == 2){
                      System.out.println("Criando uma nova subpeça");
                      System.out.print("Nome: ");
                      String subPartName = sc.next();
                      System.out.print("Descrição: ");
                      String subPartDescription = sc.next();
                      foundedPart.pushNewSubPart(subPartName, subPartDescription);
                      System.out.println();
                    }else if(subPartOption == 3){
                      System.out.println("Qual o repositório em que a peça está localizada?");
                      String subPartRep = sc.next();
                      System.out.println("Qual o número da porta do repositório em que a peça está localizada?");
                      String subPartRepPort = sc.next();
                      try{
                        PartRepository tempPr = (PartRepository) Naming.lookup("//localhost:" + subPartRepPort + "/" + subPartRep);
                        System.out.print("Digite o ID da peça: ");
                        String subPartId = sc.next();
                        if(!foundedPart.getPartId().toString().equals(findPartById(subPartId, tempPr.getRepositoryParts()).getPartId().toString())){
                          foundedPart.pushExistingSubPart(findPartById(subPartId, tempPr.getRepositoryParts()));
                        }else{
                          System.out.println();
                          System.out.println("Uma peça não pode ser subpeça direta de si mesma.");
                          System.out.println();
                        }
                      }catch(Exception e){
                        System.out.println("Não foi encontrado nenhum repositorio com esses parâmetros ou nenhuma peça.");
                      }
                    }else{
                      break;
                    }
                  }
                }
              }
              break;
            case 1:
              if(pr.getRepositoryParts().size() > 0){
                System.out.println("As peças contidas neste repositório são:");
                System.out.println("ID                   -   Nome da Peça   -   Descrição da peça");
                for (Part part : pr.getRepositoryParts()) {
                  System.out.println(part.getPartId() + " - " + part.getPartName() + " - " + part.getPartDescription());
                }
              }else{
                System.out.println("O repositório está vazio");
              }
              break;
            case 2:
              System.out.println("Pesquisar peça por:");
              System.out.println("[1 - ID (a peça será selecionada)]");
              System.out.println("[2 - Nome]");
              System.out.println("[3 - Retornar ao menu anterior]");
              int partSearch = sc.nextInt();
              if(partSearch == 1){
                System.out.print("Digite o ID da peça: ");
                String partId = sc.next();
                foundedPart = findPartById(partId, pr.getRepositoryParts());
              }else if(partSearch == 2){
                System.out.print("Digite o Nome da peça: ");
                String partName = sc.next();
                findPartByName(partName, pr.getRepositoryParts());
              }
              System.out.println();
              break;
            case 3:
              System.out.println("Criando uma nova peça");
              System.out.print("Nome: ");
              String partName = sc.next();
              System.out.print("Descrição: ");
              String partDescription = sc.next();
              foundedPart = pr.pushNewPart(partName, partDescription);
              System.out.println();
              break;
            case 4:
              System.out.println("Qual o nome novo do repositório?");
              String name = sc.next();
              pr.setRepositoryName(name);
              System.out.println();
              break;
            case 5:
              System.out.print("Insira o nome do repositório à ser conectado: ");
              String repName = sc.next();
              System.out.print("Insira o número da porta do repositório à ser conectado: ");
              int repPort = sc.nextInt();
              try{
                pr = (PartRepository) Naming.lookup("//localhost:" + repPort + "/" + repName);
              }catch(Exception e){
                System.out.println("Não foi encontrado nenhum repositorio com esses parâmetros");
              }
              break;
            case 6:
              return;
            default:
              System.out.println("Opção inválida");
              System.out.println();
              break;
        }

      }
    }
    catch (Exception e){
      System.out.println("Não foi inserido repositório ou porta válidos.");
    }
  }

  public static Part findPartById(String partId, ArrayList<Part> partList){
    try{
      for (Part part : partList) {
        if(part.getPartId().toString().equals(partId)){
          System.out.println("ID                   -   Nome da Peça   -   Descrição da peça");
          System.out.println(part.getPartId() + " - " + part.getPartName() + " - " + part.getPartDescription());
          return part;
        }
      }
      System.out.print("Não foi encontrado nenhuma peça com o ID " + partId);
      return null;
    }catch (Exception e){
      return null;
    }
  }

  public static void findPartByName(String partName, ArrayList<Part> partList){
    try{
      boolean found = false;
      boolean printTableHeader = false;

      for (Part part : partList) {
        if(part.getPartName().contains(partName)){
          found = true;
          if(found && !printTableHeader){
            printTableHeader = true;
            System.out.println("ID                   -   Nome da Peça   -   Descrição da peça");
          }
          System.out.println(part.getPartId() + " - " + part.getPartName() + " - " + part.getPartDescription());

        }
      }
      if(!found){
        System.out.print("Não foi encontrado nenhuma peça com o Nome " + partName);
      }
    }catch (Exception e){

    }
  }


}
