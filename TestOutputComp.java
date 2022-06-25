import java.io.*;
import java.util.*;

/**
 *  This program reads the entire Bitcoin data set and extracts all
 *  dust-creating transactions (i.e., those with at least one dust output).
 *  For each dust-creating transaction, we count the number of dust outputs
 *  and the total number of outputs.
 *
 *  @author Matteo Loporchio
 */
public class TestOutputComp {
  // Lower and upper bound for Bitcoin dust.
  public static final long lower = 1, upper = 545;

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: TestOutputComp <inputFile> <outputFile>");
      System.exit(1);
    }
    String inputFile = args[0], outputFile = args[1];
    // Open the input and output files.
    BufferedReader ir = new BufferedReader(new FileReader(inputFile));
    PrintWriter ow = new PrintWriter(outputFile);
    // Write CSV header.
    ow.println("txId,dustCount,outputCount");
    String line;
    while ((line = ir.readLine()) != null) {
      // Split the line and obtain info and outputs.
      String[] parts = line.split(":"),
      infos = parts[0].split(","),
      outputs = parts[2].split(";");
      // Extract the transaction identifier.
      long txId = Long.parseLong(infos[2]);
      // Examine the outputs and count how many of them are dust.
      int dustCount = 0;
      if (!parts[2].equals("")) {
        for (int offset = 0; offset < outputs.length; offset++) {
          String[] outputParts = outputs[offset].split(",");
          long amount = Long.parseLong(outputParts[1]);
          // Check if the output is dust and increment the counter.
          if (lower <= amount && amount <= upper) dustCount++;
        }
      }
      // If the current transaction is a dust-creating one, we write
      // the collected information to the output file.
      if (dustCount > 0) ow.printf("%d,%d,%d\n", txId, dustCount, outputs.length);
    }
    // Close the input and output files.
    ir.close();
    ow.close();
  }
}
