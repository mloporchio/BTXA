import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.google.common.primitives.Longs;

public class Test {
  public static void main(String[] args) {
    int n = 100;
    int c = 20;
    // Generate n random records.
    Random rd = new Random();
    byte[] valueBytes = new byte[Long.SIZE];
    List<Record> records = new ArrayList<Record>();
    for (int i = 0; i < n; i++) {
      rd.nextBytes(valueBytes);
      long value = Longs.fromByteArray(valueBytes);
      records.add(new TestRecord(value));
    }
    // Build the tree and print the root hash.
    Node root = Tree.buildPacked(records, c);
    System.out.printf("Interval: %s\nHash: %s\n",
    root.getInterval().toString(), Hash.toString(root.getHash()));
  }
}

/**
 *  A simple test class implementing the Record interface.
 */
class TestRecord implements Record {
  private final long key;
  public TestRecord(long key) {this.key = key;}
  public long getKey() {return key;}
  public byte[] asBytes() {return Longs.toByteArray(key);}
}
