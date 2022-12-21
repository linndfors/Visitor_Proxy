package org.example;

import java.util.HashMap;
import java.util.Map;

public class InGroupVisitor<T> implements Visitor<T> {
    private boolean inGroup = false;

    @Override
    public Map<String, String> onSignature(Task<T> task) {
        Map<String,String> map=new HashMap<String,String>();
        map.put("in_group", String.valueOf(inGroup));
        map.put("stamped_headers","in_group");
        return map;
    }

    @Override
    public Map<String, String> onGroupStart(Task<T> task) {
        inGroup = true;
        Map<String,String> map=new HashMap<String,String>();
        map.put("in_group", String.valueOf(inGroup));
        map.put("stamped_headers","in_group");
        return map;
    }

    @Override
    public Map<String, String> onGroupEnd(Task<T> task) {
        inGroup = false;
        Map<String,String> map=new HashMap<String,String>();
        map.put("in_group", String.valueOf(inGroup));
        map.put("stamped_headers","in_group");
        return map;
    }
}
