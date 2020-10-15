import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;


public class LempelZivCompression {

    private byte[] bytesInFile;
    private byte[] compressed_bytes;
    private byte[] tempBlock;
    private String inputFile;
    private int distance_back;
    private int wordMinSize;
    private int bytesLeft;


    public LempelZivCompression(){
        this.bytesInFile = new byte[0];
    }

    public LempelZivCompression(String inputFile) {
        this.inputFile = inputFile;
        this.bytesInFile = new byte[0];
    }

    public void readFile(String file) throws IOException {
        try {
            bytesInFile = Files.readAllBytes(Paths.get(file));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private int locateSimilarByte(ArrayList<Byte> bytes, int startIndex){

        int whereToStart = startIndex - distance_back;
        int howFar = distance_back;
        if (whereToStart < 0){
            whereToStart = 0;
            howFar = startIndex;
        }

        for (int i = whereToStart; i <= (howFar - wordMinSize); i++){
            boolean located = true;
            for (int j = i, k = 0; k < bytes.size(); j++, k++) {
                if (tempBlock[j] != bytes.get(k)){
                    located = false;
                    break;
                }
            }

            if (located){
                return i;
            }
        }
        return -2;
    }

    private void getNewBlock(int bytesLeft){
        int get = 0;
        if (bytesLeft < distance_back){
            tempBlock = new byte[bytesLeft];
            for (int i = 0; i < bytesLeft; i++, get++){
                tempBlock[i] = bytesInFile[get];
            }
            this.bytesLeft = 0;
        }else {
            tempBlock = new byte[distance_back];
            for (int i = 0; i < distance_back; i++, get++){
                tempBlock[i] = bytesInFile[get];
            }
            this.bytesLeft -= distance_back;
        }
    }



    public byte[] getBytesInFile() {
        return bytesInFile;
    }


}
