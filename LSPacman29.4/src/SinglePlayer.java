
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SinglePlayer extends Pane {
    private Pacman pacman = null;
    private ArrayList<Ghost> ghosts = null;
    private ArrayList<Donut> donuts = null;

    int NUM_FOOD = 10;
    int NUM_GHOSTS = 2;
    int level = 1;

    public Image map = null;

    public SinglePlayer() {
        // create the Layout where we can put scene elements, the main layout
        // layout are used for automatic positioning elements inside the scene, for
        // example, TOP, LEFT, RIGHT, CENTER

        // create a scene with a specific size (width, height), connnect with the layout
        try {
            map = new Image(new FileInputStream("assets/images/map.png"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        init();

        // Update the animation timer.
        AnimationTimer timer = new AnimationTimer() {
            public void handle(long now) {

                pacman.update();

                for (Ghost ghost : ghosts) {
                    ghost.update();
                }
                // Removes donuts from the pacman platform.
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for (Donut donut : donuts) {
                            if (pacman.colidesWith(donut)) {
                                donut.setVisible(false);
                                donuts.remove(donut);
                                System.out.println("Donut eaten " + donuts.size());
                            }
                        }
                    }
                });
                
                // Creates a new alert and starts it.
                if (donuts.isEmpty()) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("You Have Won!!!");
                        alert.setHeaderText("Level "+ level +" passed");
                        alert.setContentText("Score: " + NUM_FOOD);
                        alert.showAndWait();
                        NUM_FOOD += 5;
                        NUM_GHOSTS += 1;
                        level++;
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
                            alert.setHeaderText("Level: "+ level + " failed");
                            alert.setContentText("Leftover donuts: " + donuts.size());
                            alert.showAndWait();
                            if(level>1){
                                level--;
                                NUM_FOOD -= 5;
                                NUM_GHOSTS -= 1;
                            }
                            init();
                            this.start();
                        });
                        this.stop();
                    }
                }
            }
        };

        // starts the timer
        timer.start();
    }

    public void init() {
        pacman = new Pacman(map);
        ghosts = new ArrayList<>();
        donuts = new ArrayList<>();

        ImageView mapView = new ImageView(map);
        this.getChildren().clear();
        this.getChildren().add(mapView);

        for (int i = 0; i < NUM_FOOD; i++) {
            Donut donut = new Donut(map);
            this.getChildren().add(donut);
            donuts.add(donut);
        }

        for (int i = 0; i < NUM_GHOSTS; i++) {
            Ghost ghost = new Ghost(map);
            this.getChildren().add(ghost);
            ghosts.add(ghost);
        }

        this.getChildren().addAll(pacman);
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