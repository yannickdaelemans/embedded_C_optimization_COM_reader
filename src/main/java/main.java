import java.io.FileNotFoundException;

public class main {

    public static void main(String[] args) throws FileNotFoundException {

        byte[] byteToWrite = new byte[0];
        byteToWrite[0] = 1;

        COMReader comReader = new COMReader();
        comReader.initialize();
        comReader.reading(byteToWrite, 500);

    }

}
