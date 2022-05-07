import java.util.ArrayList;
import java.util.List;

/**
 *  This class contains utility methods for performing various tasks.
 *  @author Matteo Loporchio
 */
public final class Utility {

  /**
  * This function chops a list into sublists of a given length.
  * @param l the list to be divided
  * @param k the number of elements in each sublist
  * @return a list containing all the sublists
  */
  public static <T> List<List<T>> split(List<T> l, int k) {
    List<List<T>> parts = new ArrayList<List<T>>();
    for (int i = 0; i < l.size(); i += k) {
      parts.add(l.subList(i, Math.min(l.size(), i + k)));
    }
    return parts;
  }
}
