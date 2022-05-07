import java.io.*;
import java.util.*;

/**
 *  This program parses the entire Bitcoin data set and constructs a
 *  Merkle interval tree for the TX outputs of each block. The tree is
 *  then queried according to the desired range and the resulting verification
 *  object is checked. The class produces two different output files:
 *
 *  1. A CSV file containing information about all TX outputs that satisfy
 *  the query.
 *  2. A CSV file containing statistics about the construction, query and
 *  verification time for each tree.
 *
 *  @author Matteo Loporchio
 */
public class TestQuery {
  public static void main(String[] args) throws Exception {
    if (args.length < 6) {
      System.err.println("Usage: TestQuery <capacity> <inputFile> <dataFile> <statsFile> <lower> <upper>");
      System.exit(1);
    }
    // Read the input parameters.
    int c = Integer.parseInt(args[0]);
    String inputFile = args[1];
    String dataFile = args[2];
    String statsFile = args[3];
    long lower = Long.parseLong(args[3]);
    long upper = Long.parseLong(args[4]);
    Interval q = new Interval(lower, upper);
    // Open input and output files.
    BufferedReader br = new BufferedReader(new FileReader(inputFile));
    PrintWriter dw = new PrintWriter(dataFile);
    PrintWriter sw = new PrintWriter(statsFile);
    dw.printf("timestamp,blockId,txId,address,amount,scriptType,offset\n");
    sw.printf("blockId,constructionTime,queryTime,verificationTime,size,resultSize,min,max\n");
    // Read the input file.
    long tcStart, tcEnd, tqStart, tqEnd, tvStart, tvEnd, lastBlockId = -1;
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
          tcStart = System.nanoTime();
          Node t = Tree.buildPacked(currentOutputs, c);
          tcEnd = System.nanoTime();
          // Query the tree using the input interval.
          tqStart = System.nanoTime();
          VObject vo = Query.query(t, q);
          tqEnd = System.nanoTime();
          // Verify the result.
          tvStart = System.nanoTime();
          VResult vr = Query.verify(vo);
          tvEnd = System.nanoTime();
          // Write all matching records to the output file.
          List<Record> result = Query.filter(vr.getData(), q);
          for (Record r : result) {
            Output or = (Output) r;
            dw.printf("%d,%d,%d,%d,%d,%d,%d\n", or.timestamp, or.blockId,
            or.txId, or.address, or.amount, or.scriptType, or.offset);
          }
          // Write statistics to the corresponding file.
          sw.printf("%d,%d,%d,%d,%d,%d,%d,%d\n", blockId, tcEnd-tcStart,
          tqEnd-tqStart, tvEnd-tvStart, currentOutputs.size(), result.size(),
          t.getInterval().l, t.getInterval().u);
          // Reinitialize the current outputs container.
          currentOutputs = new ArrayList<>();
        }
        lastBlockId = blockId;
      }
      currentOutputs.addAll(parsedOutputs);
    }
    // Close input and output files.
    br.close();
    dw.close();
    sw.close();
  }
}
