package com.example.test.dxworkspace.presentation.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

public class VariantChartFormat implements IAxisValueFormatter {

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    public static long easyunitVariant = 1;
    private static float[] easyunitDecimal = {1f, 1000f, 1000000f, 1000000000f};

    static {
        suffixes.put(1000L, "k");
        suffixes.put(1000000L, "M");
        suffixes.put(1000000000L, "B");
        suffixes.put(1000000000000L, "T");
        suffixes.put(1000000000000000L, "P");
        suffixes.put(1000000000000000000L, "E");
    }

    private DecimalFormat mFormat;

    public VariantChartFormat() {
        mFormat = new DecimalFormat("0.###");
    }

    private String getFormattedValue(long value) {
        // Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment heresetValueFormatter
        if (value == Long.MIN_VALUE) {
            return getFormattedValue(Long.MIN_VALUE + 1);
        }
        if (value < 0) {
            return "-" + getFormattedValue(-value);
        }
        if (value < 1000) {
            return Long.toString(value); // deal with easy case
        }

        Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); // the number part of the
        // output times 10
        boolean hasDecimal = truncated < 100
                && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10)
                + suffix;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return getFormattedValue((long) value);
    }

    private String subStringNumber(String number) {
        if (number.endsWith(".0")) {
            number = number.substring(0, number.length() - 2);
        }
        if (number.endsWith(".00")) {
            number = number.substring(0, number.length() - 3);
        }
        if (number.endsWith(".000")) {
            number = number.substring(0, number.length() - 4);
        }
        return number;
    }
}