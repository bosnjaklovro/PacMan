import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import javafx.animation.AnimationTimer;

public class Client implements Runnable {
    private Socket socket;
    public ObjectInputStream ois;
    public ObjectOutputStream oos;
    public Server server;
    public String name;
    
    // Sends a client message to the server.
    public Client(Socket clientSocket, Server server) {
        this.socket = clientSocket;
        this.server = server;
        this.name = String.format("%d", (int)(Math.random()*999999));
        try {
            this.oos = new ObjectOutputStream(this.socket.getOutputStream());
            this.ois = new ObjectInputStream(this.socket.getInputStream());
            System.out.println("Client sent: " + (String) ois.readObject());

            oos.writeObject("new-player");
                            oos.writeObject(name);
                            oos.flush();
                            oos.writeObject(20);
                            oos.writeObject(20);
                            oos.writeObject(0);
                            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            server.clients.remove(this);
            e.printStackTrace();
        } 

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
               

                try {
                    synchronized (this){
                        
                        for(Client k : server.sprites.keySet()){
                            Sprite s = server.sprites.get(k);
                            oos.writeObject("old-player");
                            oos.writeObject(k.name);
                            oos.flush();
                            oos.writeObject(s.getTranslateX());
                            oos.writeObject(s.getTranslateY());
                            oos.writeObject(s.getRotation());
                            oos.flush();
                        }
                
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
    
            }
        };

        timer.start();
    }
    // This is the main loop of the client.
    @Override
    public void run() {
        while(true) {
            try {
                synchronized (this){
                    Object obj = ois.readObject();
                    if (obj instanceof String) {
                        String str = (String) obj;
                        System.out.println("Received: " + str);
                        switch (str) {
                            case "players-location":
                                server.sprites.get(this).setPositionX((int) ois.readObject());
                                server.sprites.get(this).setPositionY((int) ois.readObject());
                                server.sprites.get(this).setRotation((int) ois.readObject());
                                break;
                            case "end":
                                break;
                            default:
                                break;
                        }
                    }
                }
                
            } catch (IOException e) {
                server.clients.remove(Client.this);
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    // Returns a string representation of the request.
    @Override
    public String toString() {
        return "Client: " + this.socket.getInetAddress().getHostAddress() + ":" + this.socket.getPort() + "\n";
    }
}