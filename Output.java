import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

/**
 *  This class contains information about a single Bitcoin TX output.
 *  It implements the Record interface and uses the amount as query key.
 *  @author Matteo Loporchio
 */
public class Output implements Record {
  public final long timestamp;
  public final long blockId;
  public final long txId;
  public final long address;
  public final long amount;
  public final int scriptType;
  public final int offset;

  /**
   *  Class constructor.
   *  @param timestamp the timestamp of the transaction this output belongs to
   *  @param txId the transaction identifier
   *  @param blockId
   *  @param address
   *  @param amount
   *  @param scriptType
   *  @param offset
   */
  public Output(long timestamp, long txId, long blockId, long address,
  long amount, int scriptType, int offset) {
    this.timestamp = timestamp;
    this.blockId = blockId;
    this.txId = txId;
    this.address = address;
    this.amount = amount;
    this.scriptType = scriptType;
    this.offset = offset;
  }

  /**
   *  This method returns the key associated with the record.
   *  @return the key associated with the record
   */
  public long getKey() {
    return amount;
  }

  /**
   *  Returns a representation of the current record as an array of bytes.
   *  @return an array of bytes representing the record
   */
  public byte[] asBytes() {
    return Bytes.concat(
      Longs.toByteArray(timestamp),
      Longs.toByteArray(blockId),
      Longs.toByteArray(txId),
      Longs.toByteArray(address),
      Longs.toByteArray(amount),
      Ints.toByteArray(scriptType),
      Ints.toByteArray(offset)
    );
  }
}
