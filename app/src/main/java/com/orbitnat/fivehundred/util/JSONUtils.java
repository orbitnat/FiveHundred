package com.orbitnat.fivehundred.util;

import org.json.JSONArray;
import org.json.JSONException;

public class JSONUtils {
    public static JSONArray concatJsonArray(JSONArray... arrs) throws JSONException {
        JSONArray result = new JSONArray();

        if (arrs != null) {
            for (JSONArray arr : arrs) {
                if (arr != null) {
                    for (int i = 0; i < arr.length(); i++) {
                        result.put(arr.get(i));
                    }
                }
            }
        }

        return result;
    }
}
