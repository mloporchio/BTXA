/**
 *  This interface represents a generic record that can be indexed
 *  by a Merkle interval tree.
 *  @author Matteo Loporchio
 */
public interface Record {
  /**
   *  This method returns the key associated with the record.
   *  @return the key associated with the record
   */
  long getKey();

  /**
   *  Returns a representation of the current record as an array of bytes.
   *  @return an array of bytes representing the record
   */
  byte[] asBytes();
}
