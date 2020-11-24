import java.io.FileNotFoundException;


public class main {

    public static void main(String[] args) throws FileNotFoundException {

        ScannerClass scanner = new ScannerClass();

        byte[] byteToWrite = new byte[1];
        byteToWrite[0] = 'M';

        COMReader comReader = new COMReader(scanner.getProtocolSize(), scanner.getCOMPort());
        comReader.initialize(scanner.getFileName(), scanner.getFileName()+"raw");
        comReader.reading(scanner.getByteToWrite(), 50000);

    }


}
