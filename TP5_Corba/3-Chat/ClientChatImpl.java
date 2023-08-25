import org.omg.CORBA.* ;
import org.omg.PortableServer.* ;
import java.util.* ;
import java.io.* ;
import java.io.IOException ;
import org.omg.CosNaming.* ;
import Chat.ClientChat ;
import Chat.ServeurChat ;
import Chat.ServeurChatHelper ;
import Chat.ClientChatPOA ;
import Chat.ClientChatHelper ;

public class ClientChatImpl
  extends ClientChatPOA
{
  String my_name = "chatter" ;
  BufferedReader br = null ;
  ClientChat chatter ;
  ServeurChat serveur ;
  ThreadRun thread ;

  // Callbacks
  public void receiveNewChatter (String name)
  {
    // TODO
  }

  public void receiveExitChatter (String name)
  {
    // TODO
  }

  public void receiveChat (String name, String message)
  {
    // TODO
  }

  private String getEntry()
    throws IOException
  {
    String s = null ;
    s = br.readLine() ;
    return (s) ;
  }

  private void loop()
  {
    try
    {
      boolean got_name = false ;
      String dst = null ;
      InputStreamReader isr = null ;
      String message = " " ;

      isr = new InputStreamReader (System.in) ;
      br = new BufferedReader (isr) ;
      
      while (true /*TODO : condition de sortie du chat*/)
      {
        if (!got_name)
        {
          System.out.print("Please enter your name: ") ;
          my_name = getEntry() ;
          got_name = true ;

          // TODO : se déclarer auprès du serveur
        }
        else
        {
          // TODO : prompt de chat et gestion de l'envoi
        }
      }
    }
    catch (IOException e)
    {
      System.out.println("ERROR : " + e) ;
      e.printStackTrace() ;
    }
    finally
    {
      // informer le serveur de la fin du chat
    }
  }

  public static void main(String args[])
  {
    ClientChatImpl client = null ;

    if (args.length != 0/*TODO*/)
    {
      // TODO
      return ;
    }
    try
    {
      String [] argv /*= TODO */ ; 
      ORB orb = ORB.init(argv, null) ;

      // Init POA
      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA")) ;
      rootpoa.the_POAManager().activate() ;

      // creer l'objet qui sera appelé depuis le serveur
      client = new ClientChatImpl() ;
      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(client) ;
      client.chatter = ClientChatHelper.narrow(ref) ; 
      if (client == null)
      {
        System.out.println("Pb pour obtenir une ref sur le client") ;
        System.exit(1) ;
      }

      // contacter le serveur
      String reference /* = TODO */ ;
      org.omg.CORBA.Object obj = orb.string_to_object(reference) ;

      // obtenir reference sur l'objet distant
      client.serveur = ServeurChatHelper.narrow(obj) ;
      if (client.serveur == null)
      {
        System.out.println("Pb pour contacter le serveur") ;
        System.exit(1) ;
      } 
      else
        System.out.println("Annonce du serveur : " + client.serveur.ping()) ;

      // TODO lancer l'ORB dans un thread en utilisant client.thread

      client.loop() ;
    }
    catch (Exception e)
    {
      System.out.println("ERROR : " + e) ;
      e.printStackTrace(System.out) ;
    }
    finally
    {
      // shutdown
      if (client != null)
        client.thread.shutdown() ;
    }
  }
}
