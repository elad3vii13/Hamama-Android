package com.android.hamama.application.views;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.hamama.application.R;
import com.android.hamama.application.model.Measure;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

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
    static final int[] colors = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA};

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

/*
    In order to build the graph, the graph requires you to work with the
    DataPoint type and build an array.

*/
private  LineGraphSeries<DataPoint> buildGraphData(JsonElement responseData){

    Type listType = new TypeToken<List<Measure>>() {}.getType();
    Properties data = new Gson().fromJson(responseData, Properties.class);

    String measures = data.getProperty("measures");
    String sensorName = data.getProperty("name");
    int sid = Integer.parseInt(data.getProperty("id"));
    List<Measure> mList = new Gson().fromJson(measures, listType);

    DataPoint[] dp = new  DataPoint[mList.size()];

    if(mList.size() == 0) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
        series.setTitle(sensorName);
        series.setColor(colors[sid-1]);
        Toast.makeText(getActivity(), "No Data Found for current settings", Toast.LENGTH_SHORT).show();
        return null;
    }

    Calendar calendar = Calendar.getInstance();

    if (minDate==0)
        minDate = mList.get(0).getTime();
    if (maxDate == 0)
        maxDate = mList.get(0).getTime();

    for (int i = 0; i<mList.size(); i++){

        /* Because the graph requires you to tell him the specific period of time
        I would search for the max and min points */
        if(mList.get(i).getTime() > maxDate)
            maxDate = mList.get(i).getTime();
        if(mList.get(i).getTime() < minDate)
            minDate = mList.get(i).getTime();

        calendar.setTimeInMillis(mList.get(i).getTime());
        dp[i] = new DataPoint(mList.get(i).getTime(), mList.get(i).getValue());
    }

    // Build the graph series with the data that we have
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
        minDate =0;
        maxDate=0;
}

public void refreshGraph(String newData){
    JsonElement jsonData = JsonParser.parseString(newData);
    LineGraphSeries<DataPoint> series = buildGraphData(jsonData);

    // you can directly pass Date objects to DataPoint-Constructor
    // this will convert the Date to double via Date#getTime()

    if(series != null)
        graph.addSeries(series);

    graph.getLegendRenderer().setVisible(true);
    graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    // set date label formatter
    graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
    graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
    //set manual x bounds to have nice steps
    if (maxDate!=0)
        graph.getViewport().setMaxX(maxDate);
    if (minDate!=0)
        graph.getViewport().setMinX(minDate);

    graph.getViewport().setYAxisBoundsManual(true);
    graph.getViewport().setMaxY(15);
    graph.getViewport().setMinY(0);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }
}