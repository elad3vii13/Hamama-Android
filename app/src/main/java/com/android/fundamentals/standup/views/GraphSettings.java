package com.android.fundamentals.standup.views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.fundamentals.standup.R;
import com.android.fundamentals.standup.model.Sensor;
import com.thomashaertel.widget.MultiSpinner;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraphSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphSettings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView textView1, textView2;
    Button refreshBtn;
    Long from, to;
    GraphSettingsListener graphSettingsListener;
    int sensorId = 1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MultiSpinner spinner;
    ArrayList<Sensor> sensors;

    public GraphSettings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GraphSettings.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphSettings newInstance(String param1, String param2) {
        GraphSettings fragment = new GraphSettings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.graphSettingsListener = (GraphSettingsListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void ArrayAdapter(String[] SensorsArray, int[] secondArray){
    }

//    @Override
//    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
//        textView1 = view.findViewById(R.id.tvFrom);
//        textView2 = view.findViewById(R.id.tvTo);
//        refreshBtn = view.findViewById(R.id.btnRefresh);
//        spinner = view.findViewById(R.id.spProperties);
//
//        String[] sensors = new String[]{"one","two","three"};
//        final int[] array = new int[]{1,2,3};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,sensors);
//        spinner.setAdapter(adapter);
//        //spinner.setSelection(positionDefault);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                // DO SOMETHING = array[i];
//                // Toast.makeText(getContext(), array[i] + " ! ", Toast.LENGTH_SHORT).show();
//                sensorId = array[i];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        refreshBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putLong("from", from);
//                bundle.putLong("to", to);
//                bundle.putInt("sensor",sensorId);
//                graphSettingsListener.onNewGraphSettings(bundle);
//            }
//        });
//
//        Button btnFrom = view.findViewById(R.id.btnFrom);
//        Button btnTo = view.findViewById(R.id.btnTo);
//
//        btnFrom.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onClick(View v) {
//                showDateDialog(textView1, view);
//            }
//        });
//
//        btnTo.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onClick(View v) {
//                showDateDialog(textView2, view);
//            }
//        });
//        super.onViewCreated(view, savedInstanceState);
//    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        final int[] array = new int[]{1,2,3};
        textView1 = view.findViewById(R.id.tvFrom);
        textView2 = view.findViewById(R.id.tvTo);
        refreshBtn = view.findViewById(R.id.btnRefresh);
        spinner = view.findViewById(R.id.spProperties);

        ArrayAdapter<String> adapter;

        sensors = new ArrayList<Sensor>();
        //sensors.add(new Sensor(1, "temperature", "celsius"));
        //sensors.add(new Sensor(2, "humidity", "%"));

        // create spinner list elements
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        for(int i=0; i<sensors.size(); i++ ){
            adapter.add(sensors.get(i).getName());
        }

        MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
            public void onItemsSelected(boolean[] selected) {
               // sensorId = array[i];
               System.out.println(Arrays.toString(selected));
            }
        };

        // get spinner and set adapter
        spinner.setAdapter(adapter, false, onSelectedListener);

        // set initial selection
        boolean[] selectedItems = new boolean[adapter.getCount()];
        selectedItems[1] = true; // select second item
        spinner.setSelected(selectedItems);

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphSettingsListener.clearGraph();
                Bundle bundle = new Bundle();
                bundle.putLong("from", from);
                bundle.putLong("to", to);
                boolean[] selected = spinner.getSelected();
                for (int i=0; i<selected.length; i++){
                    if (selected[i]) {
                        bundle.putInt("sensor", (int) sensors.get(i).getId());
                        graphSettingsListener.onNewGraphSettings(bundle);
                    }
                }
            }
        });

        Button btnFrom = view.findViewById(R.id.btnFrom);
        Button btnTo = view.findViewById(R.id.btnTo);

        btnFrom.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showDateDialog(textView1, view);
            }
        });

        btnTo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showDateDialog(textView2, view);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph_settings, container, false);
    }

    private void showDateDialog(final TextView textView, View view) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                //Toast.makeText(view.getContext(), "Hour", Toast.LENGTH_SHORT).show();
                showTimeDialog(textView, view, calendar);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                //simpleDateFormat.for
                textView.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new DatePickerDialog(view.getContext(), dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog(final TextView time_in, View view, final Calendar calendar) {
        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                time_in.setText(time_in.getText() + " " + simpleDateFormat.format(calendar.getTime()));
                if(time_in.getId() == R.id.tvFrom) {
                    from = calendar.getTimeInMillis();
                }
                else {
                    to = calendar.getTimeInMillis();
                }
            }
        };

        new TimePickerDialog(view.getContext(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public interface GraphSettingsListener{
        public void onNewGraphSettings(Bundle bundle);
        public void clearGraph();
        public  ArrayList<Sensor> getSensorsList();
    }
}