import java.io.*;
import java.util.*;

/**
 *  This class can be used to parse the Bitcoin data set and
 *  create a CSV file containing information about TX outputs within
 *  a user-defined range.
 *
 *  Each row of the output file has the following structure:
 *
 *    <timestamp>,<blockId>,<txId>,<address>,<amount>,<scriptType>,<offset>
 *
 *  @author Matteo Loporchio
 */
public class FilterOutputs {
  public static void main(String[] args) throws Exception {
    if (args.length < 3) {
      System.err.println("Usage: FilterOutputs <inputFile> <outputFile> <lower> <upper>");
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
    ow.println("timestamp,blockId,txId,address,amount,scriptType,offset");
    // Read and process the input file.
    String line;
    while ((line = ir.readLine()) != null) {
      // Split the line and obtain info and outputs.
      String[] parts = line.split(":"),
      infos = parts[0].split(","),
      outputs = parts[2].split(";");
      // Parse the associated information.
      long timestamp = Long.parseLong(infos[0]);
      long blockId = Long.parseLong(infos[1]);
      long txId = Long.parseLong(infos[2]);
      // Extract outputs from TX data.
      if (!parts[2].equals("")) {
        for (int offset = 0; offset < outputs.length; offset++) {
          String[] outputParts = outputs[offset].split(",");
          long address = Long.parseLong(outputParts[0]);
          long amount = Long.parseLong(outputParts[1]);
          int scriptType = Integer.parseInt(outputParts[2]);
          // Write the TX output to file only if the corresponding
          // amount is in the desired range.
          if (lower <= amount && amount <= upper) {
            ow.printf("%d,%d,%d,%d,%d,%d,%d\n",
            timestamp, blockId, txId, address, amount, scriptType, offset);
          }
        }
      }
    }
    // Close input and output files.
    ir.close();
    ow.close();
  }
}
