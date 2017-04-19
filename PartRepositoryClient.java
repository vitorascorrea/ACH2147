import java.rmi.Naming;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.UUID;
import java.rmi.registry.LocateRegistry;


public class PartRepositoryClient{

  public static void main(String[] args){
    try{

      PartRepository pr = (PartRepository) Naming.lookup("//localhost:" + args[1] + "/" + args[0]);
      Part currentPart = null;
      Scanner sc = new Scanner(System.in);

      while(true){
        clearScreen();
        System.out.println("Repositório: " + pr.getRepositoryName());
        if(currentPart != null){
          System.out.println("Peça selecionada: " + currentPart.getPartName() + " - " + currentPart.getPartId());
        }
        System.out.println("///////////////////////////////////////////////");
        System.out.println("O que deseja fazer? (insira o número correspondente)");
        if(currentPart != null){
          System.out.println("[0 - Manipular peça selecionada]");
        }
        System.out.println("[1 - Listar peças do repositório]");
        System.out.println("[2 - Buscar ou selecionar uma peça no repositório]");
        System.out.println("[3 - Adicionar uma peça ao repositório]");
        System.out.println("[4 - Conectar em outro repositório]");
        System.out.println("[5 - Encerrar a sessão]");
        System.out.println("///////////////////////////////////////////////");
        System.out.print(">");
        int option = sc.nextInt();
        System.out.println();
        switch (option) {
          case 0:
            if(currentPart != null){
              clearScreen();
              System.out.println("///////////////////////////////////////////////");
              System.out.println("O que você deseja fazer com a peça selecionada? (insira o número correspondente)");
              System.out.println("[1 - Alterar o nome da peça]");
              System.out.println("[2 - Alterar a descrição da peça]");
              System.out.println("[3 - Manipular subpeças da peça]");
              System.out.println("[4 - Retornar ao menu anterior]");
              System.out.println("///////////////////////////////////////////////");
              System.out.print(">");
              int partOption = sc.nextInt();
              if(partOption == 1){
                System.out.println("///////////////////////////////////////////////");
                System.out.println("Nome atual da peça: " + currentPart.getPartName());
                System.out.println("Novo nome da peça: ");
                sc.nextLine();
                String newPartName = sc.nextLine();
                currentPart.setPartName(newPartName);
              }else if(partOption == 2){
                System.out.println("///////////////////////////////////////////////");
                System.out.println("Descrição atual da peça: " + currentPart.getPartDescription());
                System.out.println("Nova descrição da peça: ");
                sc.nextLine();
                String newPartDescription = sc.nextLine();
                currentPart.setPartDescription(newPartDescription);
              }else if(partOption == 3){
                while(true){
                  clearScreen();
                  System.out.println("///////////////////////////////////////////////");
                  System.out.println("O que deseja fazer com a lista de subpeças da peça " + currentPart.getPartName() + " - " + currentPart.getPartId() + " (insira o número correspondente)");
                  System.out.println("[1 - Listar subpeças]");
                  System.out.println("[2 - Adicionar uma nova subpeça]");
                  System.out.println("[3 - Adicionar uma peça existente como subpeça]");
                  System.out.println("[4 - Retornar ao menu anterior]");
                  System.out.println("///////////////////////////////////////////////");
                  System.out.print(">");
                  int subPartOption = sc.nextInt();
                  if(subPartOption == 1){
                    clearScreen();
                    if(currentPart.getSubParts().size() > 0){
                      System.out.println("///////////////////////////////////////////////");
                      System.out.println("As subpeças são:");
                      System.out.println("ID                   -   Nome da Subpeça   -   Descrição da Subpeça    - Quantidade da Subpeça");
                      int index = 0;
                      for (Part subPart : currentPart.getSubParts()) {
                        System.out.println(subPart.getPartId() + " - " + subPart.getPartName() + " - " + subPart.getPartDescription() + " - " + currentPart.getSubPartQuant(index));
                        index++;
                      }
                    }else{
                      System.out.println("///////////////////////////////////////////////");
                      System.out.println("Não existem subpeças para esta peça");
                    }
                    System.out.println("///////////////////////////////////////////////");
                    System.out.println("Pressione ENTER para voltar.");
                    sc.nextLine();
                    sc.nextLine();
                  }else if(subPartOption == 2){
                    clearScreen();
                    System.out.println("///////////////////////////////////////////////");
                    System.out.println("Criando uma nova subpeça");
                    System.out.println("Nome: ");
                    sc.nextLine();
                    String subPartName = sc.nextLine();
                    System.out.println("Descrição: ");
                    String subPartDescription = sc.nextLine();
                    System.out.println("Quantidade: ");
                    int subPartQuant = sc.nextInt();
                    currentPart.pushNewSubPart(subPartName, subPartDescription, subPartQuant);
                  }else if(subPartOption == 3){
                    clearScreen();
                    System.out.println("///////////////////////////////////////////////");
                    System.out.println("Qual o repositório em que a peça está localizada?");
                    sc.nextLine();
                    String subPartRep = sc.nextLine();
                    System.out.println("Qual o número da porta do repositório em que a peça está localizada?");
                    String subPartRepPort = sc.nextLine();
                    try{
                      PartRepository tempPr = (PartRepository) Naming.lookup("//localhost:" + subPartRepPort + "/" + subPartRep);
                      System.out.println("As peça encontradas neste repositório foram: ");
                      System.out.println("ID                   -   Nome da Peça   -   Descrição da Peça");
                      for (Part part : tempPr.getRepositoryParts()) {
                        System.out.println(part.getPartId() + " - " + part.getPartName() + " - " + part.getPartDescription());
                        if(part.getSubParts().size() > 0){
                          System.out.println("> Subpeças de " + part.getPartName());
                          System.out.println("> ID                   -   Nome da Subpeça   -   Descrição da Subpeça");
                          for (Part subPart : part.getSubParts()) {
                            System.out.println("> " + subPart.getPartId() + " - " + subPart.getPartName() + " - " + subPart.getPartDescription());
                          }
                        }
                      }
                      System.out.println("Digite o ID da peça: ");
                      String subPartId = sc.nextLine();
                      Part foundedPart = findPartById(subPartId, tempPr.getRepositoryParts());
                      if(foundedPart == null){
                        for (Part part : tempPr.getRepositoryParts()) {
                          if(part.getSubParts().size() > 0 && findPartById(subPartId, part.getSubParts()) != null){
                            foundedPart = findPartById(subPartId, part.getSubParts());
                          }
                        }
                      }
                      if(foundedPart != null && !currentPart.getPartId().toString().equals(foundedPart.getPartId().toString())){
                        System.out.print("Digite a quantidade: ");
                        int tempSubPartQuant = sc.nextInt();
                        currentPart.pushExistingSubPart(foundedPart, tempSubPartQuant);
                      }else{
                        System.out.println("///////////////////////////////////////////////");
                        System.out.println("Peça não encontrada ou tentativa de peça ser subpeça direta de si mesma.");
                      }
                    }catch(Exception e){
                      clearScreen();
                      System.out.println("///////////////////////////////////////////////");
                      System.out.println("Não foi encontrado nenhum repositorio com esses parâmetros ou nenhuma peça.");
                    }
                    System.out.println("///////////////////////////////////////////////");
                    System.out.println("Pressione ENTER para voltar.");
                    sc.nextLine();
                    sc.nextLine();
                  }else{
                    break;
                  }
                }
              }
            }
            break;
          case 1:
            clearScreen();
            if(pr.getRepositoryParts().size() > 0){
              System.out.println("///////////////////////////////////////////////");
              System.out.println("As peças contidas neste repositório são:");
              System.out.println("ID                   -   Nome da Peça   -   Descrição da Peça");
              for (Part part : pr.getRepositoryParts()) {
                System.out.println(part.getPartId() + " - " + part.getPartName() + " - " + part.getPartDescription());
                if(part.getSubParts().size() > 0){
                  System.out.println("> Subpeças de " + part.getPartName());
                  System.out.println("> ID                   -   Nome da Subpeça   -   Descrição da Subpeça - Quantidade da Subpeça");
                  int index = 0;
                  for (Part subPart : part.getSubParts()) {
                    System.out.println("> " + subPart.getPartId() + " - " + subPart.getPartName() + " - " + subPart.getPartDescription() + " - " + part.getSubPartQuant(index));
                    index++;
                  }
                }
              }
            }else{
              System.out.println("///////////////////////////////////////////////");
              System.out.println("O repositório está vazio");
            }
            System.out.println("///////////////////////////////////////////////");
            System.out.println("Pressione ENTER para voltar.");
            sc.nextLine();
            sc.nextLine();
            break;
          case 2:
            clearScreen();
            System.out.println("///////////////////////////////////////////////");
            System.out.println("Pesquisar peça por (insira o número correspondente):");
            System.out.println("[1 - ID (a peça será selecionada)]");
            System.out.println("[2 - Nome]");
            System.out.println("[3 - Retornar ao menu anterior]");
            System.out.println("///////////////////////////////////////////////");
            System.out.print(">");
            int partSearch = sc.nextInt();
            if(partSearch == 1){
              System.out.println("///////////////////////////////////////////////");
              System.out.println("As peças contidas neste repositório são:");
              System.out.println("ID                   -   Nome da Peça   -   Descrição da Peça");
              for (Part part : pr.getRepositoryParts()) {
                System.out.println(part.getPartId() + " - " + part.getPartName() + " - " + part.getPartDescription());
                if(part.getSubParts().size() > 0){
                  System.out.println("> Subpeças de " + part.getPartName());
                  System.out.println("> ID                   -   Nome da Subpeça   -   Descrição da Subpeça - Quantidade da Subpeça");
                  int index = 0;
                  for (Part subPart : part.getSubParts()) {
                    System.out.println("> " + subPart.getPartId() + " - " + subPart.getPartName() + " - " + subPart.getPartDescription() + " - " + part.getSubPartQuant(index));
                    index++;
                  }
                }
              }
              System.out.println("///////////////////////////////////////////////");
              System.out.println("Digite o ID da peça: ");
              sc.nextLine();
              String partId = sc.nextLine();
              currentPart = findPartById(partId, pr.getRepositoryParts());
            }else if(partSearch == 2){
              System.out.println("///////////////////////////////////////////////");
              System.out.println("Digite o Nome da peça: ");
              sc.nextLine();
              String partName = sc.nextLine();
              findPartByName(partName, pr.getRepositoryParts());
              System.out.println("///////////////////////////////////////////////");
              System.out.println("Pressione ENTER para voltar.");
              sc.nextLine();
              sc.nextLine();
            }
            System.out.println();
            break;
          case 3:
            clearScreen();
            System.out.println("///////////////////////////////////////////////");
            System.out.println("Criando uma nova peça");
            System.out.println("Nome: ");
            sc.nextLine();
            String partName = sc.nextLine();
            System.out.println("Descrição: ");
            String partDescription = sc.nextLine();
            currentPart = pr.pushNewPart(partName, partDescription);
            break;
          case 4:
            clearScreen();
            System.out.println("///////////////////////////////////////////////");
            System.out.println("Insira o nome do repositório à ser conectado: ");
            String repName = sc.next();
            System.out.println("Insira o número da porta do repositório à ser conectado: ");
            int repPort = sc.nextInt();
            boolean success = false;
            try{
              PartRepository check = (PartRepository) Naming.lookup("//localhost:" + repPort + "/" + repName);
              success = true;
            }catch(Exception e){
              System.out.println("///////////////////////////////////////////////");
              System.out.println("Não foi encontrado nenhum repositorio com esses parâmetros. Voltando ao repositório anterior.");
              System.out.println("///////////////////////////////////////////////");
              System.out.println("Pressione ENTER para voltar.");
              sc.nextLine();
              sc.nextLine();
            }finally{
              if(success) pr = (PartRepository) Naming.lookup("//localhost:" + repPort + "/" + repName);
            }
            break;
          case 5:
            return;
          default:
            System.out.println("///////////////////////////////////////////////");
            System.out.println("Opção inválida");
            System.out.println();
            break;
        }

      }
    }
    catch (Exception e){
      System.out.println(e);
      System.out.println("Não foi inserido repositório ou porta válidos.");
    }
  }

  public static Part findPartById(String partId, ArrayList<Part> partList){
    try{
      for (Part part : partList) {
        if(part.getPartId().toString().equals(partId)){
          return part;
        }
      }
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
        System.out.println("Não foi encontrado nenhuma peça com o Nome " + partName);
      }
    }catch (Exception e){

    }
  }

  public static void clearScreen(){
    try{
      System.out.print("\033[H\033[2J");
      System.out.flush();
    }catch (Exception e){
      System.out.println();
    }
  }


}
