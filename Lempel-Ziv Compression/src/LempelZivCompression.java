import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;


public class LempelZivCompression {

    private byte[] bytesInFile;
    private byte[] dictionary;
    private ArrayList<String> temp = new ArrayList<>();
    private String fileIn;
    private String fileOut;



    public LempelZivCompression(String fileIn, String fileOut) throws IOException {
        this.bytesInFile = new byte[0];
        this.fileOut = fileOut;
        readFile(fileIn);
    }



    private void readFile(String file) throws IOException {
        try {
            bytesInFile = Files.readAllBytes(Paths.get(file));
            dictionary = new byte[bytesInFile.length];
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private ArrayList<Byte> getBlock(int blockSize){
        ArrayList<Byte> block = new ArrayList<>();
        if (blockSize <= bytesInFile.length) {
            for (int i = 0; i < blockSize; i++) {
                block.add(bytesInFile[i]);
                bytesInFile = removeArrayElement(bytesInFile, i);
            }
        }
        return block;
    }

    private  byte[] removeArrayElement(byte[] array, int index){
        byte[] newArray = new byte[array.length - 1];
        for (int i = 0, k = 0; i < array.length; i++) {
            if (i == index) continue;

            newArray[k++] = array[i];
        }

        return newArray;
    }

    public void findSimilarBytes(int howFar){
        ArrayList<Byte> bytesToCheck = getBlock(howFar);
        String check = bytesToCheck.get(0).toString();
        for (int i = 0; i < bytesToCheck.size(); i++) {
            boolean found = false;
            for (int j = 0; j < temp.size(); j++) {
                if (check != temp.get(j)){
                    temp.add(check);
                    check = bytesToCheck.get(i).toString();
                    found = true;
                    break;
                }
            }
            if (!found){
                check = check + bytesToCheck.get(i + 1).toString();
            }
        }
    }

    public void print(){
        for (int i = 0; i < bytesInFile.length; i++) {
            System.out.println("Byte: " + bytesInFile[i] + "\n");
            char ch = (char)bytesInFile[i];
            System.out.println("char: " + ch + "\n");
        }
    }




}
