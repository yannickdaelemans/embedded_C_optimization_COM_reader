import com.fazecast.jSerialComm.SerialPort;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
//import gnu.io.*;

public class COMReader{
    FileWriter fileWriter;

    SerialPort[] portList;
    SerialPort comPort;

    int messageSize = 8;

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

    public void initialize() throws FileNotFoundException {
        // initialize port
        comPort.setBaudRate(9600);
        comPort.setNumDataBits(8);
        comPort.setNumStopBits(1);
        comPort.setParity(0);
        comPort.setFlowControl(0);
        comPort.openPort();

        // initialize file
        fileWriter = new FileWriter();
        fileWriter.InitializeFile("name");
    }

    public void reading(){
        try {
            while (true){
                while (comPort.bytesAvailable() < messageSize){
                    Thread.sleep(100);
                }
                byte[] readBuffer = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
                if (numRead > 0){
                    System.out.println("Read " + numRead + " bytes.");
                    readOutBuffer(readBuffer);
                    fileWriter.WriteToFile(SplitUpArray(readBuffer));
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
        //reverseBuffer(readBuffer);
        byte[][] splitArray = new byte[4][2];
        splitArray = SplitUpArray(readBuffer);
        int bufferLength = readBuffer.length;
        while(bufferLength > 0){
            bufferLength--;
            System.out.println(readBuffer[bufferLength]);
        }
        for (int i =0; i<4; i++){
            System.out.print(splitArray[i][0] + " ");
            System.out.println(splitArray[i][1]);
        }

    }

    private byte[] reverseBuffer(byte[] readBuffer){
      byte[] reversedBuffer;

      Collections.reverse(Arrays.asList(readBuffer));

      return readBuffer;
    }

    private byte[][] SplitUpArray(byte[] readBuffer){
        byte[][] splitArray = new byte[4][2];
        int bufferSize = readBuffer.length;
        bufferSize--;
        for (int i = 3; i>=0; i--){
            for (int j =0; j<2; j++){
                splitArray[i][j] = readBuffer[bufferSize];
                bufferSize--;
            }
        }

        return splitArray;
    }

}
