package com.widgets.management.api.domain.widget.converter;

import com.widgets.management.api.application.util.DateUtil;
import com.widgets.management.api.domain.widget.Widget;
import io.swagger.model.WidgetResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WidgetResponseConverter {

    public WidgetResponse convert(Widget widget)
    {
        WidgetResponse response = new WidgetResponse();

        response.setId(widget.getId());
        response.setWidgetX(widget.getWidgetX());
        response.setWidgetY(widget.getWidgetY());
        response.setWidth(widget.getWidth());
        response.setHeight(widget.getHeight());
        response.setWidgetZIndex(widget.getWidgetZIndex());
        response.setLastModificationDate(DateUtil.convertDateToString(widget.getLastModificationDate()));

        return response;
    }

    public List<WidgetResponse> convert(Page<Widget> widgets){

        return widgets.getContent().stream().map(
                widget -> convert(widget)).
                collect(Collectors.toList());
    }
}
