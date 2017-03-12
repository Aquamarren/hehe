package com.example.marrenmatias.trynavdrawer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class PieChartGraph extends AppCompatActivity {
    SQLiteDatabase db;
    DatabaseHelper mydb;
    private RelativeLayout mainLayout;
    private PieChart mChart;
    private Button btnBarGraph;
    private Button btnForecastPage;
    private Button btnPieChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_chart);
        openDatabase();


        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        btnBarGraph = (Button)findViewById(R.id.btnBarGraph);
        btnForecastPage = (Button)findViewById(R.id.btnForecastPage);
        btnPieChart = (Button)findViewById(R.id.btnPieChart);
        mChart = new PieChart(this);
        mainLayout.addView(mChart,500,1500);

        mChart.setCenterText("");
        mChart.setUsePercentValues(true);
        mChart.setDescription("Expenses");

        mChart.setDrawHoleEnabled(true);
       //mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(40);
        mChart.setTransparentCircleRadius(40);

        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);


        setCategoriesPieChart();

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(17);
        l.setYEntrySpace(15);

        Buttons();
    }

    protected void openDatabase() {
        db = openOrCreateDatabase("THRIFTY.db", Context.MODE_PRIVATE, null);
    }

    public  void Buttons(){
        btnBarGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PieChartGraph.this, BarChartGraph.class);
                startActivity(intent);
            }
        });

        btnForecastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PieChartGraph.this, ForecastBudgetWeek.class);
                startActivity(intent);
            }
        });

        btnPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PieChartGraph.this, PieChartGraph.class);
                startActivity(intent);
            }
        });
    }

    private void setCategoriesPieChart() {

        String SQL = "SELECT SUM(ExpenseAmount), CategoryName FROM EXPENSE WHERE ACTIVE = 1 GROUP BY CategoryName";
        Cursor ce = db.rawQuery(SQL, null);
        int count = ce.getCount();

        float[] values = new float[count];
        String[] categoryNames = new String[count];

        for (int i = 0; i < count; i++) {
            ce.moveToNext();
            categoryNames[i] = ce.getString(1);
            values[i] = ce.getFloat(0);
        }

            ArrayList<Entry> yVals1 = new ArrayList<Entry>();

            for (int j = 0; j < count; j++)
                yVals1.add(new Entry(values[j], j));

            ArrayList<String> xVals = new ArrayList<String>();

            for (int j = 0; j < count; j++)
                xVals.add(categoryNames[j]);


        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        //for (int c : ColorTemplate.VORDIPLOM_COLORS)
            //colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        //for (int c : ColorTemplate.COLORFUL_COLORS)
            //colors.add(c);

        //for (int c : ColorTemplate.LIBERTY_COLORS)
            //colors.add(c);

        //for (int c : ColorTemplate.PASTEL_COLORS)
            //colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.BLACK);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();
    }


}
