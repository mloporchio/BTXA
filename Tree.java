import java.util.ArrayList;
import java.util.List;

/**
 *  This class contains several methods for constructing Merkle interval
 *  trees and for analyzing their properties.
 *  @author Matteo Loporchio
 */
public final class Tree {
  /**
   *  This method constructs an MI-tree from a given list of records
   *  using the packed algorithm. The nodes of the tree have a fixed capacity.
   *  @param data list of data records
   *  @param c page capacity
   *  @return the tree root
   */
  public static Node buildPacked(List<Record> data, int c) {
    List<Node> current = new ArrayList<Node>();
    // Sort the records in ascending order.
    data.sort((Record r, Record s) -> Long.compare(r.getKey(), s.getKey()));
    // Split the records into chunks of size c.
    // Then create a leaf node for each chunk.
    List<List<Record>> leaves = Utility.split(data, c);
    for (List<Record> ck : leaves) current.add(new LeafNode(ck));
    // Start merging.
    while (current.size() > 1) {
      // Divide the list of current nodes into chunks.
      List<List<Node>> chunks = Utility.split(current, c);
      // For each chunk, merge all its nodes and create a new one.
      List<Node> merged = new ArrayList<Node>();
      for (List<Node> ck : chunks) merged.add(new IntNode(ck));
      // These nodes become the new working set.
      current = merged;
    }
    // Return the root of the tree, i.e. the only node left.
    return current.get(0);
  }

  /**
   *  Computes the number of leaves of the given Merkle interval tree.
   *  @param t the tree root
   *  @return the number of leaves of the tree
   */
  public static int leaves(Node t) {
    if (t.isLeaf()) return 1;
    int count = 0;
    List<Node> children = ((IntNode) t).getChildren();
    for (Node child : children) count += leaves(child);
    return count;
  }

  /**
   *  Computes the height of the given Merkle interval tree.
   *  @param t the tree root
   *  @return the tree height
   */
  public static int height(Node t) {
    // If the node is a leaf, there is nothing to compute.
    if (t.isLeaf()) return 0;
    int hmax = -1;
    List<Node> children = ((IntNode) t).getChildren();
    for (Node child : children) hmax = Math.max(hmax, height(child));
    return 1 + hmax;
  }
}
