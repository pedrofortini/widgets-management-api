package com.widgets.management.api.domain.widget.converter;

import com.widgets.management.api.domain.widget.Widget;
import io.swagger.model.WidgetCreateRequest;
import io.swagger.model.WidgetUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class WidgetRequestConverter {

    public Widget convertCreateRequest(WidgetCreateRequest request) {

        Widget converted = new Widget(request.getWidgetX(),
                request.getWidgetY(),
                request.getWidth(),
                request.getHeight());

        converted.setWidgetZIndex(request.getWidgetZIndex());

        return converted;
    }

    public Widget convertUpdateRequest(WidgetUpdateRequest request, Long id) {

        Widget converted = new Widget();

        converted.setId(id);
        converted.setWidgetX(request.getWidgetX());
        converted.setWidgetY(request.getWidgetY());
        converted.setWidgetZIndex(request.getWidgetZIndex());
        converted.setWidth(request.getWidth());
        converted.setHeight(request.getHeight());

        return converted;
    }
}
