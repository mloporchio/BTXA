import java.io.*;
import java.util.*;

/**
 *  This program reads the entire Bitcoin data set and extracts all
 *  dust-consuming transactions (i.e., those with at least one dust input).
 *  For each dust-consuming transaction, we count the number of dust inputs
 *  and the total number of inputs.
 *
 *  @author Matteo Loporchio
 */
public class TestInputComp {
  // Lower and upper bound for Bitcoin dust.
  public static final long lower = 1, upper = 545;

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: TestInputComp <inputFile> <outputFile>");
      System.exit(1);
    }
    String inputFile = args[0], outputFile = args[1];
    // Open the input and output files.
    BufferedReader ir = new BufferedReader(new FileReader(inputFile));
    PrintWriter ow = new PrintWriter(outputFile);
    // Write CSV header.
    ow.println("txId,dustCount,inputCount");
    String line;
    while ((line = ir.readLine()) != null) {
      // Split the line and obtain info and inputs.
      String[] parts = line.split(":"),
      infos = parts[0].split(","),
      inputs = parts[1].split(";");
      // Extract the transaction identifier.
      long txId = Long.parseLong(infos[2]);
      // Examine the inputs and count how many of them are dust.
      int dustCount = 0;
      if (!parts[1].equals("")) {
        for (int offset = 0; offset < inputs.length; offset++) {
          String[] inputParts = inputs[offset].split(",");
          long amount = Long.parseLong(inputParts[1]);
          // Check if the output is dust and increment the counter.
          if (lower <= amount && amount <= upper) dustCount++;
        }
      }
      // If the current transaction is a dust-consuming one, we write
      // the collected information to the output file.
      if (dustCount > 0) ow.printf("%d,%d,%d\n", txId, dustCount, inputs.length);
    }
    // Close the input and output files.
    ir.close();
    ow.close();
  }
}
