
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Server extends VBox implements Runnable {

    
    public HashMap<Client, Sprite> sprites = null;
    public ArrayList<Client> clients = null;
    public ServerSocket serverSocket = null;
    public TextArea textAreaClients  = null;

    // Initializes the server.
    public void init() {
        sprites = new HashMap<Client, Sprite>();
        clients = new ArrayList<Client>();

        textAreaClients = new TextArea();
        textAreaClients.setEditable(false);
        textAreaClients.setPrefSize(200, 200);
        this.getChildren().add(textAreaClients);

        try {
            serverSocket = new ServerSocket(5555);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getChildren().add(new Label("Server"));
    }

    // Shows the connections to the text area.
    public void showConnections() {
        Platform.runLater(() -> {
            textAreaClients.clear();
            synchronized (this) {
                for(Client client:clients){
                    textAreaClients.appendText(client.toString());
                }
            }
        });
    }

    // Connects to the pacman server.
    @Override
    public void run() {
        while(true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Waiting for client to connect");
                Client client = new Client(clientSocket, this);
                
                synchronized (clients) {
                    clients.add(client);
                }

                synchronized(sprites){
                    sprites.put(client, new Pacman(new Image(new FileInputStream("assets/images/PacmanAnim.gif"))));
                }
                
                new Thread(client).start();
                showConnections();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}