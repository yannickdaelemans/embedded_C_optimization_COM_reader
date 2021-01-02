import java.io.FileNotFoundException;


public class main {

    public static void main(String[] args) throws FileNotFoundException {

        ScannerClass scanner = new ScannerClass();

        COMReader comReader = new COMReader(scanner.getProtocolSize(), scanner.getCOMPort());
        comReader.initialize(scanner.getFileName(), scanner.getFileName()+"raw");

        for(int i = 0; i < scanner.getTests(); i++) {
            comReader.reading(scanner.getByteToWrite(), 50000);
        }
        comReader.closeAll();

    }


}
