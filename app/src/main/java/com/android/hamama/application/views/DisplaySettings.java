package com.android.hamama.application.views;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.hamama.application.R;
import com.android.hamama.application.model.Sensor;
import com.thomashaertel.widget.MultiSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplaySettings#newInstance} factory method to
 * create an instance of this fragment.
 */

public class DisplaySettings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    long WEEK_IN_UNIXTIME = 604800 * 1000; /* 60(minutes)*60(hours)= 3600 seconds
    7(Days in a week) * 24(hours a day) > 24*7*3600 > hours to seconds: 604800(in seconds) * 1000 > 604800000(mili seconds)
    */

    ProgressBar progressBar;
    TextView textView1, textView2;
    Button refreshBtn;
    Button updateSensors;
    Long from, to;
    SettingsListener graphSettingsListener;
    int sensorId = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MultiSpinner spinner;
    Spinner spinnerPriority;
    String[] priorityLevels = {"", "error", "warning", "info"};
    ArrayList<Sensor> sensors;

    public DisplaySettings() {
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
    public static DisplaySettings newInstance(String param1, String param2) {
        DisplaySettings fragment = new DisplaySettings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.graphSettingsListener = (SettingsListener) context; // ???
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        /* SHOWES THE LAST RESULT FROM THE LAST WEEK
        It does that, by creating a request from the server with the function
        onNewSettings of the graphSettingsListener (which means the function declered on-
        SensorBasedActivity because this is who implements the interface).
        */

        if (graphSettingsListener.showPriority()) { // LOG scenario
            Bundle bundle;
            bundle = new Bundle();

            to = System.currentTimeMillis();
            from = to - WEEK_IN_UNIXTIME;

            bundle.putLong("from", from);
            bundle.putLong("to", to);
            graphSettingsListener.onNewSettings(bundle);
            Toast.makeText(getContext(), "Logs from the past week", Toast.LENGTH_SHORT).show();
        }
    }

    /*
        the funciton, build a MultiSpinner for the measures activity.
        and regular spinner for the log activity.
    */

    public void initGraphSettings() {
        ArrayAdapter<String> adapter;
        sensors = graphSettingsListener.getSensorsList(); // it gets the list of the sensors as arrayList<Sensor>
        progressBar.setVisibility(View.INVISIBLE); // start as invisible
        updateSensors.setEnabled(true); // set the button as visible

        if(sensors == null) return;

        /*
            this block of code, initialise the adapter add him the sensors,
            and then sets the spinner the adapter.

           this multi-spinner will be shown only on the log activity.
        */

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        for(int i=0; i<sensors.size(); i++ ){
            adapter.add(sensors.get(i).getName());
        }

        MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
            public void onItemsSelected(boolean[] selected) {
                // sensorId = array[i];
                //System.out.println(Arrays.toString(selected));
            }
        };

        // get spinner and set adapter
        spinner.setAdapter(adapter, false, onSelectedListener);

        // set initial selection
        boolean[] selectedItems = new boolean[adapter.getCount()];
        selectedItems[1] = true; // select second item - as default
        spinner.setSelected(selectedItems);
        spinner.setEnabled(true);

        /* Because this is a multiSpinner, we can select multiple
        choices, we start by setting a boolean array, and the array value at the
        index of the selected sensors would be 'true'.
        */

        /* This code, will build the regular spinner of the priority.
           this spinner will be shown only on the log activity.
        */

        // create spinner list elements
        ArrayAdapter<String> adapter2;
        adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        adapter2.add("כולם");
        adapter2.add("תקלה");
        adapter2.add("אזהרה");
        adapter2.add("אינפורמציה");

        // get spinner and set adapter
        spinnerPriority.setAdapter(adapter2);
        spinnerPriority.setSelection(0);
        spinnerPriority.setEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        textView1 = view.findViewById(R.id.tvFrom);
        textView2 = view.findViewById(R.id.tvTo);
        refreshBtn = view.findViewById(R.id.btnRefresh);
        spinner = view.findViewById(R.id.spProperties);
        spinnerPriority = view.findViewById(R.id.spPriority);
        updateSensors = view.findViewById(R.id.updateSensors);
        progressBar = view.findViewById(R.id.progressBar2);

        /*
            With the help of the function 'showPriority' that declares
            in the 'Measures' and the 'Log' differently, we can know
            in what way to adjust the displaySettings that would fit
            to the desired look.

            In that example, we can understand that we want we don't
            the updateSensors button and the progressBar to appear.
        */

        if(graphSettingsListener.showPriority()){ // LOG
            updateSensors.setVisibility(view.INVISIBLE);
            progressBar.setVisibility(view.INVISIBLE);
        }

        /*
            In the case of a click on the updateSensors button,
            we want to show the progress bar with a fit toast message,
            and of course disable the button while it's requesting the new
            sensors list from the server. (we do that by using the function written on
            SensorBasedActivity [which is implementing the interface from DisplaySettings]).
        */

        updateSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(v.getContext(), "loading the sensor list ...", Toast.LENGTH_SHORT).show();
                updateSensors.setEnabled(false);
                graphSettingsListener.refreshSensorsList();
            }
        });

        /*
            This is what will happen, after clicking on the refreshBtn button:
            Log Scenario:
                It will build the right bundle for that, (from, to, priority[from the spinner])

            Measure Scenario:
                it will build the right bundle for that [from, to, sensor]
                and it will repeat the code, until there are no true values in the array.
        */

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphSettingsListener.clearDisplay();
                Bundle bundle;

                if (graphSettingsListener.showPriority()) { //LOG scenario
                    bundle = new Bundle();
                    bundle.putLong("from", from);
                    bundle.putLong("to", to);
                    String priorityLevel = priorityLevels[spinnerPriority.getSelectedItemPosition()];
                    if (!priorityLevel.isEmpty())
                        bundle.putString("priority", priorityLevel);
                    graphSettingsListener.onNewSettings(bundle);
                }

                else{
                    boolean[] selected = spinner.getSelected();
                    for (int i=0; i<selected.length; i++){
                        if (selected[i]) {
                            bundle = new Bundle();
                            bundle.putLong("from", from);
                            bundle.putLong("to", to);
                            bundle.putInt("sensor", (int) sensors.get(i).getId());
                            graphSettingsListener.onNewSettings(bundle);
                        }
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

    /*
        Function - that takes care of the open the Date Picker
    */

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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                time_in.setText(time_in.getText() + " " + simpleDateFormat.format(calendar.getTime()));
                if(time_in.getId() == R.id.tvFrom) {
                    from = calendar.getTimeInMillis();
                }
                else {
                    to = calendar.getTimeInMillis();
                }
            }
        };

        new TimePickerDialog(view.getContext(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),false).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  spinnerPriority.setVisibility(graphSettingsListener.showPriority()?View.VISIBLE:View.GONE);
        //  spinner.setVisibility(graphSettingsListener.showPriority()?View.GONE:View.VISIBLE);
        getView().findViewById(R.id.sensorLayout).setVisibility(graphSettingsListener.showPriority()?View.GONE:View.VISIBLE);
        getView().findViewById(R.id.linearPriority).setVisibility(graphSettingsListener.showPriority()?View.VISIBLE:View.GONE);
    }

    public interface SettingsListener{
        public void onNewSettings(Bundle bundle);
        public void clearDisplay();
        public  ArrayList<Sensor> getSensorsList();
        public void refreshSensorsList();
        public boolean showPriority();
    }
}