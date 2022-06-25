import java.io.*;
import java.util.*;

/**
 *  This program reads the entire Bitcoin data set and extracts the addresses
 *  of all dust senders, i.e., the input addresses of a dust-creating
 *  transaction. If a target address is specified, too, then the program
 *  will compute all dust senders for that specific address.
 *
 *  @author Matteo Loporchio
 */
public class TestSenders {
  // Lower and upper bounds for dust query range.
  public static final long lower = 1, upper = 545;

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: TestSenders <inputFile> <outputFile> [<address>]");
      System.exit(1);
    }
    // Read the input parameters.
    String inputFile = args[0],
    outputFile = args[1];
    long currentAddress = -1;
    // Check if the optional parameter has been supplied.
    if (args.length == 3) currentAddress = Long.parseLong(args[2]);
    // Open the input and output files.
    BufferedReader ir = new BufferedReader(new FileReader(inputFile));
    PrintWriter ow = new PrintWriter(outputFile);
    ow.println("txId,dustCreator");
    String line;
    while ((line = ir.readLine()) != null) {
      // Split the line and obtain info, inputs and outputs.
      String[] parts = line.split(":"),
      infos = parts[0].split(","),
      inputs = parts[1].split(";"),
      outputs = parts[2].split(";");
      // Parse the TX outputs and check if there is any dust output.
      // If the optional address has been supplied, we check if the
      // transaction contains a dust output directed at the address.
      boolean dust = false;
      if (!parts[2].equals("")) {
        int offset = 0;
        while (offset < outputs.length) {
          String[] outputParts = outputs[offset].split(",");
          long address = Long.parseLong(outputParts[0]);
          long amount = Long.parseLong(outputParts[1]);
          // If a dust output is found, we check the destination address.
          if (lower <= amount && amount <= upper) {
            if (currentAddress == -1 || currentAddress == address) {
              dust = true;
              break;
            }
          }
          offset++;
        }
      }
      // If dust has been detected in the outputs, then we collect all
      // sending addresses (if any) and write them on a file.
      if (dust && !parts[1].equals("")) {
        for (int i = 0; i < inputs.length; i++) {
          String[] inputParts = inputs[i].split(",");
          ow.printf("%s,%s\n", infos[2], inputParts[0]);
        }
      }
    }
    // Close input and output files.
    ir.close();
    ow.close();
  }
}
