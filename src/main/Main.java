import algorithms.KMACXOF;
import algorithms.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;

public class Main {
    /**
     * The main method of the program. Execution starts here.
     * @param args Commandline arguments for the program.
     */
    public static void main(String[] args) {
        KMACXOF kmac = new KMACXOF();
        /*
            Needed features (all of them use KMACXOF256):
            1. Compute hash of file
            2. Compute MAC tag of file
            3. Encrypt byte array of file using a password
            4. Decrypt byte array of file using a password

            Commandline format:
            help: Lists all available commands and their parameters.
            hash <inputFilePath> <outputFilePath>: Computes the hash of the input file and writes it to the output file.
            hashtext "<inputText>" <outputFilePath>: Computes the hash of text input and writes it to the output file.
                                                    Surround the input text with quotation marks.
            mac <inputFilePath> <outputFilePath: Computes the MAC of the input file and writes it to the output file.
            mactext "<inputText>" <outputFilePath>: Computes the MAC of text input and writes it to the output file.
                                                    Surround the input text with quotation marks.
            encrypt <inputFilePath> <password> <outputFilePath>: Encrypts the input file using the provided password, then writes to the output file.
            decrypt <inputFilePath> <password> <outputFilePath>: Decrypts the input file using the provided password, then writes to the output file.
        */
        if (args.length == 0){
            System.out.println("This program runs with command line parameters. Run with -help for a list of " +
                    "available commands.");
            System.exit(-5);
        }
        String selectedMode = args[0].toLowerCase();
        if(selectedMode.compareTo("-help") == 0) {
            System.out.println("Commandline arguments:\n" +
         "-help: Lists all available commands and their parameters.\n" +
         "-hash <inputFilePath> <outputFilePath>: Computes the hash of the input file and writes it to the output file.\n" +
         "hashtext <inputText> <outputFilePath>: Computes the hash of text input and writes it to the output file.\n" +
         "-mac <inputFilePath> <password> <outputFilePath: Computes the MAC of the input file using the password and " +
                    "writes it to the output file.\n" +
         "-mactext <inputText> <password> <outputFilePath>: Computes the MAC of the text input using the password " +
                    "and writes it to the output file.\n" +
         "-encrypt <inputFilePath> <password> <outputFilePath>: Encrypts the input file using the provided password, " +
                    "then writes to the output file.\n" +
         "-decrypt <inputFilePath> <password> <outputFilePath>: Decrypts the input file using the provided password, " +
                    "then writes to the output file.\n");
        }
        else if (selectedMode.compareTo("-hash") == 0) {
            if (args.length != 3){
                System.out.println("Invalid amount of parameters.\nFormat: -hash <inputFilePath> <outputFilePath>");
                System.exit(-1);
            }
            byte[] inputByteArray = readInputFile(args[1]);
            byte[] result = kmac.compute("", inputByteArray, 64, "D");
            writeOutputFile(args[2], result);
        }
        else if (selectedMode.compareTo("-hashtext") == 0) {
            if (args.length != 3){
                System.out.println("Invalid amount of parameters.\nFormat: -hashtext <inputText> <outputFilePath>");
                System.exit(-1);
            }
            byte[] inputByteArray = args[1].getBytes();
            byte[] result = kmac.compute("", inputByteArray, 64, "D");
            writeOutputFile(args[2], result);
        }
        else if (selectedMode.compareTo("-mac") == 0) {
            if (args.length != 4){
                System.out.println("Invalid amount of parameters.\nFormat: -mac <inputFilePath> <password> " +
                        "<outputFilePath>");
                System.exit(-1);
            }
            byte[] inputByteArray = readInputFile(args[1]);
            byte[] result = kmac.compute(args[2], inputByteArray, 64, "T");
            writeOutputFile(args[3], result);
        }
        else if (selectedMode.compareTo("-mactext") == 0) {
            if (args.length != 4){
                System.out.println("Invalid amount of parameters.\nFormat: -mactext <inputText> <password> " +
                        "<outputFilePath>");
                System.exit(-1);
            }
            byte[] inputByteArray = args[1].getBytes();
            byte[] result = kmac.compute(args[2], inputByteArray, 64, "T");
            writeOutputFile(args[3], result);
        }
        else if (selectedMode.compareTo("-encrypt") == 0) {
            if (args.length != 4){
                System.out.println("Invalid amount of parameters.\nFormat: -encrypt <inputFilePath> <password> " +
                        "<outputFilePath>");
                System.exit(-1);
            }
            byte[] inputByteArray = readInputFile(args[1]);
            SecureRandom random = new SecureRandom();
            byte[] randomBytes = new byte[64];
            random.nextBytes(randomBytes);
            byte[] keka = kmac.compute(Utils.concat(randomBytes, Utils.encodeString(args[2])), Utils.encodeString(""), 128, "S");
            byte[] ke = new byte[64];
            System.arraycopy(keka, 0, ke, 0, 64);
            byte[] ka = new byte[64];
            System.arraycopy(keka, 64, ka, 0, 64);
            byte[] c = kmac.compute(ke, Utils.encodeString(""), inputByteArray.length, "SKE");
            for(int i = 0; i < c.length; i++) { // XOR c and the message together.
                c[i] ^= inputByteArray[i];
            }
            byte[] t = kmac.compute(ka, inputByteArray, 64, "SKA");
            byte[] result = new byte[c.length + 128];
            System.arraycopy(randomBytes, 0, result, 0, 64);
            System.arraycopy(c, 0, result, 64, c.length);
            System.arraycopy(t, 0, result, result.length - 64, 64);
            writeOutputFile(args[3], result);
        }
        else if(selectedMode.compareTo("-decrypt") == 0) {
            if (args.length != 4){
                System.out.println("Invalid amount of parameters.\nFormat: -decrypt <inputFilePath> <password> " +
                        "<outputFilePath>");
                System.exit(-1);
            }
            byte[] inputByteArray = readInputFile(args[1]);
            byte[] randomBytes = new byte[64];
            System.arraycopy(inputByteArray, 0, randomBytes, 0, 64);
            byte[] c = new byte[inputByteArray.length - 128];
            System.arraycopy(inputByteArray, 64, c, 0, inputByteArray.length - 128);
            byte[] t = new byte[64];
            System.arraycopy(inputByteArray, inputByteArray.length - 64, t, 0, 64);

            byte[] keka = kmac.compute(Utils.concat(randomBytes, Utils.encodeString(args[2])), Utils.encodeString(""), 128, "S");
            byte[] ke = new byte[64];
            System.arraycopy(keka, 0, ke, 0, 64);
            byte[] ka = new byte[64];
            System.arraycopy(keka, 64, ka, 0, 64);
            byte[] result = kmac.compute(ke, Utils.encodeString(""), c.length, "SKE");
            for(int i = 0; i < result.length; i++) { // XOR c and the message together.
                result[i] ^= c[i];
            }
            byte[] newT = kmac.compute(ka, result, 64, "SKA");
            for(int i = 0; i < t.length; i++) {
                if(t[i] != newT[i]) {
                    System.out.println("Could not decrypt file.");
                    System.exit(-4);
                }
            }
            writeOutputFile(args[3], result);
        } else {
            System.out.println("Parameter not recognized. Run with parameter -help for list of available commands.");
        }
    }

    private static byte[] readInputFile(String input) {
        File file;
        byte[] byteArray = new byte[0];
        try {
            file = new File(input);
            byteArray = Files.readAllBytes(file.toPath());
        }
        catch (FileNotFoundException e) {
            System.out.println("Input file could not be found.");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("IOException while reading input file.");
            System.exit(-2);
        }
        
        return byteArray;
    }

    private static void writeOutputFile(String outputPath, byte[] outputData) {
        try(FileOutputStream output = new FileOutputStream(outputPath)) {
            output.write(outputData);
        }
        catch(IOException e) {
            System.out.println("Could not write output file");
            System.exit(-3);
        }
    }
}