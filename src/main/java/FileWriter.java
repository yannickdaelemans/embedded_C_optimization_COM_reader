import java.io.*;

public class FileWriter {

    private File file;
    private File fileRaw;
    private FileOutputStream fosRaw;
    private BufferedWriter bWRaw;
    private FileOutputStream fos;
    private BufferedWriter bW;
    private String fileName;
    private String fileNameRaw;
    private int writeSize = 0;

    /*
     * Initializing the file with the right name
     */
    public void InitializeFile(String name, String nameRaw, int protocolSize) throws FileNotFoundException {
        try {
            writeSize = protocolSize;
            fileName = name;
            String userHomeFolder = System.getProperty("user.home");
            file = new File(userHomeFolder, name + ".txt");
            if (file.createNewFile()) {
                System.out.println("File created with name: " + name);
            } else {
                System.out.println("File could not be created!");
                System.exit(0);
            }
            fileNameRaw = nameRaw;
            fileRaw = new File(userHomeFolder, nameRaw + ".txt");
            //fileRaw = new File(nameRaw + ".txt");
            if (fileRaw.createNewFile()) {
                System.out.println("File created with name: " + nameRaw);
            } else {
                System.out.println("File could not be created!");
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        try {
            fos = new FileOutputStream(file);
            bW = new BufferedWriter(new OutputStreamWriter(fos));
            fosRaw = new FileOutputStream(fileRaw);
            bWRaw = new BufferedWriter(new OutputStreamWriter(fosRaw));
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    public void WriteToFileRaw(byte[][] splitArray, int length) throws IOException {
        try {
            for (int i = 0; i < length; i++) {
                bWRaw.write(splitArray[i][0]);
                bWRaw.newLine();
                bWRaw.write(splitArray[i][1]);
                bWRaw.newLine();
            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    /*
     * Write to file in order of protocol size.
     */
    public void WriteToFile(byte[][] splitArray, int length) throws IOException {
        try {
            for (int i = 0; i < length; i++) {
                String writeMSB = String.format("%8s", Integer.toBinaryString(splitArray[i][0] & 0xFF)).replace(' ', '0');
                String writeLSB;
                writeLSB = String.format("%8s", Integer.toBinaryString(splitArray[i][1] & 0xFF)).replace(' ', '0');
                bW.write(writeMSB + writeLSB);
                bW.newLine();
            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public void closeBufferdWriter() throws IOException {
        try {
            bW.close();
            bWRaw.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

}
