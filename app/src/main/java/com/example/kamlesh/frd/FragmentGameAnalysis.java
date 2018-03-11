package com.example.kamlesh.frd;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class FragmentGameAnalysis extends Fragment {

    private int page;
    LineChart chartScore, chartTime;
    RadioGroup toggle;
    RadioButton toggleScore, toggleTime;
    TextView textView9, textView10, textView11, textView12, graphType;
    ViewPager viewPager;
    LinearLayout previousPage;
    int []scoreArray = new int[7];
    int []userAnswers = new int[7];
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
        userAnswers = getArguments().getIntArray("userAnswers");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_game_analysis, container, false);
        toggle = (RadioGroup)view.findViewById(R.id.toggle);
        toggleScore = (RadioButton) view.findViewById(R.id.toggleScore);
        toggleTime = (RadioButton)view.findViewById(R.id.toggleTime);
        chartScore = (LineChart)view.findViewById(R.id.chartScore);
        chartTime = (LineChart)view.findViewById(R.id.chartTime);
        textView9 = (TextView)view.findViewById(R.id.textView9);
        textView10 = (TextView)view.findViewById(R.id.textView10);
        textView11 = (TextView)view.findViewById(R.id.textView11);
        textView12 = (TextView)view.findViewById(R.id.textView12);
        graphType = (TextView)view.findViewById(R.id.textGraphType);
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        previousPage = (LinearLayout)view.findViewById(R.id.previousPage);

        Typeface ourBoldFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/primebold.otf");
        Typeface ourLightFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/primelight.otf");
        textView9.setTypeface(ourLightFont);
        textView10.setTypeface(ourLightFont);
        textView11.setTypeface(ourLightFont);
        textView12.setTypeface(ourLightFont);
        graphType.setTypeface(ourBoldFont);

        previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AfterGame)getActivity()).setCurrentitem(0,true);
            }
        });

        /*int []location = new int[2];
        chartScore.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];*/

        viewPager.setAdapter(new MyPagerAdapter(getActivity().getApplicationContext(),scoreArray,timeArray));
        viewPager.setPageMargin(12);
        final SmartTabLayout viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
        if (userAnswers[0] == 1)
            viewPagerTab.setSelectedIndicatorColors(getResources().getColor(R.color.correctAnswer));
        else
            viewPagerTab.setSelectedIndicatorColors(getResources().getColor(R.color.wrongAnswer));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if (userAnswers[position] == 1)
                    viewPagerTab.setSelectedIndicatorColors(getResources().getColor(R.color.correctAnswer));
                else
                    viewPagerTab.setSelectedIndicatorColors(getResources().getColor(R.color.wrongAnswer));
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPagerTab.setViewPager(viewPager);

        setDataForScore();
        Legend legend1 = chartScore.getLegend();
        legend1.setForm(Legend.LegendForm.LINE);
        setDataForTime();
        Legend legend2 = chartTime.getLegend();
        legend2.setForm(Legend.LegendForm.LINE);

        chartScore.setTouchEnabled(true);
        chartScore.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        chartScore.setDrawBorders(false);
        chartScore.setBorderColor(getResources().getColor(R.color.textColorSecondary));
        chartScore.setBorderWidth(1f);
        chartScore.setPinchZoom(false);
        chartScore.setDoubleTapToZoomEnabled(false);
        chartScore.setScaleEnabled(false);
        chartScore.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartScore.setDrawGridBackground(false);
        chartScore.getXAxis().setDrawGridLines(false);
        chartScore.getAxisLeft().setDrawGridLines(false);
        chartScore.getAxisRight().setDrawGridLines(false);
        chartScore.getAxisRight().setEnabled(false);
        chartScore.getDescription().setEnabled(false);
        //chartScore.getDescription().setText("Question-Score Graph");
        //chartScore.getDescription().setTextColor(getResources().getColor(R.color.colorPrimary));
        //chartScore.getDescription().setPosition(x+2,y+2);
        chartScore.getData().setHighlightEnabled(true);
        chartScore.getLegend().setEnabled(false);
        chartScore.setHighlightPerDragEnabled(true);
        chartScore.setHighlightPerTapEnabled(true);

        chartTime.setTouchEnabled(true);
        chartTime.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        chartTime.setDrawBorders(false);
        chartTime.setBorderColor(getResources().getColor(R.color.textColorSecondary));
        chartTime.setBorderWidth(1f);
        chartTime.setPinchZoom(false);
        chartTime.setDoubleTapToZoomEnabled(false);
        chartTime.setScaleEnabled(false);
        chartTime.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartTime.setDrawGridBackground(false);
        chartTime.getXAxis().setDrawGridLines(false);
        chartTime.getAxisLeft().setDrawGridLines(false);
        chartTime.getAxisRight().setDrawGridLines(false);
        chartTime.getAxisRight().setEnabled(false);
        chartTime.getDescription().setEnabled(false);
        //chartTime.getDescription().setText("Question-Score Graph");
        //chartTime.getDescription().setTextColor(getResources().getColor(R.color.colorPrimary));
        //chartTime.getDescription().setPosition(x+2,y+2);
        chartTime.getData().setHighlightEnabled(true);
        chartTime.getLegend().setEnabled(false);
        chartTime.setHighlightPerDragEnabled(true);
        chartTime.setHighlightPerTapEnabled(true);

        YAxis yAxisScore = chartScore.getAxisLeft();
        yAxisScore.setEnabled(true);
        yAxisScore.setAxisMinimum(0);
        yAxisScore.setAxisMaximum(140);
        yAxisScore.setGranularity(20);
        yAxisScore.setTextColor(getResources().getColor(R.color.textColorPrimary));
        yAxisScore.setTypeface(ourLightFont);

        YAxis yAxisTime = chartTime.getAxisLeft();
        yAxisTime.setEnabled(true);
        yAxisTime.setAxisMinimum(0);
        yAxisTime.setAxisMaximum(totalTime+5);
        yAxisTime.setGranularity((totalTime+5)/7);
        yAxisTime.setTextColor(getResources().getColor(R.color.textColorPrimary));
        yAxisTime.setValueFormatter(new YAxisTimeValueFormater());
        yAxisTime.setTypeface(ourLightFont);

        XAxis xAxisScore = chartScore.getXAxis();
        xAxisScore.setEnabled(true);
        xAxisScore.setAxisMinimum(0.7f);
        xAxisScore.setAxisMaximum(7.3f);
        xAxisScore.setTextColor(getResources().getColor(R.color.textColorPrimary));
        xAxisScore.setTypeface(ourLightFont);

        XAxis xAxisTime = chartTime.getXAxis();
        xAxisTime.setEnabled(true);
        xAxisTime.setAxisMinimum(0.7f);
        xAxisTime.setAxisMaximum(7.3f);
        xAxisTime.setTextColor(getResources().getColor(R.color.textColorPrimary));
        xAxisTime.setTypeface(ourLightFont);

        System.out.println("scoreArray = "+scoreArray[0]+" "+scoreArray[1]+" "+scoreArray[2]+" "+scoreArray[3]+" "+scoreArray[4]+" "+scoreArray[5]+" "+scoreArray[6]);
        System.out.println("timeArray = "+timeArray[0]+" "+timeArray[1]+" "+timeArray[2]+" "+timeArray[3]+" "+timeArray[4]+" "+timeArray[5]+" "+timeArray[6]);

        toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId == toggleScore.getId()) {
                    chartScore.setVisibility(View.VISIBLE);
                    chartTime.setVisibility(View.GONE);
                    textView10.setVisibility(View.VISIBLE);
                    textView11.setVisibility(View.GONE);
                    toggleScore.setBackground(getResources().getDrawable(R.drawable.toggle_score_selector));
                    toggleTime.setBackground(getResources().getDrawable(R.drawable.toggle_score_selector));
                }
                else if (checkedId == toggleTime.getId()) {
                    chartTime.setVisibility(View.VISIBLE);
                    chartScore.setVisibility(View.GONE);
                    textView11.setVisibility(View.VISIBLE);
                    textView10.setVisibility(View.GONE);
                    toggleScore.setBackground(getResources().getDrawable(R.drawable.toggle_time_selector));
                    toggleTime.setBackground(getResources().getDrawable(R.drawable.toggle_time_selector));
                }
            }
        });

        return view;
    }

    private void setDataForScore() {
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

        LineDataSet set1 = new LineDataSet(vals1, "set1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(getResources().getColor(R.color.chartScoreColor));
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(3f);
        set1.setCircleRadius(4f);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setValueTextColor(getResources().getColor(R.color.textColorPrimary));
        set1.setDrawFilled(false);
        set1.setValueFormatter(new MyGraph1ValueFormatter());

        List<ILineDataSet>dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        chartScore.setData(data);
        chartScore.invalidate();
    }

    public void setDataForTime() {
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

        LineDataSet set2 = new LineDataSet(vals2, "set2");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(getResources().getColor(R.color.chartTimeColor));
        set2.setCircleColor(Color.WHITE);
        set2.setLineWidth(3f);
        set2.setCircleRadius(4f);
        set2.setValueTextSize(9f);
        set2.setDrawFilled(true);
        set2.setValueTextColor(getResources().getColor(R.color.textColorSecondary));
        set2.setDrawFilled(false);
        set2.setValueFormatter(new MyGraph2ValueFormatter());

        List<ILineDataSet>dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set2);

        LineData data = new LineData(dataSets);
        chartTime.setData(data);
        chartTime.invalidate();
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

    public class YAxisTimeValueFormater implements IAxisValueFormatter {

        DecimalFormat mFormat;

        public YAxisTimeValueFormater(){
            mFormat = new DecimalFormat("##0.0");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value);
        }
    }

    public class MyPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        int [] mScore = new int[7];
        float [] mTime = new float[7];
        String[] pageTitle = {
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
        };

        public MyPagerAdapter(Context context, int[] score, float[] time) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mScore = score;
            mTime = time;
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((RelativeLayout) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item_aftergame, container, false);
            TextView score = (TextView) itemView.findViewById(R.id.currentScore);
            TextView time = (TextView) itemView.findViewById(R.id.currentTime);
            TextView question = (TextView) itemView.findViewById(R.id.question);
            TextView correctOption = (TextView) itemView.findViewById(R.id.correctOption);
            TextView answer = (TextView) itemView.findViewById(R.id.answer);
            TextView textView13 = (TextView) itemView.findViewById(R.id.textView13);
            TextView textView14 = (TextView) itemView.findViewById(R.id.textView14);
            TextView textView15 = (TextView) itemView.findViewById(R.id.textView15);
            LinearLayout linearLayout1 = (LinearLayout) itemView.findViewById(R.id.aboveLayout);

            Typeface ourLightFont = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/primelight.otf");
            Typeface ourBoldFont = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/primebold.otf");
            score.setTypeface(ourLightFont);
            time.setTypeface(ourLightFont);
            question.setTypeface(ourLightFont);
            correctOption.setTypeface(ourLightFont);
            answer.setTypeface(ourLightFont);
            textView13.setTypeface(ourBoldFont);
            textView14.setTypeface(ourBoldFont);
            textView15.setTypeface(ourBoldFont);

            score.setText(String.valueOf(mScore[position]));
            time.setText(String.valueOf(mTime[position]));

            if (userAnswers[position] == 1) {
                Drawable icon1 = getResources().getDrawable(R.drawable.game_analysis_st);
                Drawable mWrappedDrawable1 = icon1.mutate();
                mWrappedDrawable1 = DrawableCompat.wrap(mWrappedDrawable1);
                DrawableCompat.setTint(mWrappedDrawable1, getResources().getColor(R.color.correctAnswer));
                linearLayout1.setBackground(mWrappedDrawable1);
            }
            else {
                Drawable icon2 = getResources().getDrawable(R.drawable.game_analysis_st);
                Drawable mWrappedDrawable2 = icon2.mutate();
                mWrappedDrawable2 = DrawableCompat.wrap(mWrappedDrawable2);
                DrawableCompat.setTint(mWrappedDrawable2, getResources().getColor(R.color.wrongAnswer));
                linearLayout1.setBackground(mWrappedDrawable2);
            }

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((RelativeLayout) object);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitle[position];
        }
    }
}
