import java.util.List;

/**
 *  This class represents the output of the verification algorithm.
 *  It contains the result set of the query, the bounding interval
 *  and digest of the MI-tree root, all reconstructed from
 *  the verification object.
 *  @author Matteo Loporchio
 */
public class VResult {

  /**
   *  The result set of the query.
   */
  private List<Record> data;

  /**
   *  The minimum bounding rectangle of the reconstructed root node.
   */
  private Interval range;

  /**
   *  The hash value of the reconstructed root node.
   */
  private byte[] hash;

  /**
   *  Constructs an empty <code>VResult</code> object.
   */
  public VResult() {
    this.data = null;
    this.range = null;
    this.hash = null;
  }

  /**
   *  Default constructor for the <code>VResult</code> class.
   *  @param data the result set
   *  @param range the bounding interval of the root
   *  @param hash the root digest
   */
  public VResult(List<Record> data, Interval range, byte[] hash) {
    this.data = data;
    this.range = range;
    this.hash = hash;
  }

  /**
  * Returns the reconstructed result set.
  * @return the reconstructed result set
  */
  public List<Record> getData() {
    return data;
  }

  /**
  * Returns the reconstructed bounding interval for the MI-tree root.
  * @return the minimum bounding rectangle for the reconstructed root
  */
  public Interval getInterval() {
    return range;
  }

  /**
  * Returns the reconstructed digest for the MR-tree root.
  * @return the hash value of the reconstructed MR-tree root node
  */
  public byte[] getHash() {
    return hash;
  }
}
