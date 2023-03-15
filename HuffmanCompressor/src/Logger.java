import java.io.PrintStream;

public class Logger {

    private static PrintStream printStream = null;

    public static void setPrintStream(PrintStream newPrintStream) {
        printStream = newPrintStream;
    }

    public static void log(String message) {
        if (printStream != null) {
            printStream.println(message);
        }
    }
}