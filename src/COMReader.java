import java.io.*;
import java.util.*;
import gnu.io.*;

public class COMReader implements Runnable, SerialPortEventListener{
    static CommPortIdentifier portId;

    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;

    public COMReader(){
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        while(portEnum.hasMoreElements()){
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            System.out.println(currPortId.getName());
            if(currPortId.getName().equals("COM11")){   // port on this Windows computer
                if(currPortId.getPortType() == CommPortIdentifier.PORT_SERIAL){
                    portId = currPortId;                // set the portId
                    System.out.println("Port set");
                }else{
                    System.out.println("the port must be SERIAL");
                    System.out.println("Shutting down program");
                    System.exit(0);              // exiting the program
                }
            }
        }
    }

    public void StartReadingThread(){
        // check if portId is initialised
        if(portId == null){
            System.out.println("the port is not initialised");
            System.out.println("Shutting down program");
            System.exit(0);              // exiting the program
        }
        // open the COM port
        try{
            serialPort = (SerialPort) portId.open("COMReader", 2000);
        } catch (PortInUseException e){
            System.out.println(e);
        }
        // start up the inputStream from the device to the computer
        try {
            inputStream = serialPort.getInputStream();
        }catch (IOException e){
            System.out.println(e);
        }
        // add the event listener to the port
        try {
            serialPort.addEventListener(this);
        }catch (TooManyListenersException e){
            System.out.println(e);
        }
        // set parameters for the serial port
        try {
            serialPort.setSerialPortParams(9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
            System.out.println(e);
        }
        //start the reading thread
        readThread = new Thread(this);
        readThread.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {System.out.println(e);}
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch(serialPortEvent.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                byte[] readBuffer = new byte[20];

                try {
                    while (inputStream.available() > 0) {
                        int numBytes = inputStream.read(readBuffer);
                    }
                    System.out.println("Here Now");
                    System.out.println(new String(readBuffer));
                } catch (IOException e) {System.out.println(e);}
                break;
        }
    }

}
