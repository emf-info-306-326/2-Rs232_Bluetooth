/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatrs232Thread.wrk;

import chatrs232Thread.ctrl.Ctrl;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Waebela
 */
public class Wrk {

    public Wrk() {
        serial = Serial.getInstance();
    }

    public void setRefCtrl(Ctrl refCtrl) {
        this.refCtrl = refCtrl;
    }

    public ArrayList<String> donneLesPorts() {
        ArrayList<String> lesPorts = new ArrayList<>();

        return lesPorts;
    }

    public boolean openConnexion(String port, String baudRate, String dataBit, String stopBit, String parity, String handshaking) {
        boolean ok = false;
        if (serial == null) {
            serial = Serial.getInstance();
        }
        if (serial != null) {
            serial.setRefWrk(this);
            ok = serial.open(port, baudRate, dataBit, stopBit, parity, handshaking);
        }
        return ok;
    }

    public boolean closeConnexion() {
        
        boolean ok = serial.close();
        return ok;
    }

    public ArrayList<String> giveAvailablePort() {
        return serial.giveAllAvailabalePort();
    }

    public boolean sendText(String pseudo, String texte) {
        boolean ok = false;
        if (serial != null) {
            Date now = new Date();
            SimpleDateFormat formateur = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            String textToSend = formateur.format(now) + " " + pseudo + "\t" + texte;
            if (serial.sendText(textToSend)) {
                receiveText(textToSend);
                ok = true;
            }
        }
        return ok;
    }

    public void receiveText(String text) {
        refCtrl.receiveText(text);
    }
    private Ctrl refCtrl;
    private Serial serial;
}
