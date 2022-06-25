import java.io.*;
import java.util.*;

/**
 *  This program reads the entire Bitcoin data set and creates a new file
 *  containing general information about its transactions.
 *  For each transaction, we extract the following information:
 *
 *  - The transaction identifier
 *  - The block identifier
 *  - The number of inputs
 *  - The number of outputs
 *
 *  @author Matteo Loporchio
 */
public class TestStats {
  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: TestStats <inputFile> <outputFile>");
      System.exit(1);
    }
    String inputFile = args[0],
    outputFile = args[1];
    BufferedReader ir = new BufferedReader(new FileReader(inputFile));
    PrintWriter ow = new PrintWriter(outputFile);
    ow.println("txId,blockId,inputCount,outputCount");
    String line;
    while ((line = ir.readLine()) != null) {
      // Split the line and obtain info and inputs.
      String[] parts = line.split(":"),
      infos = parts[0].split(","),
      inputs = parts[1].split(";"),
      outputs = parts[2].split(";");
      // Parse the associated information.
      long blockId = Long.parseLong(infos[1]);
      long txId = Long.parseLong(infos[2]);
      // Determine the number of inputs and outputs.
      int numInputs = 0, numOutputs = 0;
      if (!parts[1].equals("")) numInputs = inputs.length;
      if (!parts[2].equals("")) numOutputs = outputs.length;
      // Write statistics to the output file.
      ow.printf("%d,%d,%d,%d\n", txId, blockId, numInputs, numOutputs);
    }
    // Close input and output files.
    ir.close();
    ow.close();
  }
}
