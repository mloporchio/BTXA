import java.util.List;

/**
 *  This class represents an internal node of the tree.
 *  @author Matteo Loporchio
 */
public class IntNode extends Node {
  /**
   *  An internal node stores a list of references to its children.
   */
  private List<Node> children;

  /**
   *  Constructs a new internal node.
   *  @param range t
   *  @param children a list of children for the internal node
   */
  public IntNode(List<Node> children) {
    super(Geometry.intervalFromNodes(children), Hash.fromNodes(children));
    this.children = children;
  }

  /**
   *  Returns the list of children of the node.
   *  @return the list of children
   */
  public List<Node> getChildren() {
    return children;
  }

  /**
	 *	This method returns true if the current node
	 *	is a leaf node and false if it is an internal one.
	 *	@return true if the node is a leaf, false otherwise
	 */
  public boolean isLeaf() {
    return false;
  }
}
