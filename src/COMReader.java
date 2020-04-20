import java.io.*;
import java.util.*;
import javax.comm.*;

public class COMReader implements Runnable, SerialPortEventListener{
    static CommPortIdentifier portId;

    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;

    private COMReader(){
        Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();

        while(portEnum.hasMoreElements()){
            CommPortIdentifier currPortId = portEnum.nextElement();

            if(currPortId.getName().equals("COM11")){   //port on this Windows computer
                if(currPortId.getPortType() == CommPortIdentifier.PORT_SERIAL){
                    portId = currPortId;                // set the portId
                }else{
                    System.out.println("the port must be SERIAL");
                    System.out.println("Shutting down program");
                    System.exit(0);              // exiting the program
                }
            }
        }
    }

    @Override
    public void run() {

    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {

    }
}
