import java.io.*;
import java.util.*;

/**
 *  This program performs a linear scan of the entire Bitcoin data set.
 *  Each block is analyzed to retrieve all transaction outputs
 *  within the desired query range.
 *  The program produces two different output files:
 *
 *  1. A CSV file containing information about all TX outputs that satisfy
 *  the query.
 *  2. A CSV file containing statistics about the query process and
 *  result size.
 *
 *  @author Matteo Loporchio
 */
public class TestScan {
  public static void main(String[] args) throws Exception {
    if (args.length < 5) {
      System.err.println("Usage: TestScan <inputFile> <dataFile> <statsFile> <lower> <upper>");
      System.exit(1);
    }
    // Read the input parameters.
    String inputFile = args[0];
    String dataFile = args[1];
    String statsFile = args[2];
    long lower = Long.parseLong(args[3]);
    long upper = Long.parseLong(args[4]);
    Interval q = new Interval(lower, upper);
    // Open input and output files.
    BufferedReader br = new BufferedReader(new FileReader(inputFile));
    PrintWriter dw = new PrintWriter(dataFile);
    PrintWriter sw = new PrintWriter(statsFile);
    dw.printf("timestamp,blockId,txId,address,amount,scriptType,offset\n");
    sw.printf("blockId,queryTime,blockCount,resultCount\n");
    // Read the input file.
    long tqStart, tqEnd, lastBlockId = -1;
    String line;
    List<Record> currentOutputs = new ArrayList<>();
    while ((line = br.readLine()) != null) {
      // Split the line and obtain info, inputs and outputs.
      String[] parts = line.split(":"),
      infos = parts[0].split(","),
      outputs = parts[2].split(";");
      // Parse the associated information.
      long timestamp = Long.parseLong(infos[0]);
      long blockId = Long.parseLong(infos[1]);
      long txId = Long.parseLong(infos[2]);
      // Extract outputs from TX data.
      List<Record> parsedOutputs = new ArrayList<>();
      if (!parts[2].equals("")) {
        for (int offset = 0; offset < outputs.length; offset++) {
          String[] outputParts = outputs[offset].split(",");
          long address = Long.parseLong(outputParts[0]);
          long amount = Long.parseLong(outputParts[1]);
          int scriptType = Integer.parseInt(outputParts[2]);
          parsedOutputs.add(new Output(timestamp, blockId, txId, address,
          amount, scriptType, offset));
        }
      }
      // Check if block identifier has changed since last transaction.
      if (blockId != lastBlockId) {
        if (lastBlockId != -1) {
          // Fetc
          List<Record> result = new ArrayList<>();
          tqStart = System.nanoTime();
          for (Record r : currentOutputs) {
            Output or = (Output) r;
            if (lower <= or.amount && or.amount <= upper) result.add(r);
          }
          tqEnd = System.nanoTime();
          // Write all matching records to the output file.
          for (Record r : result) {
            Output or = (Output) r;
            dw.printf("%d,%d,%d,%d,%d,%d,%d\n", or.timestamp, or.blockId,
            or.txId, or.address, or.amount, or.scriptType, or.offset);
          }
          // Write statistics to the corresponding file.
          sw.printf("%d,%d,%d,%d\n", blockId, tqEnd-tqStart,
          currentOutputs.size(), result.size());
          // Reinitialize the current outputs container.
          currentOutputs = new ArrayList<>();
        }
        lastBlockId = blockId;
      }
      currentOutputs.addAll(parsedOutputs);
    }
    // Close input and output files.
    br.close();
    //dw.close();
    sw.close();
  }
}
