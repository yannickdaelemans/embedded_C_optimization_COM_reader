import com.fazecast.jSerialComm.SerialPort;

import java.io.*;
import java.util.*;


public class COMReader {
    private FileWriter fileWriter;

    private SerialPort[] portList;
    SerialPort comPort;

    private int messageSize;

    public COMReader(int protocolSize, String COMPort) {
        messageSize = protocolSize*2;

        portList = SerialPort.getCommPorts();
        int listLength = portList.length;

        while (listLength > 0) {
            listLength--;
            System.out.println(listLength);
            SerialPort currPort = portList[listLength];
            System.out.println(currPort.getSystemPortName());
            if (currPort.getSystemPortName().equals(COMPort)) {
                comPort = currPort;
                System.out.println("connecting to: " + comPort.getPortDescription());
                break;
            }
        }
        if (listLength == 0) {
            System.out.println("ERROR: Port could not be found");
            System.out.println("Program shutting down");
            System.exit(0);
        }
    }

    /*
     * Initialize the communication over USB
     */
    public void initialize(String fileName, String fileNameRaw) throws FileNotFoundException {
        // initialize port
        comPort.setBaudRate(9600);
        comPort.setNumDataBits(8);
        comPort.setNumStopBits(1);
        comPort.setParity(0);
        comPort.setFlowControl(0);
        comPort.openPort();

        // initialize file
        fileWriter = new FileWriter();
        fileWriter.InitializeFile(fileName, fileNameRaw, messageSize/2);
    }

    /*
     * Start the reading from the USB
     * First write the right byte to the target.
     * Then wait for data to come in.
     * If there is any data, split it up correclty and push it to a file.
     */
    public void reading(byte[] write, int testAmount) {
        try {
            if (comPort.writeBytes(write, 1) < 0) {
                System.out.println("an error occurred when sending over UART");
            }
            while (testAmount > 0) {
                //System.out.println("here");
                //If, after half a second there is no data,  go further, and read out the buffer anyway
                int timer = 500;
                while (comPort.bytesAvailable() <= 0 && timer >= 0) {
                    Thread.sleep(1);
                    timer--;
                }
                if(timer <= 0){
                    System.out.println("Time expired");
                    testAmount = -1;
                }

                byte[] readBuffer = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(readBuffer, readBuffer.length); // numRead is the amount of bytes that were read

                if (numRead >= messageSize) {                                    // if the entire message was read, put it in the file
                    fileWriter.WriteToFile(SplitUpArray(readBuffer), readBuffer.length/2);
                } else if (numRead == -1) {
                    System.out.println("An error occurred when reading");
                }
                testAmount--;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void readOutBuffer(byte[] readBuffer) {
        byte[][] splitArray = new byte[messageSize/2][2];
        splitArray = SplitUpArray(readBuffer);
        int bufferLength = readBuffer.length;
        while (bufferLength > 0) {
            bufferLength--;
            System.out.println(readBuffer[bufferLength]);
        }

        for (int i = 0; i < readBuffer.length/2; i++) {
            String st1 = String.format("%8s", Integer.toBinaryString(splitArray[i][0] & 0xFF)).replace(' ', '0');
            String st2 = String.format("%8s", Integer.toBinaryString(splitArray[i][1] & 0xFF)).replace(' ', '0');
            System.out.print(splitArray[i][0] + " Hex: " + st1 + " ");
            System.out.println(splitArray[i][1] + " Hex: " + st2);
        }

    }

    private byte[] reverseBuffer(byte[] readBuffer) {
        byte[] reversedBuffer;

        Collections.reverse(Arrays.asList(readBuffer));

        return readBuffer;
    }

    /*
     * put 2 bytes after each other in a 2D matrix
     */
    private byte[][] SplitUpArray(byte[] readBuffer) {
        byte[][] splitArray = new byte[readBuffer.length/2][2];
        int bufferSize = readBuffer.length;
        bufferSize--;
        for (int i = (readBuffer.length/2)-1; i >= 0; i--) {
            for (int j = 0; j < 2; j++) {
                splitArray[i][j] = readBuffer[bufferSize];
                bufferSize--;
            }
        }
        return splitArray;
    }

    public void closeAll(){
        try{
            fileWriter.closeBufferdWriter();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
