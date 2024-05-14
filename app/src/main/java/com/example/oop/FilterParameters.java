package com.example.oop;

import java.util.HashMap;
import java.util.Map;

public class FilterParameters {

    Map<Integer, String> map = new HashMap<Integer, String>();

    FilterParameters(int nationality_spinner, int WB_spinner, int gender_spinner){
        map.put(nationality_spinner, "");
        map.put(WB_spinner, "");
        map.put(gender_spinner, "");
    }
}
