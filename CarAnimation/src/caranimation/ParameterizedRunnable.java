/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caranimation;

import caranimation.CarAnimation.Surface;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sourav
 */
public class ParameterizedRunnable implements Runnable{

    //JFrame mFrame;
    Car mCar;
    int mDelay;
    Surface mSurface;
    public ParameterizedRunnable(Surface surface , int delay) {
        this.mSurface = surface;
        this.mDelay = delay;
        mCar = new Car(CarAnimation.START_X, CarAnimation.START_Y);
        mSurface.setCar(mCar);
    }

    @Override
    public void run() {
        boolean ret;
        while (true) {
            mSurface.repaint();
            ret = mCar.update();
            try {
                Thread.sleep(this.mDelay, 0);
            } catch (InterruptedException ex) {
                Logger.getLogger(CarAnimation.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(ret == true) {
                mCar = new Car(CarAnimation.START_X, CarAnimation.START_Y);
                mSurface.setCar(mCar);
            }
            mSurface.repaint();
        }
    }
}
