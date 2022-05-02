import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;

public class Sprite extends ImageView implements Serializable {
    public PixelReader pixelReader = null;
    
    public int postionX;
    public int postionY;
    public int rotation;
    public int speed;
    public Image map;

    public Sprite(Image map){
        this.map = map;
        this.speed = 1;
    }

    public boolean colidesWith(Sprite sprite) {
        double x1 = this.getPositionX() + this.getImage().getWidth() / 2;
        double y1 = this.getPositionY() + this.getImage().getHeight() / 2;
        double x2 = sprite.getPositionX() + sprite.getImage().getWidth() / 2;
        double y2 = sprite.getPositionY() + sprite.getImage().getHeight() / 2;

        double distance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        return distance < (this.getImage().getWidth() / 2 + sprite.getImage().getWidth() / 2);
    }

    public boolean checkColision() {
        int startX = this.getPositionX();
        int startY = this.getPositionY();
        int endX = (int) (this.getPositionX() + this.getImage().getWidth());
        int endY = (int) (this.getPositionY() + this.getImage().getHeight());

        for(int x=startX;x<endX;x++) {
            if (isOnBlue(x, startY)) {
                return true;
            }
        }
        for(int y=startY;y<endY;y++) {
            if (isOnBlue(startX, y)) {
                return true;
            }
        }
        for(int x=startX;x<endX;x++) {
            if (isOnBlue(x, endY)) {
                return true;
            }
        }
        for(int y=startY;y<endY;y++) {
            if (isOnBlue(endX, y)) {
                return true;
            }
        }

        return false;
    }

    public boolean isOnBlue(int x, int y) {
        try {
            return this.map.getPixelReader().getColor((int) (x),
                    (int) (y )).getBlue() > 0.6;
        } catch (Exception e) {
            return true;
        }
    }


    public int getPositionX() {
        return postionX;
    }

    public int getPositionY() {
        return postionY;
    }

    public int getRotation() {
        return rotation;
    }

    public int getSpeed() {
        return speed;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setPositionX(int postionX) {
        this.postionX = postionX;
    }

    public void setPositionY(int postionY) {
        this.postionY = postionY;
    }
}