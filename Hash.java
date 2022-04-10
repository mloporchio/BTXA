import java.util.List;
import com.google.common.hash.*;

/**
 *  @author Matteo Loporchio
 */
public final class Hash {

  /**
   *
   */
  public static final HashFunction hf = Hashing.sha256();

  /**
   *
   */
  public static byte[] fromNodes(List<Node> nodes) {
    // Create new hasher instance.
    Hasher hasher = hf.newHasher();
    // Iterate over the list and collect intervals and node digests.
    for (Node n : nodes) {
      Interval range = n.getInterval();
      byte[] digest = n.getHash();
      hasher.putLong(range.l).putLong(range.u).putBytes(digest);
    }
    // Return the hash of the concatenation.
    return hasher.hash().asBytes();
  }

  /**
   *
   */
  public static byte[] fromRecords(List<Record> records) {
    // Create new hasher instance.
    Hasher hasher = hf.newHasher();
    // Iterate over the list and collect the representations of the records.
    for (Record r : records) hasher.putBytes(r.asBytes());
    // Return the hash of the concatenation.
    return hasher.hash().asBytes();
  }

  /**
   *
   */
  public static String toString(byte[] hash) {
    return HashCode.fromBytes(hash).toString();
  }

}
