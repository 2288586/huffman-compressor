import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HuffmanCompressorTest {

    @Test
    void compressNullDecompressedFile() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> HuffmanCompressor.compress(null, new File("")));
        assertEquals("Input and output file must be specified.", exception.getMessage());
    }

    @Test
    void compressNullCompressedFile() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> HuffmanCompressor.compress(new File(""), null));
        assertEquals("Input and output file must be specified.", exception.getMessage());
    }

    @Test
    void decompressNullCompressedFile() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> HuffmanCompressor.decompress(null, new File("")));
        assertEquals("Input and output file must be specified.", exception.getMessage());
    }


    @Test
    void decompressNullDecompressedFile() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> HuffmanCompressor.decompress(new File(""), null));
        assertEquals("Input and output file must be specified.", exception.getMessage());
    }

    @Test
    void compressDecompressFile() throws Exception {
        File expectedOutputFile = new File("tst/ExpectedOutputFile.txt");
        File compressedFile = new File("tst/CompressedFile.txt");
        File decompressedFile = new File("tst/DecompressedFile.txt");

        HuffmanCompressor.compress(expectedOutputFile, compressedFile);
        HuffmanCompressor.decompress(compressedFile, decompressedFile);

        /*FileReader expectedFileReader = new FileReader(expectedOutputFile);
        BufferedReader expectedBufferedReader = new BufferedReader(expectedFileReader);

        FileReader actualFileReader = new FileReader(decompressedFile);
        BufferedReader actualBufferedReader = new BufferedReader(actualFileReader);

        String expectedLine = "";
        String actualLine = "";

        while (expectedLine != null) {
            expectedLine = expectedBufferedReader.readLine();
            actualLine = actualBufferedReader.readLine();
            assertEquals(expectedLine, actualLine);
        }*/
    }
}