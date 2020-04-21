import com.fazecast.jSerialComm.SerialPort;

import java.io.*;
import java.util.*;
//import gnu.io.*;

public class COMReader{
    SerialPort[] portList;
    SerialPort comPort;

    public COMReader(){
        portList = SerialPort.getCommPorts();
        int listLength = portList.length;

        while(listLength > 0){
            listLength --;
            SerialPort currPort = portList[listLength];
            System.out.println(currPort.getSystemPortName());
            if(currPort.getSystemPortName().equals("COM11")){
                comPort = currPort;
                System.out.println("connecting to: " + comPort.getPortDescription());
                break;
            }
            if(listLength == 0){
                System.out.println("ERROR: Port could not be found");
                System.out.println("Program shutting down");
                System.exit(0);
            }
        }
    }

    public void initPort(){
        comPort.setBaudRate(9600);
        comPort.setNumDataBits(8);
        comPort.setNumStopBits(1);
        comPort.setParity(0);
        comPort.setFlowControl(0);
        comPort.openPort();
    }

    public void reading(){
        try {
            while (true){
                while (comPort.bytesAvailable() == 0){
                    System.out.println("Sleeping");
                    Thread.sleep(10000);
                }
                System.out.println("NOT Sleeping");
                byte[] readBuffer = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
                if (numRead > 0){
                    System.out.println("Read " + numRead + " bytes.");
                    readOutBuffer(readBuffer);
                }
                else if (numRead == 0){
                    System.out.println("No bytes were read");
                }
                else if(numRead == -1){
                    System.out.println("An error occurred when reading");
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void readOutBuffer(byte[] readBuffer){
        int bufferLength = readBuffer.length;
        while(bufferLength > 0){
            bufferLength--;
            System.out.println(readBuffer[bufferLength]);
        }
    }

}
