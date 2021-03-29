package com.widgets.management.api.domain.widget;

import com.widgets.management.api.application.MessageConstants;
import com.widgets.management.api.application.exception.WidgetPersistenceException;
import com.widgets.management.api.domain.widget.comparator.WidgetComparator;
import com.widgets.management.api.infrastructure.persistence.WidgetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class PersistWidgetService {

    private WidgetRepository widgetRepository;
    private SearchWidgetService searchService;

    public PersistWidgetService(WidgetRepository widgetRepository, SearchWidgetService searchWidgetService) {
        this.widgetRepository = widgetRepository;
        this.searchService = searchWidgetService;
    }

    @Transactional
    public void deleteWidgetById(Long id)
    {
        // Tries to find Widget first, If not Found, throws NotFound Exception
        this.searchService.findWidgetById(id);
        try
        {
            this.widgetRepository.deleteById(id);
        }
        catch (Exception e)
        {
            throw new WidgetPersistenceException(MessageConstants.MESSAGE_ERROR_DELETING_WIDGET_DATA);
        }
    }

    @Transactional
    public Widget updateWidget(Widget widget)
    {
        Widget widgetOnDB = this.searchService.findWidgetById(widget.getId());

        // Only apply shift logic when updating if Z-index was changed
        boolean shouldApplyShiftLogic = false;

        // Only calls save method if any field is updated
        boolean anyFieldUpdated = false;

        // Checks if any Widget field must be updated
        if(widget.getWidgetX() != null && !widget.getWidgetX().equals(widgetOnDB.getWidgetX()))
        {
            anyFieldUpdated = true;
            widgetOnDB.setWidgetX(widget.getWidgetX());
        }
        if(widget.getWidgetY() != null && !widget.getWidgetY().equals(widgetOnDB.getWidgetY()))
        {
            anyFieldUpdated = true;
            widgetOnDB.setWidgetY(widget.getWidgetY());
        }
        if(widget.getWidgetZIndex() != null && !widget.getWidgetZIndex().equals(widgetOnDB.getWidgetZIndex()))
        {
            anyFieldUpdated = true;
            shouldApplyShiftLogic = true;
            widgetOnDB.setWidgetZIndex(widget.getWidgetZIndex());
        }
        if(widget.getWidth() != null && !widget.getWidth().equals(widgetOnDB.getWidth()))
        {
            anyFieldUpdated = true;
            widgetOnDB.setWidth(widget.getWidth());
        }
        if(widget.getHeight() != null && !widget.getHeight().equals(widgetOnDB.getHeight()))
        {
            anyFieldUpdated = true;
            widgetOnDB.setHeight(widget.getHeight());
        }

        // If any field was updated, return saved Widget data
        if(anyFieldUpdated) {
            return this.saveWidget(widgetOnDB, shouldApplyShiftLogic);
        }

        // If there are no updates on Widget, return data already on persisted
        return widgetOnDB;
    }

    @Transactional
    public Widget saveWidget(Widget widget, boolean shouldApplyShiftLogic)
    {
        try
        {
            // If Z-index is null, move Widget to foreground (maximum Z-index seen so far)
            if(widget.getWidgetZIndex() == null)
            {
                Long maxZIndex = 0L;
                Long repositoryMax = this.widgetRepository.maxZIndex();
                if(repositoryMax != null) {
                    maxZIndex = repositoryMax;
                }
                widget.setWidgetZIndex(maxZIndex + 1);
            }
            widget.setLastModificationDate(new Date());

            if(shouldApplyShiftLogic)
            {
                List<Widget> widgetsToShift = getWidgetsToBeShifted(widget);
                widgetsToShift.stream().map(w -> this.widgetRepository.save(w));
            }
            return this.widgetRepository.save(widget);
        }
        catch (Exception e)
        {
            throw new WidgetPersistenceException(MessageConstants.MESSAGE_ERROR_PERSISTING_WIDGET_DATA);
        }
    }

    public List<Widget> getWidgetsToBeShifted(Widget widget)
    {
        List<Widget> widgetsGreaterThan = this.
                widgetRepository.findByWidgetZIndexGreaterThanEqualOrderByWidgetZIndexAsc(widget.getWidgetZIndex());

        // Removes widget being updated from analysis list
        if(widgetsGreaterThan.contains(widget)){
            widgetsGreaterThan.remove(widget);
        }

        // Checks if any shifting operation is needed
        // That happens if new Widget conflicts with existing one
        if(widgetsGreaterThan.isEmpty() ||
                !widget.getWidgetZIndex().equals(widgetsGreaterThan.get(0).getWidgetZIndex())) {

            return new ArrayList<>();
        }

        // Keep Widgets to update, ordered by Z-index value
        // ensuring widgets on set are unique
        ConcurrentSkipListSet<Widget> widgetsToUpdate = new ConcurrentSkipListSet<>(new WidgetComparator());

        // First Widget with Z-index on widgetsGreaterThan list already conflicted with the one being inserted
        Widget currentWidget = widgetsGreaterThan.get(0);
        currentWidget.setWidgetZIndex(currentWidget.getWidgetZIndex() + 1);

        // Add it to update set
        widgetsToUpdate.add(currentWidget);

        int size = widgetsGreaterThan.size();

        if(size >= 2)
        {
            // Iterates through widgetsGreaterThan list in pairs,
            // checking weather there's a conflict between current and next Widget
            // Adding next Widget to the update set if needed
            Widget nextWidget = widgetsGreaterThan.get(1);
            int i = 1;
            while(i < size && (currentWidget.getWidgetZIndex().equals(nextWidget.getWidgetZIndex())))
            {
                nextWidget.setWidgetZIndex(nextWidget.getWidgetZIndex() + 1);
                widgetsToUpdate.add(nextWidget);

                currentWidget = nextWidget;
                i++;
                if(i < size) {
                    nextWidget = widgetsGreaterThan.get(i);
                }
            }
        }
        return new ArrayList<>(widgetsToUpdate);
    }
}
