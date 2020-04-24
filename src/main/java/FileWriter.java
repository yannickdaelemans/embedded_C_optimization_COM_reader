import java.io.*;

import static java.lang.Math.abs;

public class FileWriter {

    File file;
    File fileRaw;
    FileOutputStream fosRaw;
    BufferedWriter bWRaw;
    FileOutputStream fos;
    BufferedWriter bW;
    String fileName;
    String fileNameRaw;

    public void InitializeFile(String name, String nameRaw) throws FileNotFoundException {
        try {
            fileName = name;
            file = new File(name + ".txt");
            if (file.createNewFile()) {
                System.out.println("File created with name: " + name);
            } else {
                System.out.println("File could not be created!");
                System.exit(0);
            }
            fileNameRaw = nameRaw;
            fileRaw = new File(nameRaw + ".txt");
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

    public void WriteToFileRaw(byte[][] splitArray) throws IOException {
        try {
            for (int i = 0; i < 5; i++) {
                bWRaw.write(splitArray[i][0]);
                bWRaw.newLine();
                bWRaw.write(splitArray[i][1]);
                bWRaw.newLine();
            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public void WriteToFile(byte[][] splitArray) throws IOException {
        try {
            for (int i = 0; i < 5; i++) {
                String writeMSB = Integer.toHexString(splitArray[i][0] & 0xFF);
                String writeLSB;
                if ((splitArray[i][1] & 0xFF) > 15) {
                    writeLSB = Integer.toHexString(splitArray[i][1] & 0xFF);
                } else {
                    writeLSB = "0" + Integer.toHexString(splitArray[i][1] & 0xFF); // make sure you have 0x0..
                }
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
