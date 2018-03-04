package com.example.kamlesh.frd;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class FragmentGameAnalysis extends Fragment {

    private int page;
    LineChart mChart;
    int []scoreArray = new int[7];
    float []timeArray = new float[7];
    float totalTime;

    // newInstance constructor for creating fragment with arguments
    /*public static FragmentGameAnalysis newInstance(int page) {
        FragmentGameAnalysis fragmentFirst = new FragmentGameAnalysis();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }*/

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        scoreArray = getArguments().getIntArray("scoreArray");
        totalTime = getArguments().getFloat("totalTime");
        timeArray = getArguments().getFloatArray("timeArray");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_analysis, container, false);

        mChart = (LineChart)view.findViewById(R.id.chart);
        int []location = new int[2];
        mChart.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        setData();
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        mChart.setTouchEnabled(true);
        mChart.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        mChart.setDrawBorders(false);
        mChart.setBorderColor(getResources().getColor(R.color.textColorSecondary));
        mChart.setBorderWidth(1f);
        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.setDrawGridBackground(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getDescription().setEnabled(false);
        //mChart.getDescription().setText("Question-Score Graph");
        //mChart.getDescription().setTextColor(getResources().getColor(R.color.colorPrimary));
        //mChart.getDescription().setPosition(x+2,y+2);
        mChart.getData().setHighlightEnabled(true);
        mChart.getLegend().setEnabled(false);
        mChart.setHighlightPerDragEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        YAxis yAxisLeft = mChart.getAxisLeft();
        yAxisLeft.setEnabled(true);
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setAxisMaximum(140);
        yAxisLeft.setGranularity(20);
        yAxisLeft.setTextColor(getResources().getColor(R.color.textColorPrimary));

        YAxis yAxisRight = mChart.getAxisRight();
        yAxisRight.setEnabled(true);
        yAxisRight.setAxisMinimum(0);
        yAxisRight.setAxisMaximum(totalTime+5);
        yAxisRight.setGranularity((totalTime+5)/7);
        yAxisRight.setTextColor(getResources().getColor(R.color.textColorPrimary));
        yAxisRight.setValueFormatter(new YAxisRightValueFormater());

        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setAxisMinimum(0.7f);
        xAxis.setAxisMaximum(7.3f);
        xAxis.setTextColor(getResources().getColor(R.color.textColorPrimary));


        System.out.println("scoreArray = "+scoreArray[0]+" "+scoreArray[1]+" "+scoreArray[2]+" "+scoreArray[3]+" "+scoreArray[4]+" "+scoreArray[5]+" "+scoreArray[6]);
        System.out.println("timeArray = "+timeArray[0]+" "+timeArray[1]+" "+timeArray[2]+" "+timeArray[3]+" "+timeArray[4]+" "+timeArray[5]+" "+timeArray[6]);

        return view;
    }

    private void setData() {
        //list for score graph
        List<Entry> vals1 = new ArrayList<Entry>();
        Entry vals1entry1 = new Entry(1, scoreArray[0]);
        vals1.add(vals1entry1);
        Entry vals1entry2 = new Entry(2,scoreArray[0]+scoreArray[1]);
        vals1.add(vals1entry2);
        Entry vals1entry3 = new Entry(3,scoreArray[0]+scoreArray[1]+scoreArray[2]);
        vals1.add(vals1entry3);
        Entry vals1entry4 = new Entry(4,scoreArray[0]+scoreArray[1]+scoreArray[2]+scoreArray[3]);
        vals1.add(vals1entry4);
        Entry vals1entry5 = new Entry(5,scoreArray[0]+scoreArray[1]+scoreArray[2]+scoreArray[3]+scoreArray[4]);
        vals1.add(vals1entry5);
        Entry vals1entry6 = new Entry(6,scoreArray[0]+scoreArray[1]+scoreArray[2]+scoreArray[3]+scoreArray[4]+scoreArray[5]);
        vals1.add(vals1entry6);
        Entry vals1entry7 = new Entry(7,scoreArray[0]+scoreArray[1]+scoreArray[2]+scoreArray[3]+scoreArray[4]+scoreArray[5]+scoreArray[6]);
        vals1.add(vals1entry7);

        //list for time graph
        List<Entry> vals2 = new ArrayList<Entry>();
        Entry vals2entry1 = new Entry(1, timeArray[0]);
        vals2.add(vals2entry1);
        Entry vals2entry2 = new Entry(2,timeArray[0]+timeArray[1]);
        vals2.add(vals2entry2);
        Entry vals2entry3 = new Entry(3,timeArray[0]+timeArray[1]+timeArray[2]);
        vals2.add(vals2entry3);
        Entry vals2entry4 = new Entry(4,timeArray[0]+timeArray[1]+timeArray[2]+timeArray[3]);
        vals2.add(vals2entry4);
        Entry vals2entry5 = new Entry(5,timeArray[0]+timeArray[1]+timeArray[2]+timeArray[3]+timeArray[4]);
        vals2.add(vals2entry5);
        Entry vals2entry6 = new Entry(6,timeArray[0]+timeArray[1]+timeArray[2]+timeArray[3]+timeArray[4]+timeArray[5]);
        vals2.add(vals2entry6);
        Entry vals2entry7 = new Entry(7,timeArray[0]+timeArray[1]+timeArray[2]+timeArray[3]+timeArray[4]+timeArray[5]+timeArray[6]);
        vals2.add(vals2entry7);

        LineDataSet set1 = new LineDataSet(vals1, "set1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(getResources().getColor(R.color.colorPrimary));
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(3f);
        set1.setCircleRadius(4f);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setValueTextColor(getResources().getColor(R.color.textColorPrimary));
        set1.setDrawFilled(false);
        set1.setValueFormatter(new MyGraph1ValueFormatter());

        LineDataSet set2 = new LineDataSet(vals2, "set2");
        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set2.setColor(getResources().getColor(R.color.colorAccent));
        set2.setCircleColor(Color.WHITE);
        set2.setLineWidth(3f);
        set2.setCircleRadius(4f);
        set2.setValueTextSize(9f);
        set2.setDrawFilled(true);
        set2.setValueTextColor(getResources().getColor(R.color.textColorSecondary));
        set2.setDrawFilled(false);
        set2.setValueFormatter(new MyGraph2ValueFormatter());

        List<ILineDataSet>dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);

        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.invalidate();
    }

    //to format int values
    public class MyGraph1ValueFormatter implements IValueFormatter {

        DecimalFormat mFormat;

        public MyGraph1ValueFormatter(){
            mFormat = new DecimalFormat("#.0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return "" + ((int)value);
        }
    }

    //to format one ecimal digit float values
    public class MyGraph2ValueFormatter implements IValueFormatter {

        DecimalFormat mFormat;

        public MyGraph2ValueFormatter(){
            mFormat = new DecimalFormat("##0.0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value);
        }
    }

    public class YAxisRightValueFormater implements IAxisValueFormatter {

        DecimalFormat mFormat;

        public YAxisRightValueFormater(){
            mFormat = new DecimalFormat("##0.0");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value);
        }
    }
}
