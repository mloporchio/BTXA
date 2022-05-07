import java.io.*;
import java.util.*;

/**
 *  This class can be used to parse the Bitcoin data set and
 *  create a CSV file containing information about TX inputs within
 *  a user-defined range.
 *
 *  Each row of the output file has the following structure:
 *
 *    <timestamp>,<blockId>,<txId>,<address>,<amount>,<prevTxId>,<prevTxOffset>
 *
 *  @author Matteo Loporchio
 */
public class FilterInputs {
  public static void main(String[] args) throws Exception {
    if (args.length < 3) {
      System.err.println("Usage: FilterInputs <inputFile> <outputFile> <lower> <upper>");
      System.exit(1);
    }
    String inputFile = args[0],
    outputFile = args[1];
    long lower = Long.parseLong(args[2]),
    upper = Long.parseLong(args[3]);
    // Open input and output files.
    BufferedReader ir = new BufferedReader(new FileReader(inputFile));
    PrintWriter ow = new PrintWriter(outputFile);
    // Write CSV header.
    ow.println("timestamp,blockId,txId,address,amount,prevTxId,prevTxOffset");
    // Read and process the input file.
    String line;
    while ((line = ir.readLine()) != null) {
      // Split the line and obtain info and inputs.
      String[] parts = line.split(":"),
      infos = parts[0].split(","),
      inputs = parts[1].split(";");
      // Parse the associated information.
      long timestamp = Long.parseLong(infos[0]);
      long blockId = Long.parseLong(infos[1]);
      long txId = Long.parseLong(infos[2]);
      // Extract inputs from TX data.
      if (!parts[1].equals("")) {
        for (int i = 0; i < inputs.length; i++) {
          String[] inputParts = inputs[i].split(",");
          long address = Long.parseLong(inputParts[0]),
          amount = Long.parseLong(inputParts[1]),
          prevTxId = Long.parseLong(inputParts[2]);
          int prevTxOffset = Integer.parseInt(inputParts[3]);
          // Print the TX input to file only if its amount matches
          // the desired condition.
          if (lower <= amount && amount <= upper) {
            ow.printf("%d,%d,%d,%d,%d,%d,%d\n", timestamp, blockId,
            txId, address, amount, prevTxId, prevTxOffset);
          }
        }
      }
    }
    // Close input and output files.
    ir.close();
    ow.close();
  }
}
