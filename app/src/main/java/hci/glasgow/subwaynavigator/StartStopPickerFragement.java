package hci.glasgow.subwaynavigator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import helpers.HelperFunctions;
import model.Stop;
import model.Stops;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StartStopPickerFragement.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class StartStopPickerFragement extends Fragment {

    private OnFragmentInteractionListener mListener;
    public Stops stops;

    public StartStopPickerFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_stop_picker_fragement, container, false);
        // Inflate the layout for this fragment
        final NumberPicker picker = (NumberPicker) view.findViewById(R.id.start);
        picker.setMinValue(0);
        picker.setMaxValue(stops.getStops().size() - 1);
        picker.setDisplayedValues(stops.getStringArrayOfStopsStartingFrom(stops.getStops().get(0)));
        HelperFunctions.setDividerColor(picker, getResources().getColor(R.color.colorAccent));

        Button destination = (Button)view.findViewById(R.id.pick_destination);
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stop startStop = HelperFunctions.getStopFromPicker(picker,stops);
                onButtonPressed(startStop);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Stop stop) {
        if (mListener != null) {
            mListener.startStopSelected(stop);
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
        // TODO: Update argument type and name
        public void startStopSelected(Stop stop);
    }

}
