import java.io.*;
import java.util.*;
import com.google.common.collect.Sets;

/**
 *  This program takes an address as its input and scans the entire Bitcoin
 *  data set. It prints the number of dust-creating TXs received by the address
 *  and the number of dust-creating TXs received from at least one of the
 *  known Satoshi Dice addresses.
 *
 *  @author Matteo Loporchio
 */
public class TestSatoshiDice {
  // Lower and upper bound for Bitcoin dust.
  public static final long lower = 1, upper = 545;

  // This is the set of all known Satoshi Dice addresses.
  public static final Set<Long> satoshiAddresses = Sets.newHashSet(
  3517234L,3517367L,3517636L,3521428L,3523985L,3524166L,3524243L,3524394L,
  3524641L,3525216L,3525822L,3525858L,3526385L,3526418L,3526568L,3532515L,
  3534212L,3534955L,3535501L,3541441L,3542581L,3545893L,3552405L,3557467L,
  3572642L,3577280L,4883003L,4888841L,4900246L,4912339L,4918513L,4926293L,
  4926403L,4932255L,5028176L,5028570L,5029468L,5068568L,5068570L,5071333L,
  6270487L,7157710L,7342535L,7380395L,7401482L,9350158L,9369649L,9390941L,
  9391525L,9391526L,9391527L,9396776L,9396794L,9403908L,9411717L,9414121L,
  9573898L,9582848L,9648536L,9681583L,9827561L,10039570L,10091266L,10316780L,
  10322115L,10823232L,10848112L,11008206L,11684998L,11853249L,12195141L,12220023L,
  12302115L,12302145L,12312639L,12560360L,12561484L,12902241L,12931352L,12988888L,
  13339340L,13445561L,13699715L,14097165L,14097490L,14268678L,14375706L,14388472L,
  14432332L,14473374L,14753254L,14788288L,14840056L,14909019L,14969473L,14973990L,
  15170997L,15171177L,15171178L,15171179L,15225669L,15263442L,15289717L,15489474L,
  15489553L,15489645L,15489646L,15489647L,15545838L,15580538L,15626202L,15626213L,
  15648499L,15734312L,15745120L,16101890L,16148601L,16149969L,16156238L,16172978L,
  16435621L,16497815L,17546160L,18298557L,19070315L,19091672L,19102437L,19613337L,
  19992060L,21062656L,21864252L,22999683L,24187956L,24187958L,24187964L,24188305L,
  24310059L,24310060L,24310061L,24310062L,25924872L,28019599L,28649412L,28702516L,
  30987126L,33298647L,36339063L,37303212L,37390639L,37605201L,37666690L,37797808L,
  37901746L,38034377L,38105871L,38143767L,38225938L,38235589L,38280597L,38339181L,
  38349312L,38349333L,38390168L,38628658L,38846528L,39021024L,39281399L,39307323L,
  39650783L,46144639L,48013739L,51504300L,54028253L,59159211L,59508018L,61942211L,
  61955513L,68110847L,77667799L,108817046L,111181690L,170101699L,170101774L,242984053L);

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: TestSatoshiDice <inputFile> <address>");
      System.exit(1);
    }
    String inputFile = args[0];
    long currentAddress = Long.parseLong(args[1]);
    long numReceived = 0, numReceivedFromSD = 0;
    // Open the input file.
    BufferedReader ir = new BufferedReader(new FileReader(inputFile));
    String line;
    while ((line = ir.readLine()) != null) {
      // Split the line and obtain info, inputs and outputs.
      String[] parts = line.split(":"),
      infos = parts[0].split(","),
      inputs = parts[1].split(";"),
      outputs = parts[2].split(";");
      // First, we examine the outputs and see if the address is among
      // the receivers of this transaction.
      boolean receiving = false;
      if (!parts[2].equals("")) {
        for (int offset = 0; offset < outputs.length; offset++) {
          String[] outputParts = outputs[offset].split(",");
          long address = Long.parseLong(outputParts[0]);
          long amount = Long.parseLong(outputParts[1]);
          // Check if there exists a dust output directed to the address.
          if (currentAddress == address && lower <= amount && amount <= upper) {
            receiving = true;
            numReceived++;
            break;
          }
        }
      }
      // Then, if there is at least one dust output directed to the current
      // address, we check the inputs and verify if one or more Satoshi Dice
      // addresses are sending money in this transaction.
      if (receiving && !parts[1].equals("")) {
        Set<Long> inputAddresses = new TreeSet<>();
        for (int offset = 0; offset < inputs.length; offset++) {
          String[] inputParts = inputs[offset].split(",");
          long address = Long.parseLong(inputParts[0]);
          inputAddresses.add(address);
        }
        // Check if the sending addresses contain at least one Satoshi Dice
        // address.
        Set<Long> common = Sets.intersection(inputAddresses, satoshiAddresses);
        if (!common.isEmpty()) numReceivedFromSD++;
      }
    }
    // Close the input file.
    ir.close();
    // Print the results on stdout.
    System.out.printf("Address:\t\t%d\nReceived TXs:\t\t%d\nReceived TXs from SD:\t\t%d\n",
    currentAddress, numReceived, numReceivedFromSD);
  }
}
