import java.rmi.Naming;
import java.util.Scanner;


public class CalculatorClient
{
  public static void main(String[] args)
  {
    try
    {
      Calculator c = (Calculator) Naming.lookup("//localhost:2020/CalculatorService");
      while(true){
        Scanner sc = new Scanner(System.in);
        System.out.println("Vamos fazer uma adição");
        System.out.println("Digite o primeiro número:");
        int num1 = sc.nextInt();
        System.out.println("Digite o segundo número:");
        int num2 = sc.nextInt();
        System.out.println("Adição : " + c.add(num1, num2));
      }
    }
    catch (Exception e)
    {
      System.out.println(e);
    }
  }
}
