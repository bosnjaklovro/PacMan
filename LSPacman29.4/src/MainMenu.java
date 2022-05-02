import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu extends VBox {

    /**
     * Initializes the main menu.
     * @param stage The stage to show the main menu.
     */
    
    private Button singleplayer = new Button("Single Player");
    private Button multiplayer = new Button("Multiplayer");
    private Button server = new Button("Server");
    private Button exit = new Button("Exit");

    /** 
     * Initializes the main menu.
     * @param PacManGame Initializes the pacman game.
    */
    public void init(PacManGame pacmanGame) {

        singleplayer.setOnAction(e -> {
            SinglePlayer game = new SinglePlayer();
            pacmanGame.scene.setRoot(game);
            game.init();
            game.handleMovement(pacmanGame.scene);
        });

        multiplayer.setOnAction(e -> {
            MultiPlayer game = new MultiPlayer();
            pacmanGame.scene.setRoot(game);
            game.init();
            game.handleMovement(pacmanGame.scene);
        });

        server.setOnAction(e -> {
            Server server = new Server();
            pacmanGame.scene.setRoot(server);
            server.init();
            new Thread(server).start();
        });

        exit.setOnAction(e -> {
            System.exit(0);
        });
    }

    // Sets up the main menu.
    public MainMenu() {

        this.setAlignment(Pos.CENTER);
        this.setSpacing(5);
        singleplayer.setMinWidth(100);
        multiplayer.setMinWidth(100);
        server.setMinWidth(100);
        exit.setMinWidth(100);
        this.getChildren().add(new Label("Play now"));
        this.getChildren().addAll(singleplayer, multiplayer, server, exit);

    }
}