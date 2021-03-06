package com.khansa.thedictionary.fragment;



import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.khansa.mydictonary.R;
import com.khansa.thedictionary.Word_meaningActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDefination extends Fragment {


    public FragmentDefination() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_defination,container, false);//Inflate Layout

        Context context=getActivity();
        TextView text = (TextView) view.findViewById(R.id.textView);

        String en_definition= ((Word_meaningActivity)context).en_defination;

        text.setText(en_definition);
        if(en_definition==null)
        {
            text.setText("No definition found");
        }

        return view;
    }
}
