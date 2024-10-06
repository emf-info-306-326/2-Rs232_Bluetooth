/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatrs232Listener.ctrl;

import chatrs232Listener.ihm.Ihm;
import chatrs232Listener.wrk.Wrk;
import java.util.ArrayList;

/**
 *
 * @author Waebela
 */
public class Ctrl {
    
    public void startUp(){
        ArrayList<String>availablePort = refWrk.giveAvailablePort();
        refIhm.afficheComboPort(availablePort);
    }
    
    public void connecte(String port, String baudRate, String dataBit, String stopBit, String parity, String handshaking){
        if (refWrk.openConnexion(port, baudRate, dataBit, stopBit, parity, handshaking)){
            refIhm.afficheConsole("Port ouvert");
        }else{
            refIhm.afficheConsole("Erreur lors de l'ouverture du port");
        }
    }
    
    public void deconnecte(){
        if (refWrk.closeConnexion()){
            refIhm.afficheConsole("Port fermé");
        }else{
            refIhm.afficheConsole("Erreur lors de la fermetre du port");
        }
    }
    
    public void sendText(String pseudo, String texte){
        if (refWrk.sendText(pseudo, texte)){
            refIhm.afficheConsole("Envoi du message");
        }else{
            refIhm.afficheConsole("Erreur lors de l'envoi du message");
        }
    }

    public void receiveText(String texte){
        refIhm.afficheText(texte);
    }
    public void setRefIhm(Ihm refIhm) {
        this.refIhm = refIhm;
    }

    public void setRefWrk(Wrk refWrk) {
        this.refWrk = refWrk;
    }
    
    private Ihm refIhm;
    private Wrk refWrk;
}
