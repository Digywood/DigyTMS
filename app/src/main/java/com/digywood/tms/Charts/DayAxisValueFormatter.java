package com.digywood.tms.Charts;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter implements IAxisValueFormatter
{

    int i=0;

    protected String[] mMonths = new String[]{
            "Min", "Avg", "Max"
    };

    private BarLineChartBase<?> chart;

    public DayAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        String monthName = mMonths[i % mMonths.length];
        i++;
        return monthName;
    }
}
