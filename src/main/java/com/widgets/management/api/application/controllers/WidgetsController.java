package com.widgets.management.api.application.controllers;

import com.widgets.management.api.application.MessageConstants;
import com.widgets.management.api.application.ServiceConstants;
import com.widgets.management.api.application.exception.InvalidRequestException;
import com.widgets.management.api.domain.widget.PersistWidgetService;
import com.widgets.management.api.domain.widget.SearchWidgetService;
import com.widgets.management.api.domain.widget.Widget;
import com.widgets.management.api.domain.widget.converter.WidgetRequestConverter;
import com.widgets.management.api.domain.widget.converter.WidgetResponseConverter;
import io.swagger.api.WidgetsApi;
import io.swagger.model.WidgetCreateRequest;
import io.swagger.model.WidgetResponse;
import io.swagger.model.WidgetUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class WidgetsController implements WidgetsApi {

    private PersistWidgetService persistWidgetService;
    private SearchWidgetService searchWidgetService;

    private WidgetRequestConverter requestConverter;
    private WidgetResponseConverter responseConverter;


    public WidgetsController(PersistWidgetService persistWidgetService,
                             SearchWidgetService searchWidgetService,
                             WidgetRequestConverter requestConverter,
                             WidgetResponseConverter responseConverter) {
        this.persistWidgetService = persistWidgetService;
        this.searchWidgetService = searchWidgetService;
        this.requestConverter = requestConverter;
        this.responseConverter = responseConverter;
    }

    @Override
    public ResponseEntity<WidgetResponse> createWidget(@Valid @RequestBody WidgetCreateRequest widget) {

        Widget widgetToSave = this.requestConverter.convertCreateRequest(widget);
        Widget savedWidget = this.persistWidgetService.saveWidget(widgetToSave, true);
        WidgetResponse response = this.responseConverter.convert(savedWidget);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteWidgetById( @Min(value = 0) @PathVariable("id") Long id) {

        this.persistWidgetService.deleteWidgetById(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<WidgetResponse> getWidgetById( @Min(value = 0) @PathVariable("id") Long id) {

        Widget foundWidget = this.searchWidgetService.findWidgetById(id);
        WidgetResponse response = this.responseConverter.convert(foundWidget);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<WidgetResponse>> getWidgets(@Min(0) @RequestParam(value = "currentPage", required = false, defaultValue="0") Long currentPage,
                                                           @Min(1) @Max(500) @RequestParam(value = "pageSize", required = false, defaultValue="10") Long pageSize) {

        if(currentPage < ServiceConstants.MIN_CURRENTPAGE ||
                (pageSize < ServiceConstants.MIN_PAGESIZE || pageSize > ServiceConstants.MAX_PAGESIZE)) {
            throw new InvalidRequestException(MessageConstants.MESSAGE_INVALID_GET_REQUEST);
        }

        Page<Widget> widgetsOnDB = this.searchWidgetService.findAllWidgetsSortedByZIndexPaginated(currentPage, pageSize);
        List<WidgetResponse> responses = this.responseConverter.convert(widgetsOnDB);

        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<WidgetResponse> updateWidget( @Min(value = 0) @PathVariable("id") Long id,
                                                        @Valid @RequestBody WidgetUpdateRequest widget) {

        Widget widgetToUpdate = this.requestConverter.convertUpdateRequest(widget, id);
        Widget updatedWidget = this.persistWidgetService.updateWidget(widgetToUpdate);
        WidgetResponse response = this.responseConverter.convert(updatedWidget);

        return ResponseEntity.ok(response);
    }
}
