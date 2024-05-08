package org.example.zhc.util.zhc.validation;

import java.util.List;
import java.util.Set;

public interface ChangeListener<T> {
    void onChange(List<T> changes);
}
