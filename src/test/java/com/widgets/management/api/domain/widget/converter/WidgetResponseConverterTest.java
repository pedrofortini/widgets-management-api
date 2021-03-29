package com.widgets.management.api.domain.widget.converter;

import com.widgets.management.api.domain.widget.Widget;
import io.swagger.model.WidgetCreateRequest;
import io.swagger.model.WidgetResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class WidgetResponseConverterTest {

    private WidgetResponseConverter converter;

    @Before
    public void setUp() {
        this.converter = new WidgetResponseConverter();
    }

    @Test
    public void shouldReturn_CorrectlyConvertedWidgetResponse_WhenInputIsValid(){

        Widget widget = new Widget(1L, 1L, 4L, 5L);
        widget.setId(1L);
        widget.setWidgetZIndex(2L);
        widget.setLastModificationDate(new Date());

        WidgetResponse convertedWidget = this.converter.convert(widget);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(convertedWidget.getId()).isEqualTo(widget.getId());
        softly.assertThat(convertedWidget.getWidgetX()).isEqualTo(widget.getWidgetX());
        softly.assertThat(convertedWidget.getWidgetY()).isEqualTo(widget.getWidgetY());
        softly.assertThat(convertedWidget.getWidgetZIndex()).isEqualTo(widget.getWidgetZIndex());
        softly.assertThat(convertedWidget.getWidth()).isEqualTo(widget.getWidth());
        softly.assertThat(convertedWidget.getHeight()).isEqualTo(widget.getHeight());
        softly.assertThat(convertedWidget.getLastModificationDate()).isNotBlank();

        softly.assertAll();
    }

    @Test
    public void shouldReturn_CorrectlyConvertedListOfWidgetResponse_WhenInputIsValid(){

        Page<Widget> widgets = new PageImpl<>(new ArrayList<>());

        List<WidgetResponse> convertedWidgetList = this.converter.convert(widgets);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(convertedWidgetList.size()).isEqualTo(0);
        softly.assertAll();
    }
}