/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caranimation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.PipedOutputStream;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import ticketsystem.TollCollectorThread;

/**
 *
 * @author Sourav Dasgupta
 */
public class CarAnimation extends JFrame {
    public static final int NUM_CARS = 4;
    public static final int WINDOW_WIDTH = 580, WINDOW_HEIGHT = 550;
    public static final int START_X = 10, START_Y = 50;
    public static final int MAX_DELAY = 500, MIN_DELAY = 200;
    public static final int TOLL_Y = 300;
    
    
    public static final PipedOutputStream mStream = new PipedOutputStream();
    TollCollectorThread tollThread = null;

    public CarAnimation() {
        tollThread = new TollCollectorThread(mStream);
        startTollCollectorThread();
        initUI();
    }

    private void startTollCollectorThread() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.submit(tollThread);
    }


    public class Surface extends JPanel {
        Car mCar = null;

        public void setCar(Car car) {
            this.mCar = car;
        }

        private void doDrawing(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.black);

            mCar.paintCar(g);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            doDrawing(g);
        }
    }

    public void moveCar(Surface surface, int delay) {
        ParameterizedRunnable helloRunnable = new ParameterizedRunnable(/*this, car,*/surface, delay);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.submit(helloRunnable);
    }

    private void initUI() {

        Random r = new Random();
        
        setTitle("OOAD Project GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        
        for(int i = 0; i < NUM_CARS; i++) {
            
            Surface surface = new Surface();
            int delay = r.nextInt((MAX_DELAY - MIN_DELAY) + 1) + MIN_DELAY;
       
            container.add(surface);
            moveCar(surface, delay);
        }
        
        add(container);
        setLocationRelativeTo(null);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CarAnimation carAnimation = new CarAnimation();
            carAnimation.setVisible(true);
            System.out.println("Done with window");
        });
    }
}
