import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;

public class Donut extends Sprite {
    private Image image = null;

    /** Creates a donut sprite 
     * @param map the map to use
     * 
    */
    public Donut(Image map) {
        super(map);
        this.postionX = 0;
        this.postionY = 0;
        this.rotation = 0;
        try {
            image = new Image(new FileInputStream("assets/images/Donut.gif"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // animated gif of the pacman
        this.setImage(image);
        while(this.checkColision()) {
            this.setPositionX((int) (Math.random() * map.getWidth()));
            this.setPositionY((int) (Math.random() * map.getHeight()));
            this.setTranslateX(postionX);
            this.setTranslateY(postionY);
            this.setRotate(rotation);
        }
    }
}
