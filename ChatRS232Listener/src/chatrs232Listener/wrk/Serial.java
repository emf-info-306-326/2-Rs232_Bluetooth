/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatrs232Listener.wrk;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 *
 * @author Waebela
 */
public class Serial implements SerialPortEventListener {

    public static final String PORT = "COM1";
    public static final int BAUDRATE = SerialPort.BAUDRATE_9600;
    public static final int DATABIT = SerialPort.DATABITS_8;
    public static final int STOPBIT = SerialPort.STOPBITS_1;
    public static final int PARITY = SerialPort.PARITY_NONE;
    public static final int FLOWCTRL = SerialPort.FLOWCONTROL_NONE;

    private Serial() {
        port = PORT;
        baudRate = BAUDRATE;
        dataBit = DATABIT;
        stopBit = STOPBIT;
        parity = PARITY;
        flowCtrl = FLOWCTRL;
    }

    public static Serial getInstance() {
        if (instance == null) {
            instance = new Serial();
        }
        return instance;
    }

    private void initialiseProtocole(String port, String baudRate, String dataBit, String stopBit, String parity, String handshaking) {
        this.port = port;
        switch (baudRate) {
            case "9600":
                this.baudRate = SerialPort.BAUDRATE_9600;
                break;
            case "4800":
                this.baudRate = SerialPort.BAUDRATE_9600;
                break;
            case "115200":
                this.baudRate = SerialPort.BAUDRATE_115200;
                break;
            case "1200":
                this.baudRate = SerialPort.BAUDRATE_1200;
                break;
            case "14400":
                this.baudRate = SerialPort.BAUDRATE_14400;
                break;
            case "19200":
                this.baudRate = SerialPort.BAUDRATE_19200;
                break;
            case "128000":
                this.baudRate = SerialPort.BAUDRATE_128000;
                break;
            case "256000":
                this.baudRate = SerialPort.BAUDRATE_256000;
                break;
            case "38400":
                this.baudRate = SerialPort.BAUDRATE_38400;
                break;
            case "57600":
                this.baudRate = SerialPort.BAUDRATE_57600;
                break;
            default:
                this.baudRate = SerialPort.BAUDRATE_9600;
        }
        switch (dataBit) {
            case "8":
                this.dataBit = SerialPort.DATABITS_8;
                break;
            case "7":
                this.dataBit = SerialPort.DATABITS_7;
                break;
            case "6":
                this.dataBit = SerialPort.DATABITS_6;
                break;
            case "5":
                this.dataBit = SerialPort.DATABITS_5;
                break;
            default:
                this.dataBit = SerialPort.DATABITS_8;
                break;

        }
        switch (stopBit) {
            case "1":
                this.stopBit = SerialPort.STOPBITS_1;
                break;
            case "1.5":
                this.stopBit = SerialPort.STOPBITS_1_5;
                break;
            case "2":
                this.stopBit = SerialPort.STOPBITS_2;
                break;
            default:
                this.stopBit = SerialPort.STOPBITS_1;
                break;
        }
        switch (parity) {
            case "None":
                this.parity = SerialPort.PARITY_NONE;
                break;
            case "Even":
                this.parity = SerialPort.PARITY_EVEN;
                break;
            case "Odd":
                this.parity = SerialPort.PARITY_ODD;
                break;
            default:
                this.parity = SerialPort.PARITY_NONE;
                break;
        }
        switch (handshaking) {
            case "None":
                this.flowCtrl = SerialPort.FLOWCONTROL_NONE;
                break;
            case "xOn-xOff":
                this.flowCtrl = SerialPort.FLOWCONTROL_XONXOFF_IN;
                break;
            case "RTS-CTS":
                this.flowCtrl = SerialPort.FLOWCONTROL_RTSCTS_IN;
                break;
            default:
                this.flowCtrl = SerialPort.FLOWCONTROL_NONE;
                break;
        }
    }

    public boolean open(String port, String baudRate, String dataBit, String stopBit, String parity, String handshaking) {
        initialiseProtocole(port, baudRate, dataBit, stopBit, parity, handshaking);
        boolean ok = open();
        return ok;
    }

    public boolean open() {
        boolean ok = false;
        if (serialPort == null) {
            serialPort = new SerialPort(port);
            try {
                serialPort.openPort();
                serialPort.setParams(baudRate, dataBit, stopBit, parity);
                serialPort.setFlowControlMode(flowCtrl);
                serialPort.addEventListener(this);
                bufferReceived = null;
                bufferCpt = 0;
                ok = true;
            } catch (SerialPortException ex) {
                System.out.println("ERREUR Ouverture Port RS 232 : " + ex.getMessage());
            }
        }
        return ok;
    }

    public boolean close() {
        boolean ok = false;
        if (serialPort != null) {
            try {
                serialPort.closePort();
                serialPort = null;
                ok = true;
            } catch (SerialPortException ex) {
                System.out.println("ERREUR Fermeture Port RS 232 : " + ex.getMessage());
            }
        }
        return ok;
    }

    public ArrayList<String> giveAllAvailabalePort() {
        String[] lesPorts = SerialPortList.getPortNames();
        ArrayList<String> availablePorts = new ArrayList<>();
        for (String unPort : lesPorts) {
            availablePorts.add(unPort);
        }
        return availablePorts;
    }

    public boolean sendText(String text) {
        boolean ok = false;
        try {
            serialPort.writeString(text + "\n");
            ok = true;
        } catch (SerialPortException ex) {
            // Rien car déjà false
            System.out.println("ERREUR Ecriture " + ex.getMessage());
        }
        return ok;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if ((event.isRXCHAR()) && (event.getEventValue() > 0)) {
            byte buffer[];
            try {
                buffer = serialPort.readBytes();
                for (byte b : buffer) {
                    if (b == '\n') {
                        refWrk.receiveText(new String(bufferReceived));
                        bufferReceived = null;
                        bufferCpt = 0;
                    } else {
                        if (bufferReceived == null){
                            bufferReceived = new byte[2000];
                        }
                        bufferReceived[bufferCpt] = b;
                        bufferCpt++;
                    }
                }
            } catch (SerialPortException ex) {
                Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public int getDataBit() {
        return dataBit;
    }

    public void setDataBit(int dataBit) {
        this.dataBit = dataBit;
    }

    public int getStopBit() {
        return stopBit;
    }

    public void setStopBit(int stopBit) {
        this.stopBit = stopBit;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public int getFlowCtrl() {
        return flowCtrl;
    }

    public void setFlowCtrl(int flowCtrl) {
        this.flowCtrl = flowCtrl;
    }

    public void setRefWrk(Wrk refWrk) {
        this.refWrk = refWrk;
    }

    private String port;
    private int baudRate;
    private int dataBit;
    private int stopBit;
    private int parity;
    private int flowCtrl;
    private SerialPort serialPort;
    private static Serial instance;
    private byte[] bufferReceived;
    private int bufferCpt;

    private Wrk refWrk;
}
