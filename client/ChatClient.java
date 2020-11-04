// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      
    	if(message.charAt(0)=='#') {
    		message = message.substring(1,message.length());

    		//Terminate client
    		if(message.equals("quit")) {
    			System.out.println("Quitting...");
    			quit();
    		}

    		//Disconnect client from server
    		else if(message.equals("logoff")){
    			System.out.println("Logging off...");
    			closeConnection();
    		}

    		//Sets the client host
    		else if(message.startsWith("sethost")){
    			if(this.isConnected()==true) {
    				System.out.println("Command Denied: Must be logged Off");
    			} else {
    				System.out.println("Setting Host...");
    				String newHost = message.split(" ")[1];
    				try {
    					setHost(newHost);
    					System.out.println("New Host: " + newHost);
    				}catch(Exception e){
    					System.out.print("Unable to connect to host: "+ newHost);
    				}
    			}
    		}

    		//Sets the client port
    		else if(message.startsWith("setport")){
    			if(this.isConnected()==true) {
    				System.out.println("Command Denied: Must be logged Off");
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

    		//Causes the client to connect to the server
    		else if(message.equals("login")){
    			if(this.isConnected()==true) {
    				System.out.println("Command Denied: Must be logged Off");
    			} else {
    				System.out.println("Logging in...");
    				try {
    					openConnection();
    					System.out.println("Logged in");
    				}catch(IOException e) {
    					System.out.println("Unable to login");
    				}
    				//System.out.println("Logged in as: " + this.loginID);
    			}
    		}

    		//Displays the current host name
    		else if(message.equals("gethost")){
    			System.out.println("Current host name : "+this.getHost());
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
    		sendToServer(message);
    	}
    	
    }
    catch(IOException e)
    {
      clientUI.display
        ("> Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

	/**
	* Hook method called after the connection has been closed. The default
	* implementation does nothing. The method may be overriden by subclasses to
	* perform special processing such as cleaning up and terminating, or
	* attempting to reconnect.
	*/
  	@Override
  	protected void connectionClosed()
  	{
  		clientUI.display("> Conection has closed");
  	}

	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 *
	 * @param exception
	 *            the exception raised.
	 */
  	@Override
  	protected void connectionException(Exception exception) {
  		clientUI.display("> Server has closed");
  		quit();
	}
}
//End of ChatClient class
