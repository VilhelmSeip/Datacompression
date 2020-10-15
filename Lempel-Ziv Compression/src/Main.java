import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        LempelZivCompression lempelZivCompression = new LempelZivCompression("C:\\Users\\Vilhe\\IdeaProjects\\Oblig7\\Lempel-Ziv Compression\\lib\\opg7-2020.pdf",
                "C:\\Users\\Vilhe\\IdeaProjects\\Oblig7\\Lempel-Ziv Compression\\lib\\FileOutTest.txt", 127, 4);

        lempelZivCompression.compress();


    }

}
