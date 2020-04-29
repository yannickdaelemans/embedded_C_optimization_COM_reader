import java.io.FileNotFoundException;

public class main {

    public static void main(String[] args) throws FileNotFoundException {

        byte[] byteToWrite = new byte[1];
        byteToWrite[0] = '1';

        COMReader comReader = new COMReader();
        comReader.initialize("clock16MHz", "clock16MHzRAW");
        comReader.reading(byteToWrite, 500);

    }

}
