import java.io.FileNotFoundException;

public class main {

    public static void main(String[] args) throws FileNotFoundException {
        COMReader comReader = new COMReader();
        comReader.initialize();
        comReader.reading();

    }

}
