import java.util.Scanner;

public class TestProgram
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        
        // Simple program that reads comma-separated values
        // Example input: "5,10" will read 5 and 10
        String input = scanner.nextLine();
        String[] parts = input.split(",");
        
        int num1 = Integer.parseInt(parts[0].trim());
        int num2 = Integer.parseInt(parts[1].trim());
        
        int sum = num1 - num2;
        System.out.println(sum);
        
        scanner.close();
    } 
}

