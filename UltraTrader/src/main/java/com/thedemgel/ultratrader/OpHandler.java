package com.thedemgel.ultratrader;

import java.util.ArrayList;
import java.util.List;

public class OpHandler {
    private List<String> toggle = new ArrayList<>();

    public void applyToggle(String value) {
        if (toggle.contains(value)) {
            toggle.remove(value);
        } else {
            toggle.add(value);
        }
    }

    public boolean getToggle(String value) {
        if (toggle.contains(value)) {
            return true;
        }

        return false;
    }
}
