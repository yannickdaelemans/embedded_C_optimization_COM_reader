import java.util.Scanner;

public class ScannerClass {

    // variables
    String COMPort = "COM1";
    byte[] byteToWrite = new byte[1];
    String fileName;
    int protocolSize = 0;

    public ScannerClass(){
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter COMPort to use");
        COMPort = myObj.nextLine();  // Read user input
        System.out.println("Enter Filename ");
        fileName = myObj.nextLine();  // Read user input
        System.out.println("Enter protocol size ");
        protocolSize = myObj.nextInt();  // Read user input
        System.out.println("Enter character ");
        char input = myObj.next().charAt(0);
        byteToWrite[0] =  (byte)input;  // Read user input

    }

    public String getCOMPort() {
        return COMPort;
    }

    public void setCOMPort(String COMPort) {
        this.COMPort = COMPort;
    }

    public byte[] getByteToWrite() {
        return byteToWrite;
    }

    public void setByteToWrite(byte[] byteToWrite) {
        this.byteToWrite = byteToWrite;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getProtocolSize() {
        return protocolSize;
    }

    public void setProtocolSize(int protocolSize) {
        this.protocolSize = protocolSize;
    }
}
