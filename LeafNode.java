import java.util.List;

/**
 *  @author Matteo Loporchio
 */
public class LeafNode extends Node {
  /**
   *  A leaf node stores a list of records.
   */
  private final List<Record> data;

  /**
   *  Constructs a new leaf node from a list of records.
   *  @param data a list of records to be stored in the node
   */
  public LeafNode(List<Record> data) {
    super(Geometry.intervalFromRecords(data), Hash.fromRecords(data));
    this.data = data;
  }

  /**
   *  Returns the list of records of the leaf node.
   *  @return the list of records associated with the node
   */
  public List<Record> getData() {
    return data;
  }

  /**
   *	This method returns true if the current node
   *	is a leaf node and false if it is an internal one.
   *	@return true if the node is a leaf, false otherwise
   */
  public boolean isLeaf() {
    return true;
  }
}
