public class main {

    public static void main(String[] args) {
        COMReader comReader = new COMReader();
        comReader.initPort();
        comReader.reading();
    }

}