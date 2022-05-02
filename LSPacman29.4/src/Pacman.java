
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.security.spec.X509EncodedKeySpec;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

// Sprite extends ImageView
public class Pacman extends Sprite {
    private Image image = null;
    
    public Pacman(Image map) {
        super(map);

        this.postionX = 100;
        this.postionY = 100;
        this.rotation = 0;

        try {
            image = new Image(new FileInputStream("assets/images/PacmanAnim.gif"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // animated gif of the pacman
        this.setImage(image);
    }

    public void update() {
        //rotation += 2;
       // System.out.println("rotation: " + rotation);
        int oldPositionX = this.getPositionX();
        int oldPositionY = this.getPositionY();

        switch(rotation) {
            case 0: // RIGHT
                this.setPositionX(this.getPositionX() + this.getSpeed());
            break;
            case 90: // DOWN
                this.setPositionY(this.getPositionY() + this.getSpeed());
            break;
            case 180: // LEFT
                this.setPositionX(this.getPositionX() - this.getSpeed());
            break;
            case 270: // UP
                this.setPositionY(this.getPositionY() - this.getSpeed());
            break;
        }

        // collision

        if (checkColision()) {
            this.setPositionX(oldPositionX);
            this.setPositionY(oldPositionY);
        }
 
        // postionX += (int) (Math.random() * getPacmanWidth() / 30);
        // postionY += (int) (Math.random() * getPacmanHeight() / 30);
        this.setTranslateX(postionX);
        this.setTranslateY(postionY);
        this.setRotate(rotation);
        // this.setRotation(rotation);

    } // end update()

  
    public void goUP() {
        // System.out.println("UP");
        rotation = 270;
    }

    public void goDOWN() {
        rotation = 90;
        // System.out.println("DOWN");
    }

    public void goLEFT() {
        rotation = 180;
        // System.out.println("LEFT");
    }

    public void goRIGHT() {
        rotation = 0;
        // System.out.println("RIGHT");
    }

}