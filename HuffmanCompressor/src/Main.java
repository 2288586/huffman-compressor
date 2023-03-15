import java.io.File;

public class Main {
    public static void main(String[] args) {
        if (Boolean.parseBoolean(args[6])) {
            Logger.setPrintStream(System.out);
        }

        File inputFile = new File(args[2]);
        File outputFile = new File(args[4]);

        if (args[0].equals("-compress")) {

            try {
                HuffmanCompressor.compress(inputFile, outputFile);
            } catch (Exception exception) {
                Logger.log(exception.getMessage());
            }

        } else if (args[0].equals("-decompress")) {

            try {
                HuffmanCompressor.decompress(inputFile, outputFile);
            } catch (Exception exception) {
                Logger.log(exception.getMessage());
            }

        }
    }
}