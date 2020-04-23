import com.fazecast.jSerialComm.SerialPort;

import java.io.*;
import java.util.*;

public class COMReader{
    private FileWriter fileWriter;

    private SerialPort[] portList;
    private SerialPort comPort;

    private int messageSize = 8;

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

    public void reading(byte[] write, int testAmount){
        try {
            while (testAmount > 0){
                comPort.writeBytes(write, 1);

                 //If, after 2 seconds 6 pieces of data have not been read, go further, and read out the buffer anyway
                 //do not write to file though!
                int timer = 20;
                while (comPort.bytesAvailable() < messageSize && timer >= 0){
                    Thread.sleep(100);
                    timer --;
                }

                byte[] readBuffer = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(readBuffer, readBuffer.length); // numRead is the amount of bytes that were read

                if (numRead >= messageSize){                                    // if the entire message was read, put it in the file
                    System.out.println("Read " + numRead + " bytes.");
                    readOutBuffer(readBuffer);
                    fileWriter.WriteToFile(SplitUpArray(readBuffer));
                }
                else if (numRead < messageSize && numRead >= 0){                // message not read, add one to test
                    System.out.println("Not enough bytes were read");
                    testAmount ++;
                }
                else if(numRead == -1){
                    System.out.println("An error occurred when reading");
                }
                testAmount--;
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

    public int getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(int messageSize) {
        this.messageSize = messageSize;
    }
}
