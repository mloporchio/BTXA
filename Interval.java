import java.util.List;

/**
 *  This class represents a 1D range with integer-valued bounds.
 *  @author Matteo Loporchio
 */
public class Interval {
  /**
   *  Lower bound of the interval.
   */
  public final long l;

  /**
   *  Upper bound of the interval.
   */
  public final long u;

  /**
   *  Constructs a new interval with the given bounds.
   *  @param l lower bound
   *  @param u upper bound
   */
  public Interval(long l, long u) {
    this.l = l;
    this.u = u;
  }

  /**
   *  Returns a string representing the current interval.
   *  @return a string representing the interval
   */
  public String toString() {
    return String.format("[%d, %d]", l, u);
  }
}
