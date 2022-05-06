import java.io.*;
import java.util.*;

public class TestQuery {
  public static void main(String[] args) throws Exception {
    if (args.length < 5) {
      System.err.println("Usage: TestQuery <capacity> <inputFile> <outputFile> <lower> <upper>");
      System.exit(1);
    }
    // Read the input parameters.
    int c = Integer.parseInt(args[0]);
    String inputFile = args[1];
    String outputFile = args[2];
    long lower = Long.parseLong(args[3]);
    long upper = Long.parseLong(args[4]);
    Interval q = new Interval(lower, upper);
    // Open input and output files.
    BufferedReader br = new BufferedReader(new FileReader(inputFile));
    PrintWriter ow = new PrintWriter(outputFile);
    ow.printf("timestamp,blockId,txId,address,amount,scriptType,offset\n");
    // Read the input file.
    long lastBlockId = -1;
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
          // Construct the MI-tree for the current block.
          Node t = Tree.buildPacked(currentOutputs, c);
          // Query the tree using the input interval.
          VObject vo = Query.query(t, q);
          // Skip reconstruction and extract matching records directly
          // from the verification object.
          List<Record> result = Query.filterVO(vo, q);
          // Write all matching records to the output file.
          for (Record r : result) {
            Output or = (Output) r;
            ow.printf("%d,%d,%d,%d,%d,%d,%d\n", or.timestamp, or.blockId,
            or.txId, or.address, or.amount, or.scriptType, or.offset);
          }
          // Reinitialize the current outputs container.
          currentOutputs = new ArrayList<>();
        }
        lastBlockId = blockId;
      }
      currentOutputs.addAll(parsedOutputs);
    }
    // Close input and output files.
    br.close();
    ow.close();
  }
}
