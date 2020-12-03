package com.android.fundamentals.standup.views;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.fundamentals.standup.R;
import com.android.fundamentals.standup.model.Measure;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Graph#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Graph extends Fragment {

    private long minDate,maxDate;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    GraphView graph;
    static final int[] colors = new int[]{Color.RED, Color.BLUE, Color.GREEN};

    public Graph() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Graph.
     */
    // TODO: Rename and change types and number of parameters
    public static Graph newInstance(String param1, String param2) {
        Graph fragment = new Graph();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

private  LineGraphSeries<DataPoint> buildGraphData(JsonElement responseData){
    Type listType = new TypeToken<List<Measure>>() {}.getType();
    Properties data = new Gson().fromJson(responseData, Properties.class);
    String measures = data.getProperty("measures");
    String sensorName = data.getProperty("name");
    int sid = Integer.parseInt(data.getProperty("id"));
    List<Measure> mList = new Gson().fromJson(measures, listType);
    DataPoint[] dp = new  DataPoint[mList.size()];
    Calendar calendar = Calendar.getInstance();
    minDate = mList.get(0).getDate();
    maxDate = mList.get(0).getDate();

    for (int i = 0; i<mList.size();i++){
        if(mList.get(i).getDate() > maxDate)
            maxDate = mList.get(i).getDate();

        if(mList.get(i).getDate() < minDate)
            minDate = mList.get(i).getDate();

        calendar.setTimeInMillis(mList.get(i).getDate());
        dp[i] = new DataPoint(mList.get(i).getDate(), mList.get(i).getValue());
    }

    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
    series.setTitle(sensorName);
    series.setColor(colors[sid-1]);
    series.setDrawDataPoints(true);
    series.setDataPointsRadius(10);
    series.setThickness(8);

    return series;
}

public void clearGraph(){
        graph.removeAllSeries();
}

public void refreshGraph(String newData){
    JsonElement jsonData = JsonParser.parseString(newData);
    LineGraphSeries<DataPoint> series = buildGraphData(jsonData);

    // you can directly pass Date objects to DataPoint-Constructor
    // this will convert the Date to double via Date#getTime()

    // graph.removeAllSeries();
    graph.addSeries(series);
    // graph.init();
    // styling

    graph.getLegendRenderer().setVisible(true);
    graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

    // set date label formatter
    graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
    graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

    //set manual x bounds to have nice steps
    graph.getViewport().setMaxX(maxDate);
    graph.getViewport().setMinX(minDate);
    graph.getViewport().setXAxisBoundsManual(true);

    // as we use dates as labels, the human rounding to nice readable numbers
    // is not necessary
    graph.getGridLabelRenderer().setHumanRounding(false);
    graph.getViewport().setScrollable(true); // enables horizontal scrolling
    graph.getViewport().setScrollableY(true); // enables vertical scrolling
    graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
    graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.graph = (GraphView) view.findViewById(R.id.graph);

//        // generate Dates
//        Calendar calendar = Calendar.getInstance();
//        Date d1 = calendar.getTime();
//        calendar.add(Calendar.DATE, 1);
//        Date d2 = calendar.getTime();
//        calendar.add(Calendar.DATE, 1);
//        Date d3 = calendar.getTime();

//        GraphView graph = (GraphView) view.findViewById(R.id.graph);
//
//        // you can directly pass Date objects to DataPoint-Constructor
//        // this will convert the Date to double via Date#getTime()
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(d1, 1),
//                new DataPoint(d2, 2),
//                new DataPoint(d3, 3)
//        });
//
//        graph.addSeries(series);
//
//        // set date label formatter
//        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity().getApplicationContext()));
//        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space
////
//        // set manual x bounds to have nice steps
//        graph.getViewport().setMinX(d1.getTime());
//        graph.getViewport().setMaxX(d3.getTime());
//        graph.getViewport().setXAxisBoundsManual(true);
//
//        // as we use dates as labels, the human rounding to nice readable numbers
//        // is not necessary
//        graph.getGridLabelRenderer().setHumanRounding(false);
        //graph.removeAllSeries();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }
}