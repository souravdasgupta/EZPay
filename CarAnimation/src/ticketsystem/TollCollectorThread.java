/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketsystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sourav
 */
public class TollCollectorThread implements Runnable {

    PipedInputStream mStream = null;

    public TollCollectorThread(PipedOutputStream stream) {
        try {
            mStream = new PipedInputStream(stream);
        } catch (IOException ex) {
            Logger.getLogger(TollCollectorThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                ObjectInputStream recv = new ObjectInputStream(mStream);
                String registration = (String) recv.readObject();
                System.out.println("Registration is "+registration);
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(TollCollectorThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
