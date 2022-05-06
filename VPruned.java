/**
 *  This verification object is generated during the visit of a
 *  pruned internal node.
 *  @author Matteo Loporchio
 */
public class VPruned implements VObject {
  
  /**
   *  The bounding interval of the pruned node.
   */
  private Interval range;

  /**
   *  The cryptographic digest of the pruned node.
   */
  private byte[] hash;

  /**
   *  Constructs a new pruned verification object.
   *  @param range the bounding interval of the pruned node
   *  @param hash the digest of the pruned node
   */
  public VPruned(Interval range, byte[] hash) {
    this.range = range;
    this.hash = hash;
  }

  /**
   *  Returns the interval associated with the pruned verification object.
   *  @return the interval of the verification object
   */
  public Interval getInterval() {
    return range;
  }

  /**
   *  Returns the digest associated with the pruned verification object.
   *  @return the hash value of the pruned verification object
   */
  public byte[] getHash() {
    return hash;
  }
}
