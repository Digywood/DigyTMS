package com.digywood.tms.Charts;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter implements IAxisValueFormatter
{

    protected String[] mMonths = new String[]{
            "Min", "Avg", "Max"
    };

    private BarLineChartBase<?> chart;

    public DayAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        int month = (int) value;
        if(month>0){
            month=month-1;
        }
        String monthName = mMonths[month % mMonths.length];
        return monthName;
    }
}
