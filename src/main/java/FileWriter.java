import java.io.*;

public class FileWriter {

    File file;
    FileOutputStream fos;
    BufferedWriter bW;
    String fileName;

    public void InitializeFile(String name) throws FileNotFoundException{
        try{
            fileName = name;
            file = new File(name+".txt");
            if(file.createNewFile()){
                System.out.println("File created with name: "+ name);
            } else{
                System.out.println("File could not be created!");
                System.exit(0);
            }
        }catch (IOException e){
            System.out.println(e);
        }

        try {
            fos = new FileOutputStream(file);
            bW = new BufferedWriter(new OutputStreamWriter(fos));
        }catch (FileNotFoundException e){
            System.out.println(e);
        }
    }

    public void WriteToFile(byte[][] splitArray) throws IOException {
        try{
            for (int i =0; i<4; i++){
                bW.write(splitArray[i][0]);
                bW.newLine();
                bW.write(splitArray[i][1]);
                bW.newLine();
            }
        }catch (IOException e){
            System.out.println(e);
        }

    }

    public void closeBufferdWriter() throws IOException {
        try {
            bW.close();
        }catch (IOException e){
            System.out.println(e);
        }

    }

}
