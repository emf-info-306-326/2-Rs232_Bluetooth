/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatrs232Thread;

import chatrs232Thread.ctrl.Ctrl;
import chatrs232Thread.ihm.Ihm;
import chatrs232Thread.wrk.Wrk;

/**
 *
 * @author Waebela
 */
public class ChatRS232 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Ctrl ctrl = new Ctrl();
        Ihm ihm = new Ihm();
        Wrk wrk = new Wrk();
        
        ctrl.setRefIhm(ihm);
        ctrl.setRefWrk(wrk);
        ihm.setRefCtrl(ctrl);
        wrk.setRefCtrl(ctrl);
        
        ctrl.startUp();
    }
    
}
