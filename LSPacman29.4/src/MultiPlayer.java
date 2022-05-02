
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class MultiPlayer extends Pane implements Runnable {

    private Pacman pacman = null;
    private Vector<Ghost> ghosts = null;
    private Vector<Donut> donuts = null;
    private HashMap<String, Sprite> sprites = null;

    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;
    Socket socket = null;

    public Image map = null;

     /**
      * Creates a multiplayer game.
     */
    public MultiPlayer() {

        /** create the Layout where we can put scene elements, the main layout
        *   layout are used for automatic positioning elements inside the scene, for
        *   example, TOP, LEFT, RIGHT, CENTER
          */

        /*  create a scene with a specific size (width, height), connnect with the layout
         */
        try {
            map = new Image(new FileInputStream("assets/images/map.png"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        init();

        AnimationTimer timer = new AnimationTimer() {
            public void handle(long now) {

                pacman.update();

                try {
                    synchronized (this) {
                        oos.writeObject("players-location");
                        oos.writeObject((int) pacman.getTranslateX());
                        oos.writeObject((int) pacman.getTranslateY());
                        oos.writeObject((int) pacman.getRotation());
                        oos.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (Ghost ghost : ghosts) {
                    ghost.update();
                }
                synchronized (this) {
                    for (Donut donut : donuts) {
                        if (pacman.colidesWith(donut)) {
                            synchronized (this) {

                                donut.setVisible(false);
                                donuts.remove(donut);
                            }
                            System.out.println("Donut eaten " + donuts.size());
                        }
                    }
                }

                if (donuts.size() == 0) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("You Have WOn!!!");
                        alert.setHeaderText("Game Well DOne");
                        alert.setContentText("Score: " + donuts.size());
                        alert.showAndWait();
                        init();
                        this.start();
                    });
                    this.stop();
                }
                for (Ghost ghost : ghosts) {
                    if (ghost.colidesWith(pacman)) {
                        System.out.println("Game Over");
                        System.out.println("Score: " + donuts.size());
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Game Over");
                            alert.setHeaderText("Game Over");
                            alert.setContentText("Leftover donuts: " + donuts.size());
                            alert.showAndWait();
                            init();
                            this.start();
                        });
                        this.stop();
                    }
                }
            }
        };

        try {
            socket = new Socket("localhost", 5555);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            oos.writeObject(new String("Connected"));
            oos.flush();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /**
         * starts the timer
         */ 
        timer.start();

        new Thread(this).start();
    }

    /**
     * Initializes the pacman map.
     *  
     */ 
    public void init() {
        pacman = new Pacman(map);
        ghosts = new Vector<>();
        donuts = new Vector<>();
        sprites = new HashMap<String, Sprite>();

        ImageView mapView = new ImageView(map);
        this.getChildren().clear();
        this.getChildren().add(mapView);

        for (int i = 0; i < 100; i++) {
            Donut donut = new Donut(map);
            this.getChildren().add(donut);
            donuts.add(donut);
        }

        for (int i = 0; i < 20; i++) {
            Ghost ghost = new Ghost(map);
            this.getChildren().add(ghost);
            ghosts.add(ghost);
        }

        this.getChildren().addAll(pacman);
    }

    @Override
    public void run() {
        /*
         * Update the animation timer.
         */
        String name;
        Pacman pac;
        while (true) {
            try {
                synchronized (this) {
                    Object obj = ois.readObject();
                    if (obj instanceof String) {
                        String str = (String) obj;
                        System.out.println("Received: " + str);
                        switch (str) {
                            case "old-player":
                                name = (String) ois.readObject();
                                pac = (Pacman) this.sprites.get(name);
                                pac.setTranslateX((int) ois.readObject());
                                pac.setTranslateY((int) ois.readObject());
                                pac.setRotation((int) ois.readObject());
                                break;
                            case "new-player":
                                name = (String) ois.readObject();
                                pac = new Pacman(map);
                                pac.setTranslateX((int) ois.readObject());
                                pac.setTranslateY((int) ois.readObject());
                                pac.setRotation((int) ois.readObject());
                                final String NAME = name;
                                final Pacman PAC = pac;
                                Platform.runLater(new Runnable() {
                                    
                                    @Override
                                    public void run() {
                                        sprites.put(NAME, PAC);
                                        getChildren().add(PAC);

                                        // TODO Auto-generated method stub
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                            alert.setTitle("New Player");
                                            alert.setHeaderText("New Player");
                                            alert.setContentText("New Player: " + NAME);
                                            alert.show();
                                        
                                        
                                    }
                                });
                                break;
                        }
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Controls the movement of the pacman.
     * 
     * @param Scene    scene to control
     * @param KeyEvent event to control
     */
    public void handleMovement(Scene scene) {
        System.out.println("handleMovement");
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP:
                case W:
                    pacman.goUP();
                    break;
                case DOWN:
                case S:
                    pacman.goDOWN();
                    break;
                case LEFT:
                case A:
                    pacman.goLEFT();
                    break;
                case RIGHT:
                case D:
                    pacman.goRIGHT();
                    break;
                default:
                    break;
            }
        });
    }
}