package com.widgets.management.api.application.controllers;

import com.widgets.management.api.application.exception.InvalidRequestException;
import com.widgets.management.api.application.exception.WidgetPersistenceException;
import com.widgets.management.api.application.util.DateUtil;
import com.widgets.management.api.domain.widget.PersistWidgetService;
import com.widgets.management.api.domain.widget.SearchWidgetService;
import com.widgets.management.api.domain.widget.Widget;
import com.widgets.management.api.domain.widget.converter.WidgetRequestConverter;
import com.widgets.management.api.domain.widget.converter.WidgetResponseConverter;
import com.widgets.management.api.infrastructure.persistence.WidgetRepository;
import io.swagger.model.WidgetCreateRequest;
import io.swagger.model.WidgetResponse;
import io.swagger.model.WidgetUpdateRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

public class WidgetsControllerTest {

    private WidgetsController controller;

    private PersistWidgetService persistWidgetService;
    private SearchWidgetService searchWidgetService;
    private WidgetRequestConverter requestConverter;
    private WidgetResponseConverter responseConverter;

    @Before
    public void setUp() {

        this.persistWidgetService = PowerMockito.mock(PersistWidgetService.class);
        this.searchWidgetService = PowerMockito.mock(SearchWidgetService.class);
        this.requestConverter = PowerMockito.mock(WidgetRequestConverter.class);
        this.responseConverter = PowerMockito.mock(WidgetResponseConverter.class);

        this.controller = new WidgetsController(this.persistWidgetService, this.searchWidgetService,
                this.requestConverter, this.responseConverter);
    }

    /********************** TESTS CONTROLLER METHODS  *****************************************************/

    @Test
    public void shouldReturn_ResponseEntityOKAndCallCorrectMethods_WhenUsingCreateWidgetRequest() {

        WidgetCreateRequest createRequest = buildCreateRequest(0L, 0L, 2L, 4L, 5L);

        Widget requestConverterReturn = new Widget(0L,0L,4L,5L);
        requestConverterReturn.setWidgetZIndex(2L);

        Widget saveReturn = new Widget(0L,0L,4L,5L);
        saveReturn.setWidgetZIndex(2L);
        saveReturn.setId(1L);

        WidgetResponse responseConverterReturn =
                buildWidgetResponse(0L, 0L, 2L, 4L, 5L, 1L);

        PowerMockito.when(this.requestConverter.convertCreateRequest(any(WidgetCreateRequest.class))).
                thenReturn(requestConverterReturn);
        PowerMockito.when(this.persistWidgetService.saveWidget(any(Widget.class), anyBoolean())).
                thenReturn(saveReturn);
        PowerMockito.when(this.responseConverter.convert(any(Widget.class))).
                thenReturn(responseConverterReturn);

        ResponseEntity response = this.controller.createWidget(createRequest);

        ArgumentCaptor<WidgetCreateRequest> convertRequestArgument = ArgumentCaptor.forClass(WidgetCreateRequest.class);
        verify(this.requestConverter, times(1)).convertCreateRequest(convertRequestArgument.capture());

        ArgumentCaptor<Widget> saveWidgetArgument = ArgumentCaptor.forClass(Widget.class);
        verify(this.persistWidgetService, times(1)).saveWidget(saveWidgetArgument.capture(), anyBoolean());

        ArgumentCaptor<Widget> responseConvertArgument = ArgumentCaptor.forClass(Widget.class);
        verify(this.responseConverter, times(1)).convert(responseConvertArgument.capture());

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(convertRequestArgument.getValue()).isEqualTo(createRequest);
        softly.assertThat(saveWidgetArgument.getValue()).isEqualTo(requestConverterReturn);
        softly.assertThat(responseConvertArgument.getValue()).isEqualTo(saveReturn);

        softly.assertThat(response.getStatusCode().value()).isEqualTo(200);
        softly.assertThat(response.getBody()).isEqualTo(responseConverterReturn);

        softly.assertAll();
    }

    @Test
    public void shouldReturn_ResponseEntityOKAndCallCorrectMethods_WhenUsingUpdateWidgetRequest() {

        WidgetUpdateRequest updateRequest = buildUpdateRequest(2L);

        Widget requestConverterReturn = new Widget(0L,0L,4L,5L);
        requestConverterReturn.setWidgetZIndex(2L);
        requestConverterReturn.setId(1L);

        Widget updateReturn = new Widget(0L,0L,4L,5L);
        updateReturn.setWidgetZIndex(2L);
        updateReturn.setId(1L);

        WidgetResponse responseConverterReturn =
                buildWidgetResponse(0L, 0L, 2L, 4L, 5L, 1L);

        PowerMockito.when(this.requestConverter.convertUpdateRequest(any(WidgetUpdateRequest.class), any(Long.class))).
                thenReturn(requestConverterReturn);
        PowerMockito.when(this.persistWidgetService.updateWidget(any(Widget.class))).
                thenReturn(updateReturn);
        PowerMockito.when(this.responseConverter.convert(any(Widget.class))).
                thenReturn(responseConverterReturn);

        ResponseEntity response = this.controller.updateWidget(1L, updateRequest);

        ArgumentCaptor<WidgetUpdateRequest> convertRequestArgument = ArgumentCaptor.forClass(WidgetUpdateRequest.class);
        verify(this.requestConverter, times(1)).convertUpdateRequest(convertRequestArgument.capture(), any(Long.class));

        ArgumentCaptor<Widget> updateWidgetArgument = ArgumentCaptor.forClass(Widget.class);
        verify(this.persistWidgetService, times(1)).updateWidget(updateWidgetArgument.capture());

        ArgumentCaptor<Widget> responseConvertArgument = ArgumentCaptor.forClass(Widget.class);
        verify(this.responseConverter, times(1)).convert(responseConvertArgument.capture());

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(convertRequestArgument.getValue()).isEqualTo(updateRequest);
        softly.assertThat(updateWidgetArgument.getValue()).isEqualTo(requestConverterReturn);
        softly.assertThat(responseConvertArgument.getValue()).isEqualTo(updateReturn);

        softly.assertThat(response.getStatusCode().value()).isEqualTo(200);
        softly.assertThat(response.getBody()).isEqualTo(responseConverterReturn);

        softly.assertAll();
    }

    @Test
    public void shouldReturn_ResponseEntityNoContentAndCallCorrectMethods_WhenUsingDeleteWidgetByIdRequest() {

        ResponseEntity response = this.controller.deleteWidgetById(1L);

        ArgumentCaptor<Long> deleteRequestArgument = ArgumentCaptor.forClass(Long.class);
        verify(this.persistWidgetService, times(1)).deleteWidgetById(deleteRequestArgument.capture());

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(deleteRequestArgument.getValue()).isEqualTo(1L);
        softly.assertThat(response.getStatusCode().value()).isEqualTo(204);

        softly.assertAll();
    }

    @Test
    public void shouldReturn_ResponseEntityOKAndCallCorrectMethods_WhenUsingGetWidgetByIdRequest() {

        Widget widget1 = new Widget();
        widget1.setId(1L);

        WidgetResponse responseConverterReturn =
                buildWidgetResponse(0L, 0L, 2L, 4L, 5L, 1L);

        PowerMockito.when(this.searchWidgetService.findWidgetById(any(Long.class))).
                thenReturn(widget1);
        PowerMockito.when(this.responseConverter.convert(any(Widget.class))).
                thenReturn(responseConverterReturn);

        ResponseEntity response = this.controller.getWidgetById(1L);

        ArgumentCaptor<Long> findByIdRequestArgument = ArgumentCaptor.forClass(Long.class);
        verify(this.searchWidgetService, times(1)).findWidgetById(findByIdRequestArgument.capture());

        ArgumentCaptor<Widget> convertArgument = ArgumentCaptor.forClass(Widget.class);
        verify(this.responseConverter, times(1)).convert(convertArgument.capture());

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(findByIdRequestArgument.getValue()).isEqualTo(1L);
        softly.assertThat(convertArgument.getValue()).isEqualTo(widget1);
        softly.assertThat(response.getStatusCode().value()).isEqualTo(200);
        softly.assertThat(response.getBody()).isEqualTo(responseConverterReturn);

        softly.assertAll();
    }

    @Test
    public void shouldThrow_InvalidRequestExceptionWithCorrectMessage_WhenInvalidGetWidgetsRequest(){

        assertThatThrownBy(() -> this.controller.getWidgets(-1L, 604L))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("Invalid Request. Current Page must be at least 0. Page size must be between 1 and 500");
    }

    @Test
    public void shouldReturn_ResponseEntityOKAndCallCorrectMethods_WhenUsingGetWidgetsRequest() {

        Page<Widget> widgets = new PageImpl<>(new ArrayList<>());
        List<WidgetResponse> responseList = new ArrayList<>();

        PowerMockito.when(this.searchWidgetService.findAllWidgetsSortedByZIndexPaginated(any(Long.class), any(Long.class))).
                thenReturn(widgets);
        PowerMockito.when(this.responseConverter.convert(any(Page.class))).
                thenReturn(responseList);

        ResponseEntity response = this.controller.getWidgets(0L, 10L);

        ArgumentCaptor<Page<Widget>> convertArgument = ArgumentCaptor.forClass(Page.class);
        verify(this.responseConverter, times(1)).convert(convertArgument.capture());

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(convertArgument.getValue()).isEqualTo(widgets);
        softly.assertThat(response.getStatusCode().value()).isEqualTo(200);
        softly.assertThat(response.getBody()).isEqualTo(responseList);

        softly.assertAll();
    }

    /******************************************************************************************************/

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

    private WidgetResponse buildWidgetResponse(Long widgetX, Long widgetY,
                                               Long widgetZIndex, Long width,
                                               Long height, Long id)
    {
        WidgetResponse response = new WidgetResponse();

        response.setWidgetX(widgetX);
        response.setWidgetY(widgetY);
        response.setWidgetZIndex(widgetZIndex);
        response.setWidth(width);
        response.setHeight(height);
        response.setId(id);
        response.setLastModificationDate(DateUtil.convertDateToString(new Date()));

        return response;
    }
}