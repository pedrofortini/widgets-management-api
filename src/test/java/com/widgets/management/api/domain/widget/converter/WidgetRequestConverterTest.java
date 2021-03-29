package com.widgets.management.api.domain.widget.converter;

import com.widgets.management.api.application.exception.WidgetPersistenceException;
import com.widgets.management.api.domain.widget.Widget;
import io.swagger.model.WidgetCreateRequest;
import io.swagger.model.WidgetUpdateRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

public class WidgetRequestConverterTest {

    private WidgetRequestConverter converter;

    @Before
    public void setUp() {
        this.converter = new WidgetRequestConverter();
    }

    @Test
    public void shouldReturn_CorrectlyConvertedCreateRequest_WhenInputRequestIsValid(){

        WidgetCreateRequest createRequest = buildCreateRequest(0L, 0L, 2L, 4L, 5L);

        Widget convertedRequest = this.converter.convertCreateRequest(createRequest);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(convertedRequest.getWidgetX()).isEqualTo(createRequest.getWidgetX());
        softly.assertThat(convertedRequest.getWidgetY()).isEqualTo(createRequest.getWidgetY());
        softly.assertThat(convertedRequest.getWidgetZIndex()).isEqualTo(createRequest.getWidgetZIndex());
        softly.assertThat(convertedRequest.getWidth()).isEqualTo(createRequest.getWidth());
        softly.assertThat(convertedRequest.getHeight()).isEqualTo(createRequest.getHeight());

        softly.assertAll();
    }

    @Test
    public void shouldReturn_CorrectlyConvertedUpdateRequest_WhenInputRequestIsValid(){

        WidgetUpdateRequest updateRequest = buildUpdateRequest(2L);

        Widget convertedRequest = this.converter.convertUpdateRequest(updateRequest, 1L);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(convertedRequest.getId()).isEqualTo(1L);
        softly.assertThat(convertedRequest.getWidgetZIndex()).isEqualTo(updateRequest.getWidgetZIndex());

        softly.assertAll();
    }

    private WidgetCreateRequest buildCreateRequest(Long widgetX, Long widgetY, Long widgetZIndex, Long width, Long height)
    {
        WidgetCreateRequest request = new WidgetCreateRequest();

        request.setWidgetX(widgetX);
        request.setWidgetY(widgetY);
        request.setWidgetZIndex(widgetZIndex);
        request.setWidth(width);
        request.setHeight(height);

        return request;
    }

    private WidgetUpdateRequest buildUpdateRequest(Long widgetZIndex)
    {
        WidgetUpdateRequest request = new WidgetUpdateRequest();

        request.setWidgetZIndex(widgetZIndex);
        return request;
    }
}