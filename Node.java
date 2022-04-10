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
	private final Interval range;

	/**
	 *	The cryptographic digest associated with the node.
	 */
	private final byte[] hash;

	/**
	 *
	 */
	public Node(Interval range, byte[] hash) {
		this.range = range;
		this.hash = hash;
	}

	/**
	 *
	 */
	public Interval getInterval() {
		return range;
	}

	/**
	 *	
	 */
	public byte[] getHash() {
		return hash;
	}

	/**
	 *	This method returns true if the current node
	 *	is a leaf node and false if it is an internal one.
	 *	@return true if the node is a leaf, false otherwise
	 */
	public abstract boolean isLeaf();
}
