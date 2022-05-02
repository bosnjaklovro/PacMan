/**
 * JavaFX Symple GUI, example 1
 * @author David Patric, Alan Mutka
 * @version 2205
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

public class PacManGame extends Application {

   // String musicFile = "GameMusic.mp3";

   // Media sound = new Media(Paths.get(musicFile).toUri().toString());
   // MediaPlayer mediaPlayer = new MediaPlayer(sound);

   public Stage stage = null;
   public Scene scene = null;
   // int rgb = img.getRGB(-360, -360);
   // Color color = new Color(rgb);

   // int blue

   public static void main(String[] args) {
      // method inside the Application class, it will setup our program as a JavaFX
      // application
      // then the JavaFX is ready, the "start" method will be called automatically

      launch(args);
   }

   @Override
   public void start(Stage _stage) {
      // create a scene
      this.stage = _stage;
      /////////////////

      // GameScene game = new GameScene();
      // scene = new Scene(game, 720, 720);
      // game.init();
      // game.handleMovement(scene);

      MainMenu main = new MainMenu();

      this.scene = new Scene(main, 400, 400);
      
      // Creates a INFORMATION alert.
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("PacMan");
      alert.setHeaderText("Welcome");
      alert.setContentText("Click the Singleplayer button to play alone, Multiplayer to play with other players or Server to host a game.");
      alert.showAndWait();
      
      main.init(this);

      //////// Setting window properties
      // set the window title
      try {
         _stage.getIcons().add(new Image(new FileInputStream("assets/images/PacmanPink.png")));
      } catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      _stage.setTitle("Pac-Man");
      _stage.setScene(scene);
      _stage.show();
   }
}