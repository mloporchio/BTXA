/**
 *  This abstract class represents a generic node of the Merkle interval tree.
 *  For each node we keep track of the minimum bounding interval
 *	of all records in the subtree and the corresponding cryptographic digest.
 *	@author Matteo Loporchio
 */
public abstract class Node {
	/**
	 *	The interval associated with the node.
	 */
	private final Interval range;

	/**
	 *	The cryptographic digest associated with the node.
	 */
	private final byte[] hash;

	/**
	 *	Class constructor.
	 *	@param range the interval associated with the node
	 *	@param hash the cryptographic digest associated with the node
	 */
	public Node(Interval range, byte[] hash) {
		this.range = range;
		this.hash = hash;
	}

	/**
	 *	Returns the interval associated with the node.
	 *	@return the interval associated with the node
	 */
	public Interval getInterval() {
		return range;
	}

	/**
	 *	Returns the cryptographic digest associated with the node.
	 *	@return the cryptographic digest associated with the node
	 */
	public byte[] getHash() {
		return hash;
	}

	/**
	 *	Returns true if the current node is a leaf
	 *	and false if it is an internal one.
	 *	@return true if the node is a leaf, false otherwise
	 */
	public abstract boolean isLeaf();
}
