package com.example.oop;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SpinnerMy {
    int id;
    Dialog dialog;
    Spinner spinner;
    String[] params;
    ArrayAdapter<String> adapter;
    FilterParameters paramsF;
    Context context;

    SpinnerMy(Dialog dialog, String[] params, FilterParameters paramsF, Context context, int spinner_id){
        this.spinner = dialog.findViewById(spinner_id);
        this.params = params;
        this.adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, this.params);
        this.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(this.adapter);

        this.paramsF = paramsF;

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paramsF.map.put(spinner_id, (String)parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        this.spinner.setOnItemSelectedListener(itemSelectedListener);
    }
}
