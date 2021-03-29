package com.widgets.management.api.domain.widget.comparator;

import com.widgets.management.api.domain.widget.Widget;

import java.util.Comparator;

public class WidgetComparator implements Comparator<Widget> {

    @Override
    public int compare(Widget a, Widget b) {
        return Long.compare(a.getWidgetZIndex(), b.getWidgetZIndex());
    }
}
