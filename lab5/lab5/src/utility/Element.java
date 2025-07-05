package utility;

import java.io.Serializable;

/**
 * Абстрактный класс для элементов коллекции.
 */
public abstract class Element implements Comparable<Element>, Validatable, Serializable {
    public abstract long getId();

    @Override
    public int compareTo(Element element) {
        return Long.compare(this.getId(), element.getId());
    }
}
