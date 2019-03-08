package fr.ocr.utiles;


public class QuickSort<T extends Number> implements Comparable<Number> {

    public QuickSort(T[] ts) {

        triRapideGenerics(ts, 0, ts.length - 1);
    }

    private int fctPartition(Number[] ts, int imin, int imax) {
        int i = imin;
        int j = imax - 1;

        Number v = ts[imax];

        while (i <= j) {
            if (v instanceof Integer) {
                while ((i < imax) && (ts[i].intValue() <= v.intValue())) i++;
                while ((j >= imin) && (ts[j].intValue() >= v.intValue())) j--;
            } else if (v instanceof Double) {
                while ((i < imax) && (ts[i].doubleValue() <= v.doubleValue())) i++;
                while ((j >= imin) && (ts[j].doubleValue() >= v.doubleValue())) j--;
            } else if (v instanceof Float) {
                while ((i < imax) && (ts[i].floatValue() <= v.floatValue())) i++;
                while ((j >= imin) && (ts[j].floatValue() >= v.floatValue())) j--;
            } else if (v instanceof Long) {
                while ((i < imax) && (ts[i].longValue() <= v.longValue())) i++;
                while ((j >= imin) && (ts[j].longValue() >= v.longValue())) j--;

            } else
                throw new UnsupportedOperationException("accepte Integer, Long, Float,Double uniquement");

            if (i < j) {
                Number temp = ts[i];
                ts[i] = ts[j];
                ts[j] = temp;
            }
        }
        ts[imax] = ts[i];
        ts[i] = v;
        return i;
    }

    @Override
    public int compareTo(Number o) {

        return 0;
    }

    private void triRapideGenerics(Number[] T, int imin, int imax) {
        int i;
        try {
            if (imin < imax) {
                i = fctPartition(T, imin, imax);
                triRapideGenerics(T, imin, i - 1);
                triRapideGenerics(T, i + 1, imax);
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | UnsupportedOperationException e) {
            System.out.println(e.getLocalizedMessage());
            throw e;
        }
    }

}
