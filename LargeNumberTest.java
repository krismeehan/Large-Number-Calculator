// Student: Kris Meehan
// Professor: Professor Soule
// Class: CSC103
// FILE: LargeNumberTest.java

// This program demonstrates use of the UnboundedInt class. The user
// is prompted to enter 2 numbers of any size and they can be added, 
// multiplied together, or displayed at any time. The user can create and
// display a clone of the first number, or change the current numbers
// at any time. It is assumed the user will not input negative numbers.
// The program is functional with an input of zero.

import java.util.Scanner;
import java.lang.Integer;

public class LargeNumberTest
{
   private static Scanner kb = new Scanner(System.in);   
      
   public static void main(String[ ] args)
   {
      // variables used to test UnboundedInt class
      String number1str = new String();
      String number2str = new String();
      String sumStr = new String();
      String multStr = new String();
      boolean isEqual;      
      int userInput = 0;        
     
      // stores each large number in a string
      System.out.println("Enter the first large number to be stored: ");
      number1str = kb.nextLine();      
      System.out.println("Enter the second large number to be stored: ");
      number2str = kb.nextLine();
      
      // turns string to UnboundedInt objects
      UnboundedInt number1 = new UnboundedInt(number1str);
      UnboundedInt number2 = new UnboundedInt(number2str);
      
      // loops until user inputs 7 to end the program
      while(userInput != 7)
      {
         printMenu();
         userInput = kb.nextInt();
         
         // prints the two UnboundedInt objects
         if (userInput == 1)
         {
            System.out.println("1st number: " + number1.toString());
            System.out.println("2nd number: " + number2.toString());
            System.out.println();
         }
         // user inputs two new strings to be converted to UnboundedInt objects
         else if (userInput == 2)
         {
            kb.nextLine();
            
            System.out.println("Enter the first large number to be stored: ");
            number1str = kb.nextLine();           
            System.out.println("Enter the second large number to be stored: ");
            number2str = kb.nextLine();
            
            number1 = new UnboundedInt(number1str);
            number2 = new UnboundedInt(number2str);
         }
         // checks if the two UnboundedInt objects are equal
         else if (userInput == 3)
         {
            isEqual = number1.equals(number2);
            if (isEqual == true)
               System.out.println("The two numbers are equal.");
            else
               System.out.println("The two numbers are not equal."); 
            System.out.println();             
         }
         // prints the sum of the two UnboundedInt objects
         else if (userInput == 4)
         {
            UnboundedInt sum = number1.add(number2);
            System.out.println("Numbers added: " + sum.toString());
            System.out.println();
         }
         // prints the multiplication of the two UnboundedInt objects
         else if (userInput == 5)
         {
            UnboundedInt mult = number1.multiply(number2);   
            System.out.println("Multiplied: " + mult.toString());
            System.out.println(); 
         }
         // clones and prints the first UnboundedInt object
         else if (userInput == 6)
         {
            UnboundedInt clone = number1.clone();
            System.out.println("First number cloned: " + clone.toString());
            System.out.println();
         }
         // exits the loops, ends the program
         else if (userInput == 7)
         {
            System.out.println("Goodbye.");   
         }
         // tells the user their input was out of menu range
         else
         {
            System.out.println("Menu choice is not in range (1-7)");
            System.out.println();      
         } 
          
      }                       
       
   }
   // this method prints the menu options when called
   public static void printMenu()
   {
      System.out.println("Menu Options:");
      System.out.println("    1. Display both numbers");
      System.out.println("    2. Input two new numbers (without commas)");
      System.out.println("    3. Check if the two numbers are equal");
      System.out.println("    4. Report the sum of the two numbers");
      System.out.println("    5. Report the multiplication of the two numbers");
      System.out.println("    6. Create and output the clone of the first number");
      System.out.println("    7. Quit");
      System.out.println("Enter menu selection:");
   }
}
