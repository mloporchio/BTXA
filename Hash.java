import java.util.List;
import com.google.common.hash.*;

/**
 *  This class contains a set of methods for generating and handling
 *  cryptographic digests.
 *  @author Matteo Loporchio
 */
public final class Hash {
  /**
   *  We choose SHA-256 as our cryptographic hashing algorithm.
   */
  public static final HashFunction hf = Hashing.sha256();

  /**
   *  Computes the cryptographic digest of a list of Merkle interval
   *  tree nodes by concatenating their binary representations.
   *  @param nodes the list of nodes
   *  @return the cryptographic hash of the nodes in the list
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
   *  Computes the cryptographic digest of a list of record by concatenating
   *  their binary representations.
   *  @param data the list of records
   *  @return the cryptographic hash of the records in the list
   */
  public static byte[] fromRecords(List<Record> data) {
    // Create new hasher instance.
    Hasher hasher = hf.newHasher();
    // Iterate over the list and collect the representations of the records.
    for (Record r : data) hasher.putBytes(r.asBytes());
    // Return the hash of the concatenation.
    return hasher.hash().asBytes();
  }

  /**
   *  Computes the cryptographic digest of a tree node by concatenating
   *  the intervals and digests of its subtrees.
   *  @param ranges the list of intervals
   *  @param digests the list of cryptographic hashes
   *  @return the cryptographic hash of the node
   */
  public static byte[] fromEntries(List<Interval> ranges, List<byte[]> digests) {
    // Check if the lengths of the input lists coincide.
    if (ranges.size() != digests.size())
      throw new IllegalArgumentException("Input lists with different length!");
    // Create new hasher instance.
    Hasher hasher = hf.newHasher();
    // Iterate over the lists and collect intervals and node digests.
    for (int i = 0; i < ranges.size(); i++) {
      Interval range = ranges.get(i);
      byte[] digest = digests.get(i);
      hasher.putLong(range.l).putLong(range.u).putBytes(digest);
    }
    // Return the hash of the concatenation.
    return hasher.hash().asBytes();
  }

  /**
   *  Returns a human-readable hexadecimal string representing
   *  the bytes of the cryptographic digest.
   *  @param hash the cryptographic digest
   *  @return a hexadecimal string representing the digest
   */
  public static String toString(byte[] hash) {
    return HashCode.fromBytes(hash).toString();
  }

}
