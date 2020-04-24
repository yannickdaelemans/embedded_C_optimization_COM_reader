import com.fazecast.jSerialComm.SerialPort;

import java.io.*;
import java.util.*;


public class COMReader {
    private FileWriter fileWriter;

    private SerialPort[] portList;
    SerialPort comPort;

    private int messageSize = 10;

    public COMReader() {
        portList = SerialPort.getCommPorts();
        int listLength = portList.length;

        while (listLength > 0) {
            listLength--;
            System.out.println(listLength);
            SerialPort currPort = portList[listLength];
            System.out.println(currPort.getSystemPortName());
            if (currPort.getSystemPortName().equals("COM11")) {
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
        fileWriter.InitializeFile("name", "nameRaw");
    }

    public void reading(byte[] write, int testAmount) {
        try {
            while (testAmount > 0) {
                System.out.println("here");
                if (comPort.writeBytes(write, 1) < 0) {
                    System.out.println("an error occurred when sending over UART");
                }

                //If, after 2 seconds 6 pieces of data have not been read, go further, and read out the buffer anyway
                //do not write to file though!
                int timer = 2000;
                while (comPort.bytesAvailable() < messageSize && timer >= 0) {
                    Thread.sleep(1);
                    timer--;
                }

                byte[] readBuffer = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(readBuffer, readBuffer.length); // numRead is the amount of bytes that were read

                if (numRead >= messageSize) {                                    // if the entire message was read, put it in the file
                    System.out.println("Read " + numRead + " bytes.");
                    readOutBuffer(readBuffer);
                    fileWriter.WriteToFileRaw(SplitUpArray(readBuffer));
                    fileWriter.WriteToFile(SplitUpArray(readBuffer));
                } else if (numRead < messageSize && numRead >= 0) {                // message not read, add one to test
                    System.out.println("Not enough bytes were read");
                    System.out.println("amount of bytes read: " + numRead);
                    testAmount++;
                } else if (numRead == -1) {
                    System.out.println("An error occurred when reading");
                }
                testAmount--;
            }
            fileWriter.closeBufferdWriter();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void readOutBuffer(byte[] readBuffer) {
        //reverseBuffer(readBuffer);
        byte[][] splitArray = new byte[messageSize/2][2];
        splitArray = SplitUpArray(readBuffer);
        int bufferLength = readBuffer.length;
        while (bufferLength > 0) {
            bufferLength--;
            System.out.println(readBuffer[bufferLength]);
        }
        for (int i = 0; i < messageSize/2; i++) {
            String st1 = Integer.toHexString(splitArray[i][0] & 0xFF);
            String st2 = Integer.toHexString(splitArray[i][1] & 0xFF);
            System.out.print(splitArray[i][0] + " Hex: " + st1 + " ");
            System.out.println(splitArray[i][1] + " Hex: " + st2);
        }

    }

    private byte[] reverseBuffer(byte[] readBuffer) {
        byte[] reversedBuffer;

        Collections.reverse(Arrays.asList(readBuffer));

        return readBuffer;
    }

    private byte[][] SplitUpArray(byte[] readBuffer) {
        byte[][] splitArray = new byte[messageSize/2][2];
        int bufferSize = readBuffer.length;
        bufferSize--;
        for (int i = (messageSize/2)-1; i >= 0; i--) {
            for (int j = 0; j < 2; j++) {
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
