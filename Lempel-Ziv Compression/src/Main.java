import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {
        LempelZivCompression lempelZivCompression = new LempelZivCompression();

        lempelZivCompression.readFile("C:\\Users\\Vilhe\\IdeaProjects\\Algoritmer og Datastrukturer oblig 7\\lib\\Test.txt");

        System.out.println(lempelZivCompression.getBytesInFile());
    }

}
