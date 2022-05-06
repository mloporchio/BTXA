import java.util.List;

/**
 *  @author Matteo Loporchio
 */
public final class Geometry {
  /**
   *  The empty interval, represented as (+inf, -inf).
   */
  public static final Interval EMPTY_INTERVAL =
  new Interval(Long.MAX_VALUE, Long.MIN_VALUE);

  /**
   *  Returns true if and only if a given value is inside
   *  the specified interval.
   *  @param i the interval
   *  @param x a value
   *  @return true if x is in the interval, false otherwise
   */
   public static boolean contains(Interval i, long x) {
     return (i.l <= x && x <= i.u);
   }

  /**
   *  Returns true if and only if the interval overlaps with the current one.
   *  @param i the other interval
   *  @return true if r1 overlaps with r2, false otherwise
   */
  public static boolean intersects(Interval i, Interval j) {
    return (i.l <= j.u) && (j.l <= i.u);
  }

  /**
   *  Computes the minimum bounding union between two intervals.
   *  @param i first interval
   *  @param j second interval
   *  @return the minimum bounding union of the two intervals
   */
  public static Interval enlarge(Interval i, Interval j) {
    return new Interval(Math.min(i.l, j.l), Math.max(i.u, j.u));
  }


  /**
   *  Computes the minimum bounding union of a list of intervals.
   *  @param intervals the list of intervals
   *  @return the minimum bounding union of the intervals in the list
   */
  public static Interval enlarge(List<Interval> intervals) {
    long l = Long.MAX_VALUE, u = Long.MIN_VALUE;
    for (Interval i : intervals) {
      if (i.l <= l) l = i.l;
      if (i.u >= u) u = i.u;
    }
    return new Interval(l, u);
  }

  /**
   *  Computes the minimum bounding interval for a set of tree nodes.
   *  @param values list of values
   *  @return the minimum bounding interval of all values
   */
  public static Interval intervalFromNodes(List<Node> nodes) {
    long l = Long.MAX_VALUE, u = Long.MIN_VALUE;
    for (Node n : nodes) {
      Interval i = n.getInterval();
      if (i.l <= l) l = i.l;
      if (i.u >= u) u = i.u;
    }
    return new Interval(l, u);
  }

  /**
   *  Computes the minimum bounding interval for a set of values.
   *  @param values list of values
   *  @return the minimum bounding interval of all values
   */
  public static Interval intervalFromRecords(List<Record> data) {
    long l = Long.MAX_VALUE, u = Long.MIN_VALUE;
    for (Record r : data) {
      long k = r.getKey();
      if (k <= l) l = k;
      if (k >= u) u = k;
    }
    return new Interval(l, u);
  }

}
