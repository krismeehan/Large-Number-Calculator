// Student: Kris Meehan
// Professor: Professor Soule
// Class: CSC103
// File: UnboundedInt.java 

/******************************************************************************
* @author Kris Meehan
* This class is for project 2;
* An UnboundedInt is a collection of three digit Nodes able to hold a positive
* Integer of any size. The constructor takes a String of numbers.
*
* There is a front Node, a back Node, and a cursor Node pointing at the current
* Node. The cursor position can be changed or value returned through the following 
* methods (start, advance and getNodeValue).  
* 
* Two UnboundedInt objects can be added or multiplied together, using the "add"
* and "multiply" methods.
* 
* @note
*    Assistance building methods from "Data Structures & Other Objects Using JAVA"
*    by Michael Main, from Oracle.com for String and Int class methods, and from
*    "stackoverflow.com/questions/6034523/format-an-integer-using-java-string-format"
*    for formatting an Integer to 3 digits.

******************************************************************************/
import java.lang.Integer;
import java.lang.String;


public class UnboundedInt implements Cloneable
{
   // Invariant of the UnboundedInt class:
   //   1. How many Nodes are required to store the UnboundedInt.
   //   2. The front node in the UnboundedInt (least significant digit).
   //   3. The back node in the UnboundedInt (most significant digit).
   //   4. The cursor representing the current Node.
   private int manyNodes;
   private IntNode front;
   private IntNode back;
   private IntNode cursor;   

   /**
   * Initialize an UnboundedInt object based on the String provided.
   * Numbers are stored in three digit Nodes, the first Node representing
   * the LSB and the back Node being the MSB.
   * @param digitString
   *   The String of digits to be stored. 
   * @postcondition
   *   Nodes containing the String of numbers have been added to 
   *   the UnboundedInt
   * @note
   *   It is assumed only positive Integers will be passed in   
   **/   
   public UnboundedInt(String digitString)
   {   
      // index variables for current substring to add to Node and leading zero counter
      int subStart = 0;
      int subEnd = subStart + 3;
      int subFirst = 3;
      int subInt;      
      int leadingZeros = 0;
      
      String sub = new String();
      String digitNoLeadZero = new String(digitString);
      
      // If passed in string has leading zeros, remove them
      for (int i = 0; (digitNoLeadZero.charAt(i) == '0') && (i < digitNoLeadZero.length()-1); i++)
      {
         leadingZeros++;   
      }
      digitNoLeadZero = digitNoLeadZero.substring(leadingZeros, digitNoLeadZero.length());   
      
      manyNodes = (digitNoLeadZero.length() / 3) + 1;           
      
      // determine number of digits in first substring based on digits in number
      if ((digitNoLeadZero.length() % 3) == 1)     
         subFirst = 1;   
 
      else if ((digitNoLeadZero.length() % 3) == 2)      
         subFirst = 2;      
      else
         manyNodes -= 1;        
      sub = digitNoLeadZero.substring(subStart, subFirst);
      
      subInt = Integer.parseInt(sub);         
      front = new IntNode(subInt, null);
      
      // set first Node to "back", as each following Node will be placed in front
      back = front;
      
      subStart += subFirst;
      subEnd += subFirst;
      
      // loops as many times as Nodes required. Takes current substring,
      // adds the Integer value to a Node placed at the front of UnboundedInt
      // and increments for the substring to add next.   
      for (int j = 1; j < manyNodes; j++)
      {
         // dont allow substring length to be more than string length
         if (subEnd > digitNoLeadZero.length())
            subEnd = digitNoLeadZero.length();
            
         sub = digitNoLeadZero.substring(subStart, subEnd);
         subInt = Integer.parseInt(sub);
         
         front = new IntNode(subInt, front);                     
         
         subStart+=3;
         subEnd+=3;                 
      }              
   }
   
   /**
   * Add an UnboundedInt to the UnboundedInt the method was called on.
   * @param toAdd
   *   The UnboundedInt to add to the current UnboundedInt.
   * @return
   *   The return value is an UnboundedInt containing the addition of
   *   the two UnboundedInts. 
   * @postcondition
   *   The sum of the two UnboundedInts has been returned.  
   **/
   public UnboundedInt add(UnboundedInt toAdd)
   {
      int carry = 0;
      int nodeSum;
      String strSum = new String();
      String tempStr = new String(); 
      
      // start both cursors at front (lowest weight numbers)      
      start();
      toAdd.start();     
      
      // loop until either UnboundInt objects are at last node
      for (int i = 0; (i < manyNodes) || (i < toAdd.manyNodes); i++)
      {
         // Add only the value of passed in UnboundInt node if current cursor is null
         // Advance cursor of passed in UnboundInt object 
         if (cursor == null)
         {
            nodeSum = toAdd.getNodeValue();
            toAdd.advance();
         }
         // Add only the value of current UnboundInt node if passed in cursor is null
         // Advance cursor of current UnboundInt object 
         else if(toAdd.cursor == null)
         {
            nodeSum = getNodeValue();
            advance();
         }
         // Add both values of UnboundInt object nodes
         // Advance cursors of passed in and current UnboundInt object
         else
         {
            nodeSum = getNodeValue() + toAdd.getNodeValue();
            advance();
            toAdd.advance();
         }          
         if (carry == 1)
         {
            nodeSum += 1;
            carry = 0;
         }   
         if (nodeSum >= 1000)
         {
            nodeSum -= 1000;
            carry = 1;
         } 
         // add node sum to the String representing total sum 
         tempStr = String.format("%03d", nodeSum);       
         strSum = tempStr + strSum;  
      }
      // if there is a carry after loop is done, add 1 at the front of sum 
      if (carry == 1)
         strSum = "1" + strSum;      
      
      UnboundedInt sum = new UnboundedInt(strSum);
      return sum;  
   }
   
   /**
   * Multiply an UnboundedInt to the UnboundedInt the method was called on.
   * @param toMultiply
   *   The UnboundedInt to multiply to the current UnboundedInt.
   * @return
   *   The return value is an UnboundedInt containing the multiplication of
   *   the two UnboundedInts. 
   * @postcondition
   *   The multiplication of the two UnboundedInts has been returned.  
   **/
   public UnboundedInt multiply(UnboundedInt toMultiply)
   {
      // get String values of the UnboundedInt objects to multiply
      String passedTemp = new String(toMultiply.toStringNoCommas());
      String currentTemp = new String(toStringNoCommas());
      
      // initialize UnboundedInt objects with a value of 0
      UnboundedInt toAddNum = new UnboundedInt("0");
      UnboundedInt mult = new UnboundedInt("0");
      
      // String variables for holding the current number of the passed
      // in and current UnboundedInts, the current numbers calculated
      // and the total of the number to be added.
      String strToAdd = new String();
      String strToAddTemp = new String();
      String passedStr = new String();
      String currentStr = new String();      
      
      int currentInt;      
      int passedInt;      
      int toAddInt;
      int carry = 0;
      int trailZeros = 0;
      
      // loop through each number of passed in UnboundedInt from LSB to MSB
      for (int i = passedTemp.length(); i > 0; i--)
      {
         passedStr = passedTemp.substring(i-1, i);
         passedInt = Integer.parseInt(passedStr);
         
         // only calculate number to add if number to multiply is not zero
         if (passedInt != 0)
         {
            strToAdd = "";
            // loop through current UnboundedInt from LSB to MSB
            // Multiply each number by the current number of passed in UnboundedInt 
            for (int j = currentTemp.length(); j > 0; j--)
            {              
               currentStr = currentTemp.substring(j-1, j);
               currentInt = Integer.parseInt(currentStr);
               
               // add carry to current value and reset carry to zero
               toAddInt = (passedInt*currentInt) + carry;
               carry = 0;
               
               if (toAddInt >= 10)
               {
                  // calculate carry and number to add
                  carry = toAddInt / 10;
                  toAddInt = toAddInt % 10;
               }
               // add newest number to front of String
               strToAddTemp = Integer.toString(toAddInt);
               strToAdd = strToAddTemp + strToAdd;
            }
            if (carry != 0)
            {
               // add leftover carry to front of String
               strToAdd = Integer.toString(carry) + strToAdd;
               carry = 0;
            }
            for (int k = 0; k < trailZeros; k++)
               // add trailing zeros for each additional number to add
               strToAdd = strToAdd.concat("0");
            
            // if this is the first run of loop...   
            if (i == passedTemp.length())
               mult = new UnboundedInt(strToAdd);
            else
            {
               toAddNum = new UnboundedInt(strToAdd);
               mult = mult.add(toAddNum);
            }
         }
         trailZeros++;
      }            
      return mult;
   }
   
   /**
   * Set the cursor to the front of the UnboundedInt called on.
   * @param - none
   * @postcondition
   *   The front Node is now the current Node.
   **/   
   public void start()
   {
      cursor = front;
   }   
   
   /**
   * Set the cursor to the next Node of the UnboundedInt called on.
   * @param - none
   * @precondition
   *   The cursor is not null, meaning there is another Node to advance to. 
   * @postcondition
   *   The cursor has been set to the next Node if there is one.
   * @exception IllegalStateException
   *   Indicates that there is not Node to advance to.
   **/
   public void advance()
   {
      if (cursor != null)
         cursor = cursor.getLink();
         
      else         
         throw new IllegalStateException("There is no current cursor.");
   }
      
   /**
   * Get the value of the current Node in the UnboundedInt.
   * @param - none
   * @precondition
   *   The cursor is not pointing to a Node. 
   * @postcondition
   *   The data from the current Node has been returned.
   * @exception IllegalStateException
   *   Indicates that the cursor does not point to a Node.
   * @return
   *   The data at the current Node.
   **/
   public int getNodeValue()
   {
      if (cursor instanceof IntNode)
         return cursor.getData();
         
      else   
         throw new IllegalStateException("Cursor is not pointing to a Node.");      
   }
   
   /**
   * Generate a copy of this UnboundedInt.
   * @param - none
   * @exception OutOfMemoryError
   *   Indicates insufficient memory for creating the clone.
   * @return
   *   The return value is a copy of this UnboundedInt. Subsequent changes to the
   *   copy will not affect the original, nor vice versa.
   **/
   public UnboundedInt clone( )
   {  // Clone a DoubleArraySeq object.
      UnboundedInt answer;
           
      try
      {
         answer = (UnboundedInt) super.clone( );
      }
      catch (CloneNotSupportedException e)
      {  // This exception should not occur. But if it does, it would probably
         // indicate a programming error that made super.clone unavailable.
         // The most common error would be forgetting the "Implements Cloneable"
         // clause at the start of this class.
         throw new RuntimeException
         ("This class does not implement Cloneable");
      }      
      
      return answer;
   }
   /**
   * Return a boolean true or false based on whether the passed in object
   * equals the current object.
   * @param obj
   *   The object being compared to the current object. 
   * @postcondition
   *   Each element of the two objects have been compared. True has been returned
   *   if each element was the same, false if there was a discrepancy. 
   * @return
   *   a boolean to tell whether the objects are equal.
   **/
   public boolean equals(Object obj)
   {
      // check that object is an UnboundedInt.
      if (obj instanceof UnboundedInt)
      {
         UnboundedInt toCheck = (UnboundedInt) obj;
         // check if Node size of UnboundedInts are the same.
         if (toCheck.manyNodes == manyNodes)
         {
            start();
            toCheck.start();
            // for each Node, check if values are the same
            // return false as soon as a set of values do not match
            for (int i = 0; i < manyNodes; i++)
            {
               if (getNodeValue() != toCheck.getNodeValue())
                  return false;
               advance();
               toCheck.advance();
            }
            return true;
         }
         else
            return false;        
      }
      else
         return false;  
   }         
   /**
   * Return a string containing the contents of the UnboundedInt in order,
   * separated by commas every 3 digits from the back.
   * @param - none 
   * @postcondition
   *   The elements of the UnboundedInt have been added to a string and returned.
   * @exception IllegalStateException
   *   Indicates that the UnboundedInt is empty, so toString may not be called. 
   * @return
   *   a string containing the elements of the UnboundedInt.
   **/
   public String toString()
   {
      if (manyNodes == 0)
         throw new IllegalStateException ("The sequence is empty.");
         
      String temp = new String();
      String number = new String();
      
      start();
      
      
      for (int i = 0; i < manyNodes; i++)
      {         
         number = "," + number;
        
         if (i+1 == manyNodes)
         {
            back = cursor;
            temp = Integer.toString(getNodeValue()); 
         }   
         else
            temp = String.format("%03d", getNodeValue());   
            
         number = temp + number;                                              
         advance();     
      }
      number = number.substring(0, number.length()-1);
      
      return number;   
   }
   /**
   * Return a string containing the contents of the UnboundedInt in order,
   * without commas.
   * @param - none 
   * @postcondition
   *   The elements of the UnboundedInt have been added to a string and returned.
   * @exception IllegalStateException
   *   Indicates that the UnboundedInt is empty, so toString may not be called. 
   * @return
   *   a string containing the elements of the UnboundedInt.
   **/
   public String toStringNoCommas()
   {
      if (manyNodes == 0)
         throw new IllegalStateException ("The sequence is empty.");
         
      String temp = new String();
      String number = new String();
      
      start();     
      
      for (int i = 0; i < manyNodes; i++)
      {         
        
         if (i+1 == manyNodes)
         {
            back = cursor;
            temp = Integer.toString(getNodeValue()); 
         }   
         else
            temp = String.format("%03d", getNodeValue());   
            
         number = temp + number;                                              
         advance();     
      }      
      return number;   
   }    
} //end UnboundedInt class
           
