
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;

public class Ghost extends Sprite{
    
    private Image image = null;

    /**
     * Creates a ghost image.
     * @param map the map to use

     *  */ 
    public Ghost(Image map) {
        super(map);
        this.postionX = 100;
        this.postionY = 200;
        this.rotation = 0;
        try {
            image = new Image(new FileInputStream("assets/images/GhostOrange.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.setImage(image);
    }

    // Updates the position of the ghost.
    public void update() {
    

       // System.out.println("rotation: " + (((int)Math.random() * 4) * 90));
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
            this.setRotation(((int)(Math.random() * 4) * 90));
        }
 
        synchronized (this) {
            this.setTranslateX(postionX);
            this.setTranslateY(postionY);
            this.setRotate(rotation);
        }

    } // end update()
}