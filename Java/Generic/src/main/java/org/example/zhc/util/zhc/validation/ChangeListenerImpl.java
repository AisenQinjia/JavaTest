package org.example.zhc.util.zhc.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChangeListenerImpl {
    List<ChangeListener<String>> changeListeners = new ArrayList<>();
    public void addListener(ChangeListener<String> listener) {
        System.out.println("addListener");
        changeListeners.add(listener);
    }

    public void onChange(String str) {
        System.out.println("onChange");
        for (ChangeListener<String> listener : changeListeners) {
            //str->list
            List<String> list = new ArrayList<>();
            list.add(str);
            listener.onChange(list);
        }
    }
}
