
import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;


public class ServerConsole implements ChatIF 
{

  final public static int DEFAULT_PORT = 5555;
  
  EchoServer server;
  
  Scanner fromServerConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ServerConsole(EchoServer server) 
  {

	this.server = server;
	this.fromServerConsole = new Scanner(System.in);
    	
    // Create scanner object to read from console
    fromServerConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
      String message;

      while (true) 
      {
        message = fromServerConsole.nextLine();
        server.handleMessageFromServerUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("SERVER MSG> " + message);
  }
}
//End of ConsoleChat class