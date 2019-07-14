/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caranimation;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *
 * @author sourav
 */
public class Car extends JComponent{
    BufferedImage car, lane, toll;
    Point mCarPosition;
    Point mStartPos = null;
    String mRegistration;
    int mCarType;
    boolean isAuthorized;
    private static final String[] CAR_RESOURCES = {
        "/resources/car1.png", 
        "/resources/car2.png",
        "/resources/truck.png"
    };
    
    int counter = 0;
    
    public Car(int x, int y){
        try {
            mCarType = ThreadLocalRandom.current().nextInt(0, CAR_RESOURCES.length );
            URL input = Car.class.getResource(CAR_RESOURCES[mCarType]);
            URL laneInput = Car.class.getResource("/resources/lane.jpg");
            URL tollInput = Car.class.getResource("/resources/toll.png");
            isAuthorized = (1 == ((int)(Math.random() * 2) % 2));
            
            if(input == null || laneInput == null) {
                System.out.println("Failed to read image");
            }
            
            car = ImageIO.read(input);
            lane = ImageIO.read(laneInput);
            toll = ImageIO.read(tollInput);
            
            this.mStartPos = new Point(x, y);
        } catch (IOException ex) {
            Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
        }
        mCarPosition = new Point(mStartPos.x, mStartPos.y);
        mRegistration = randomAlphaNumeric(6);
    }
    
    public void paintCar(Graphics g){
        g.drawImage(this.lane, 0, 0, null);
        g.drawImage(this.car, this.getCarPosition().x, 
                this.getCarPosition().y, 100, 100, null);
        g.drawImage(this.toll, 0, CarAnimation.TOLL_Y, 130, 130, null);
    }

    private static String randomAlphaNumeric(int count) {
        
        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }

        return builder.toString();
    }
    
    public Point getCarPosition() {
       return new Point(mCarPosition.x, mCarPosition.y);
    }
   
    public void setCarPosition(Point carPosition) {
       mCarPosition = carPosition;
    }
    
    void sendDataToTollCollector() throws IOException {
        
        synchronized(CarAnimation.mStream) {
            ObjectOutputStream send = new ObjectOutputStream(CarAnimation.mStream);
            send.writeObject(mRegistration);
        }
    }
   
    public boolean update(){
        repaint();
        if(getCarPosition().y < CarAnimation.WINDOW_HEIGHT)  {
            Point currentPostion = getCarPosition();
            
            if(currentPostion.y <= CarAnimation.TOLL_Y && 
                    currentPostion.y + 10 > CarAnimation.TOLL_Y && !isAuthorized) {
                try {
                    System.out.println("Car not Authorized, Sending Registration " + mRegistration);
                    sendDataToTollCollector();
                    
                } catch (IOException ex) {
                    Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            mCarPosition.x = currentPostion.x;
            mCarPosition.y = currentPostion.y + 10;
        } else {
            System.out.println("Destination reached");
            return true;
        }
        repaint(); 
        return false;
    }
}
