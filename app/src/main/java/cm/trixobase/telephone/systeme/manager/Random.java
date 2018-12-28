package cm.trixobase.telephone.systeme.manager;

import java.util.Calendar;

/**
 * Created by noumianguebissie on 10/8/18.
 */

public class Random<T> {

    T[] elements;

    public Random(T... elements) {
        this.elements = elements;
    }

    public T build() {
        return elements[getInt(elements.length)];
    }

    private int getInt(int size) {
        int value = 0;
        int t = (Calendar.getInstance().get(Calendar.MILLISECOND));
        for (int i=0 ; i<t ; i++) {
            value++;
            if (value == size)
                value = 0;
        }
        return value;
    }

}
