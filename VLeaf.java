import java.util.ArrayList;
import java.util.List;

/**
 *  This class represents the verification object generated during
 *  the visit of a leaf node.
 *  @author Matteo Loporchio
 */
public class VLeaf implements VObject {
  
  /**
   *  The set of records included in the verification object.
   */
  private List<Record> data;

  /**
   *  Constructs a new leaf verification object from a given set of records.
   *  @param records the list of records
   */
  public VLeaf(List<Record> data) {
    this.data = new ArrayList<>();
    this.data.addAll(data);
  }

  /**
   *  Returns the list of records associated to the verification object.
   *  @return the list of points of the verification object
   */
  public List<Record> getData() {
    return data;
  }
}
