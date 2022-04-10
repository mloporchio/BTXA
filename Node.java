/**
 *  This class represents a generic node of the Merkle interval tree.
 *  For each node we keep track of the minimum bounding interval
 *	of all records in the subtree.
 *	@author Matteo Loporchio
 */
public abstract class Node {
	/**
	 *
	 */
	private final Interval i;

	/**
	 *	
	 */
	public Node(Interval i) {
		this.i = i;
	}

	/**
	 *
	 */
	public Interval getInterval() {
		return i;
	}

	/**
	 *	This method returns true if the current node
	 *	is a leaf node and false if it is an internal one.
	 *	@return true if the node is a leaf, false otherwise
	 */
	public abstract boolean isLeaf();
}
