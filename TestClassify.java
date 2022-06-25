import java.io.*;
import java.util.*;

/**
 *  This program classifies transactions according to their inputs.
 *
 *  - Type 0: transactions without any input.
 *  - Type 1: transactions that do not aggregate any dust output.
 *  - Type 2: transactions that combine dust with larger amounts.
 *  - Type 3: transactions that only aggregate dust outputs.
 *  - Type 4: unrecognized type.
 *
 *  @author Matteo Loporchio
 */
public class TestClassify {
  public static final int DUST_LIMIT = 545;
  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: TestClassify <inputFile> <outputFile>");
      System.exit(1);
    }
    String inputFile = args[0],
    outputFile = args[1];
    BufferedReader ir = new BufferedReader(new FileReader(inputFile));
    PrintWriter ow = new PrintWriter(outputFile);
    ow.println("txId,type");
    String line;
    while ((line = ir.readLine()) != null) {
      // Split the line and obtain info and inputs.
      String[] parts = line.split(":"),
      infos = parts[0].split(","),
      inputs = parts[1].split(";");
      // Parse the associated information.
      long txId = Long.parseLong(infos[2]);
      // Examine TX inputs.
      long minInput = Long.MAX_VALUE, maxInput = Long.MIN_VALUE;
      if (!parts[1].equals("")) {
        for (int i = 0; i < inputs.length; i++) {
          String[] inputParts = inputs[i].split(",");
          long amount = Long.parseLong(inputParts[1]);
          if (amount <= minInput) minInput = amount;
          if (amount >= maxInput) maxInput = amount;
        }
      }
      // Classify the transaction.
      int type = classify(minInput, maxInput);
      // Write statistics to the output file.
      if (type != 4) ow.printf("%d,%d\n", txId, type);
      else ow.printf("%d,%d-%d\n", txId, minInput, maxInput);
    }
    // Close input and output files.
    ir.close();
    ow.close();
  }

  /**
   *  Transaction classification function.
   */
  public static int classify(long minInput, long maxInput) {
    // Type 0: transactions without any input.
    if (minInput == Long.MAX_VALUE && maxInput == Long.MIN_VALUE) return 0;
    // Type 1: transactions that do not aggregate any dust.
    if (minInput > DUST_LIMIT) return 1;
    // Type 2: transactions that combine dust with larger amounts.
    if ((0 <= minInput && minInput <= DUST_LIMIT) && maxInput > DUST_LIMIT) return 2;
    // Type 3: transactions that only aggregate dust.
    if ((0 <= minInput && minInput <= DUST_LIMIT) && (0 <= maxInput && maxInput <= DUST_LIMIT)) return 3;
    // Type 4: unrecognized transaction type.
    return 4;
  }
}
