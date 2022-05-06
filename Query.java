import java.util.ArrayList;
import java.util.List;

/**
 *
 *  @author Matteo Loporchio
 */
public final class Query {

  /**
   *  Returns all records in the list whose key belongs to the given
   *  query interval.
   *  @param data the list of records
   *  @param query the query interval
   *  @return
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
   *  This method can be used to query the MI-tree to retrieve all records
   *  whose key falls in the given interval.
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
   *
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

  /**
   *
   */
  // public static VResult verify(VObject vo) {
  //   // Reconstruct a leaf node.
  //   if (vo instanceof VLeaf) {
  //     List<Point> records = ((VLeaf) vo).getRecords();
  //     Rectangle MBR = Geometry.MBR(records);
  //     byte[] h = Hash.hashPoints(records);
  //     return new VResult(records, MBR, h);
  //   }
  //   // Reconstruct a pruned internal node.
  //   if (vo instanceof VPruned) {
  //     VPruned pr = ((VPruned) vo);
  //     return new VResult(new ArrayList<>(), pr.getInterval(), pr.getHash());
  //   }
  //   // Otherwise we must reconstruct a non-pruned internal node.
  //   // This node is represented by means of a VO container.
  //   List<Point> records = new ArrayList<Point>();
  //   List<Rectangle> rects = new ArrayList<Rectangle>();
  //   List<byte[]> hashes = new ArrayList<byte[]>();
  //   // Obtain the VO container.
  //   VContainer cont = (VContainer) vo;
  //   // Recursively examine each VO in the container.
  //   for (int i = 0; i < cont.size(); i++) {
  //     VResult partial = verify(cont.get(i));
  //     // Take all the matching records and add them to the result set.
  //     records.addAll(partial.getContent());
  //     // Collect all rectangles and hashes.
  //     rects.add(partial.getMBR());
  //     hashes.add(partial.getHash());
  //   }
  //   //
  //   Rectangle u = Geometry.enlarge(rects);
  //   byte[] hash = Hash.reconstruct(rects, hashes);
  //   return new VResult(records, u, hash);
  // }
}
