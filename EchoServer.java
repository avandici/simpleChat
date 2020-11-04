// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the server.
   *
   * @param msg The message received from the server.
   */
  public void handleMessageFromServerUI(String message) {
    try
    {
    	if(message.charAt(0)=='#') {
    		message = message.substring(1,message.length());

    		//Terminate server
    		if(message.equals("quit")) {
    			System.out.println("Quitting...");
    			close();
    		}

    		//Server stops listening for new clients
    		else if(message.equals("stop")){
    			System.out.println("Stopped listening for new clients");
    			stopListening();

    		}
    		
    		//Close server
    		else if(message.equals("close")){
    			System.out.println("Closing Server...");
    			stopListening();
    			close();
    			
    		}

    		//Sets the server port
    		else if(message.startsWith("setport")){
    			if(this.isListening()==true) {
    				System.out.println("Command Denied: Server must be closed");
    			} else {
    				System.out.println("Setting port...");
    				int newPort = Integer.parseInt(message.split(" ")[1]);
    				try {
    					setPort(newPort);
    					System.out.println("New Port: " + newPort);
    				}catch(Exception e){
    					System.out.print("Unable to connect to Port: "+ newPort);
    				}
    			}
    		}

    		//starts the server
    		else if(message.equals("start")){
    			if(this.isListening()==false) {
    				System.out.println("Command Denied: Server must be stopped");
    			} else {
    				System.out.println("Listening...");
    				try {
    					listen();
    					System.out.println("Server Listening");
    				}catch(IOException e) {
    					System.out.println("Unable to listen");
    				}
    				//System.out.println("Logged in as: " + this.loginID);
    			}
    		}


    		//Displays the current port number
    		else if(message.equals("getport")){
    			System.out.println("Current port number: "+Integer.toString(this.getPort()));
    		}

    		//If no valid command recognize
    		else{
    			System.out.println("Invalid Command");
    		}
    	}else {
    		sendToAllClients("FROM SERVER> "+message);
    	}
    	
    }
    catch(IOException e)
    {
        System.out.println("Could not send message to clients");
    }
  }
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("New client connected to the server");
  }
  
  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("Client disconnected from server");
  }
  
  /**
   * Hook method called each time an exception is thrown in a
   * ConnectionToClient thread.
   * The method may be overridden by subclasses but should remains
   * synchronized.
   *
   * @param client the client that raised the exception.
   * @param Throwable the exception thrown.
   */
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
	  System.out.println("Client disconnected form server");
  }
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
      ServerConsole serverConsole = new ServerConsole(sv);
      serverConsole.accept();
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
    
  }
}
//End of EchoServer class