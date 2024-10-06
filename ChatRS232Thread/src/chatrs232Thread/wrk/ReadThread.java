/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatrs232Thread.wrk;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author waeberla
 */
public class ReadThread extends Thread {

    public ReadThread(Serial refSerial, SerialPort refSerialPort) {
        this.refSerial = refSerial;
        this.serialPort = refSerialPort;
        pause = false;
        bufferReceived = null;
        bufferCpt = 0;
    }

    private void attend(int mili) {
        try {
            sleep(mili);
        } catch (InterruptedException ex) {
            Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            if (!pause) {
                readText();
            } else {
                attend(10);
            }
        }
    }

    public void readText() {
        byte buffer[];
        try {
            buffer = this.serialPort.readBytes();
            if ((buffer != null) && (buffer.length > 0)) {
                for (byte b : buffer) {
                    if (b == '\n') {
                        refSerial.receiveMsg(new String(bufferReceived));
                        bufferReceived = null;
                        bufferCpt = 0;
                    } else {
                        if (bufferReceived == null) {
                            bufferReceived = new byte[2000];
                        }
                        bufferReceived[bufferCpt] = b;
                        bufferCpt++;
                    }
                }
            }
        } catch (SerialPortException ex) {
            bufferReceived = null;
            bufferCpt = 0;
        }
    }

    public void stopThread() {
        running = false;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }
    private Serial refSerial;
    private SerialPort serialPort;
    public volatile static boolean running;
    public volatile boolean pause;
    private byte[] bufferReceived;
    private int bufferCpt;

}
