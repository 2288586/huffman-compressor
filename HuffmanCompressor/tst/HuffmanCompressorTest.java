import org.junit.jupiter.api.Test;

import java.io.File;

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
}