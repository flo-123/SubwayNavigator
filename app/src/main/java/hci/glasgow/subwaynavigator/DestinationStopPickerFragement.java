package hci.glasgow.subwaynavigator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import helpers.HelperFunctions;
import model.Stop;
import model.Stops;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DestinationStopPickerFragement.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DestinationStopPickerFragement extends Fragment {

    private OnFragmentInteractionListener mListener;
    public Stops stops;
    public Stop startStop;
    private String[] listOfStops;
    private NumberPicker picker;
    private View view;
    private boolean inbound = true;

    public DestinationStopPickerFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_destination_stop_picker_fragement, container, false);


        listOfStops = stops.getStringArrayOfStopsStartingFrom(stops.getStops().get(stops.getStops().indexOf(startStop)));
        picker = (NumberPicker)view.findViewById(R.id.destination);
        picker.setMinValue(0);
        picker.setMaxValue(listOfStops.length - 1);
        picker.setDisplayedValues(listOfStops);
        HelperFunctions.setDividerColor(picker, getResources().getColor(R.color.colorAccent));

        Button start = (Button)view.findViewById(R.id.start_navigation);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stop destStop = HelperFunctions.getStopFromPicker(picker, stops);
                onButtonPressed(destStop,inbound);
            }
        });

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.direction);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {




            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                toggleDirection();
                if (checkedId == R.id.radio_inbound) {
                    inbound = true;
                } else if (checkedId == R.id.radio_outgoing) {
                    inbound = false;
                }

            }

        });

        return view;
    }

    public void onButtonPressed(Stop stop, boolean inbound) {
        if (mListener != null) {
            mListener.destinatinStopSelected(stop, inbound);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void destinatinStopSelected(Stop stop, boolean inbound);
    }

    private void toggleDirection() {
        listOfStops = reverse(listOfStops);
        picker = (NumberPicker)view.findViewById(R.id.destination);
        picker.setMinValue(0);
        picker.setDisplayedValues(null);
        picker.setMaxValue(listOfStops.length -1);
        picker.setDisplayedValues(listOfStops);
        picker.setValue(0);
    }

    private String[] reverse(String[] input) {
        String [] output = new String[input.length];
        output[0] = input[0];
        for (int i = 1; i < input.length; i++) {
            output[input.length-i] = input[i];
        }
        return output;
    }

}
