import java.util.ArrayList;
import java.util.List;

/**
 *  This class contains several methods for retrieving records from the
 *  Merkle interval tree and for verifying the results of a query.
 *  @author Matteo Loporchio
 */
public final class Query {

  /**
   *  Returns all records in the list whose key belongs to the given
   *  query interval.
   *  @param data the list of records
   *  @param query the query interval
   *  @return the list of all records whose key is in the given interval
   */
  public static List<Record> filter(List<Record> data, Interval q) {
    List<Record> result = new ArrayList<>();
    data.forEach((r) -> {
      long key = r.getKey();
      if (q.l <= key && key <= q.u) result.add(r);
    });
    return result;
  }

  /**
   *  Queries the Merkle interval tree to retrieve all records whose key
   *  belongs to the given interval.
   *  @param T the root of the Merkle interval tree
   *  @param q the query interval
   *  @return a VO for the root
   */
  public static VObject query(Node T, Interval q) {
    // If the node is a leaf, we construct a VO with all its records.
    if (T.isLeaf()) {
      return new VLeaf(((LeafNode) T).getData());
    }
    // Otherwise, we need to check if the node interval intersects the query.
    // If this is not the case, then the subtree will not contain any
    // matching record.
    if (!Geometry.intersects(T.getInterval(), q)) {
      return new VPruned(T.getInterval(), T.getHash());
    }
    // Otherwise, we need to explore recursively all subtrees rooted in
    // the current node.
    VContainer cont = new VContainer();
    ((IntNode) T).getChildren().forEach((n) -> {
      VObject partial = query(n, q);
      cont.append(partial);
    });
    return cont;
  }

  /**
   *  Verifies the correctness of a certain verification object by
   *  reconstructing the root of the Merkle interval tree.
   *  The output of this method is a <code>VResult</code> object
   *  that contains the result set, the bounding interval and
   *  the cryptographic digest of the root.
   *  @param vo the verification object
   *  @returns a <code>VResult</code> object
   */
  public static VResult verify(VObject vo) {
    // Reconstruct a leaf node.
    if (vo instanceof VLeaf) {
      List<Record> data = ((VLeaf) vo).getData();
      Interval range = Geometry.intervalFromRecords(data);
      byte[] digest = Hash.fromRecords(data);
      return new VResult(data, range, digest);
    }
    // Reconstruct a pruned internal node.
    if (vo instanceof VPruned) {
      VPruned pr = ((VPruned) vo);
      return new VResult(new ArrayList<>(), pr.getInterval(), pr.getHash());
    }
    // Otherwise we must reconstruct a non-pruned internal node.
    // This node is represented by means of a VO container.
    List<Record> data = new ArrayList<>();
    List<Interval> intervals = new ArrayList<>();
    List<byte[]> hashes = new ArrayList<>();
    // Obtain the VO container.
    VContainer cont = (VContainer) vo;
    // Recursively examine each VO in the container.
    for (int i = 0; i < cont.size(); i++) {
      VResult partial = verify(cont.get(i));
      // Take all the matching records and add them to the result set.
      data.addAll(partial.getData());
      // Collect all intervals and hashes.
      intervals.add(partial.getInterval());
      hashes.add(partial.getHash());
    }
    // Reconstruct the bounding interval and digest of the node.
    Interval range = Geometry.enlarge(intervals);
    byte[] hash = Hash.fromEntries(intervals, hashes);
    return new VResult(data, range, hash);
  }

  /**
   *  Utility method for extracting matching records directly from
   *  a verification object.
   *  @param vo the verification object
   *  @param q the query
   *  @return all records in the verification that match the given query
   */
  public static List<Record> filterVO(VObject vo, Interval q) {
    // Filter all records in a leaf.
    if (vo instanceof VLeaf) {
      List<Record> data = ((VLeaf) vo).getData();
      return filter(data, q);
    }
    // There is nothing to return for a pruned subtree.
    if (vo instanceof VPruned) return new ArrayList<Record>();
    // Recombine all partial results from explored subtree.
    List<Record> result = new ArrayList<>();
    VContainer cont = (VContainer) vo;
    for (int i = 0; i < cont.size(); i++) {
      result.addAll(filterVO(cont.get(i), q));
    }
    return result;
  }

}
