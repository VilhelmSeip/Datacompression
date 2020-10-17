import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;


public class LempelZivCompression {

    private byte[] bytesInFile;
    private byte[] compressed_bytes;
    private byte[] tempBlock;
    private String fileOut;
    private final int distance_back;
    private final int wordMinSize;
    private int bytesLeft;


    public LempelZivCompression(){
        this.bytesInFile = new byte[0];
        this.distance_back = 127;
        this.wordMinSize = 4;
    }

    public LempelZivCompression(String inputFile, String fileOut, int distance_back, int wordMinSize) throws IOException {
        this.bytesInFile = new byte[0];
        this.fileOut = fileOut;
        this.distance_back = distance_back;
        this.wordMinSize = wordMinSize;
        readFile(inputFile);
    }

    private void readFile(String file) throws IOException {
        try {
            bytesInFile = Files.readAllBytes(Paths.get(file));
            compressed_bytes = new byte[bytesInFile.length];
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
                try {
                    if (tempBlock[j] != bytes.get(k)) {
                        located = false;
                        break;
                    }
                }catch (Exception e){ }
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

    public void compress(){

        bytesLeft = bytesInFile.length;
        int bufferindex = 0;

        while (bytesLeft != 0){

            getNewBlock(bytesLeft);
            int byteIndex = 0;
            boolean compressionFound = false;
            int compressionIndex = -2;
            int bytesFinished = 0;

            for (int i = 0; i < bytesInFile.length; i++) {

                ArrayList<Byte> presentBytes = new ArrayList<>();

                compressionFound = false;
                int compressionLength = -2;
                int uncompressedBytes = -2;
                int compressionStartIndex = 0;

                for (int j = 0; j < bytesInFile.length; j++) {
                    presentBytes.add(bytesInFile[j]);

                    if (presentBytes.size() >= wordMinSize && i >= wordMinSize && bytesInFile.length - i >= wordMinSize) {

                        int compressionPlace = locateSimilarByte(presentBytes, i);
                        if (compressionPlace >= 0) {
                            compressionFound = true;
                            compressionIndex = i;
                            compressionStartIndex = compressionPlace;
                            compressionLength = presentBytes.size();
                        } else break;
                    }
                }


                if (compressionFound) {
                    int uncompressed = compressionIndex - bytesFinished;
                    compressed_bytes[bufferindex] = (byte)uncompressed;

                    bufferindex++;

                    for (int h = bytesFinished; h < compressionIndex; h++, bufferindex++) {
                        compressed_bytes[bufferindex] = bytesInFile[h];
                    }
                    int back = compressionIndex -compressionStartIndex;

                    compressed_bytes[bufferindex] = (byte) back;
                    bufferindex++;
                    compressed_bytes[bufferindex] = (byte) compressionLength;
                    i += compressionLength;
                }
            }

            int uncompressed = bytesInFile.length - bytesFinished;

            compressed_bytes[bufferindex] = (byte) - uncompressed;
            bufferindex++;

            for (int y = bytesFinished; y < bytesInFile.length; y ++){
                compressed_bytes[bufferindex] = bytesInFile[y];
            }
        }

        byte[] buffer = compressed_bytes;
        emptyBufferBytesFix(buffer, bufferindex);
        writeToFile();
    }

    private void writeToFile() {
        try{
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(fileOut));
            dataOutputStream.write(compressed_bytes);
            dataOutputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void emptyBufferBytesFix(byte[] buffer, int bufferLength) {
        compressed_bytes = new byte[bufferLength];
        for (int i = 0; i < bufferLength; i++) {
            compressed_bytes[i] = buffer[i];
        }
    }

    public void compress(String inputFile, String fileOut) throws IOException {
        this.fileOut = fileOut;
        readFile(inputFile);
        compress();
    }




}
