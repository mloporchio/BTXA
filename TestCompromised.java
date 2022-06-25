import java.io.*;
import java.util.*;

/**
 *  This program analyzes the entire Bitcoin data set and extracts
 *  all transactions where at least one output is directed to
 *  the compromised address 1JwSSubhmg6iPtRjtyqhUYYH7bZg3Lfy1T.
 *  For each transaction, it writes all outpus associated with the address
 *  on a file and all sending address on another dedicated file.
 *
 *  (Source: https://privatekeys.pw/address/bitcoin/1JwSSubhmg6iPtRjtyqhUYYH7bZg3Lfy1T)  
 *  @author Matteo Loporchio
 */
public class TestCompromised {
  // Lower and upper bounds for dust query range.
  public static final long lower = 1, upper = 545;
  // The compromised address (i.e., 1JwSSubhmg6iPtRjtyqhUYYH7bZg3Lfy1T).
  public static final long targetAddress = 3108769;

  public static void main(String[] args) throws Exception {
    if (args.length < 3) {
      System.err.println("Usage: TestCompromised <inputFile> <outputsFile> <sendersFile>");
      System.exit(1);
    }
    // Read the input parameters.
    String inputFile = args[0],
    outputsFile = args[1],
    sendersFile = args[2];
    // Open the input and output files.
    BufferedReader ir = new BufferedReader(new FileReader(inputFile));
    PrintWriter ow = new PrintWriter(outputsFile);
    PrintWriter sw = new PrintWriter(sendersFile);
    ow.println("timestamp,blockId,txId,address,amount,scriptType,offset");
    sw.println("timestamp,txId,sender,amount");
    String line;
    while ((line = ir.readLine()) != null) {
      // Split the line and obtain info, inputs and outputs.
      String[] parts = line.split(":"),
      infos = parts[0].split(","),
      inputs = parts[1].split(";"),
      outputs = parts[2].split(";");
      // Parse the associated information.
      long timestamp = Long.parseLong(infos[0]);
      long blockId = Long.parseLong(infos[1]);
      long txId = Long.parseLong(infos[2]);
      // Parse the TX outputs and check if there is any output directed
      // to the target address.
      boolean check = false;
      if (!parts[2].equals("")) {
        for (int offset = 0; offset < outputs.length; offset++) {
          String[] outputParts = outputs[offset].split(",");
          long address = Long.parseLong(outputParts[0]);
          long amount = Long.parseLong(outputParts[1]);
          int scriptType = Integer.parseInt(outputParts[2]);
          // If the output address coincides with the target address,
          // then write the output tuple to the outputs file.
          if (address == targetAddress) {
            check = true;
            ow.printf("%d,%d,%d,%d,%d,%d,%d\n",
            timestamp, blockId, txId, address, amount, scriptType, offset);
          }
        }
      }
      // If the target address is among the output addresses,
      // then we collect all sending addresses (if any) and write them on a file.
      if (check && !parts[1].equals("")) {
        for (int i = 0; i < inputs.length; i++) {
          String[] inputParts = inputs[i].split(",");
          long senderAddress = Long.parseLong(inputParts[0]),
          inputAmount = Long.parseLong(inputParts[1]);
          sw.printf("%d,%d,%d,%d\n", timestamp, txId, senderAddress, inputAmount);
        }
      }
    }
    // Close input and output files.
    ir.close();
    ow.close();
    sw.close();
  }
}
