package com.widgets.management.api.infrastructure.persistence.inmemory;

import com.widgets.management.api.application.MessageConstants;
import com.widgets.management.api.application.exception.PersistenceRepositoryException;
import com.widgets.management.api.application.exception.WidgetPersistenceException;
import com.widgets.management.api.application.util.DateUtil;
import com.widgets.management.api.domain.widget.Widget;
import com.widgets.management.api.domain.widget.comparator.WidgetComparator;
import com.widgets.management.api.infrastructure.persistence.WidgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Repository
@Profile("memory")
public class InMemoryWidgetRepository implements WidgetRepository {

    Logger logger = LoggerFactory.getLogger(InMemoryWidgetRepository.class);

    /********************** IN-MEMORY STORAGE DATA STRUCTURES  *****************************************************/

    // Stores Widgets keyed by id
    private Map<Long, Widget> widgetsMap;

    // Stores Widgets sorted by Z-index
    // ConcurrentSkipListSet is synchronized ensuring thread safety
    private ConcurrentSkipListSet<Widget> widgetsByZvalue;

    // Keeps track of all sequential Widget ids
    private ConcurrentSkipListSet<Long> widgetIds;

    /***************************************************************************************************************/

    public InMemoryWidgetRepository() {

        // Used ConcurrentHashMap ensuring thread safety
        this.widgetsMap = new ConcurrentHashMap<>();
        this.widgetsByZvalue = new ConcurrentSkipListSet<>(new WidgetComparator());
        this.widgetIds = new ConcurrentSkipListSet();
    }

    /********************** SAVING, UPDATING And DELETING  *****************************************************/

    // Implements saveOrUpdate logic, replacing Widget data if already exists
    @Override
    public Widget save(Widget widget) throws WidgetPersistenceException
    {
        if(!this.checkValidWidgetData(widget))
        {
            if(widget != null)
            {
                logger.error(String.format(MessageConstants.LOG_MESSAGE_ERROR_REPOSITORY, widget.getId(),
                        widget.getWidgetX(), widget.getWidgetY(), widget.getWidgetZIndex(), widget.getWidth(),
                        widget.getHeight(), DateUtil.convertDateToString(widget.getLastModificationDate())));
            }
            throw new PersistenceRepositoryException(MessageConstants.MESSAGE_ERROR_PERSISTENCE_LAYER);
        }

        // Implements logic for saving new Widgets
        if(widget.getId() == null || !this.widgetsMap.containsKey(widget.getId())) {
            this.createWidget(widget);
        }

        // Updates widget data on map and keeps widgetsByZvalue set updated
        else {
            Widget lastWidgetValueOnMap = this.widgetsMap.get(widget.getId());

            // Removes Widget being updated with it's latest Z-index from widgetsByZvalue set
            this.widgetsByZvalue.remove(lastWidgetValueOnMap);

            // Removes Widget on widgetsByZvalue with Z-index equal to Widget's current Z-index
            this.widgetsByZvalue.remove(widget);

            // Adds new updated Z-index Widget object on widgetsByZvalue set
            this.widgetsByZvalue.add(widget);
            this.widgetsMap.put(widget.getId(), widget);
        }

        return widget;
    }

    private void createWidget(Widget widget)
    {
        // If it's the first Widget being created, the id is 1
        Long newWidgetId = 1L;

        // If Widgets were created in the past
        if (!widgetIds.isEmpty()) {
            // New Widget must be last id given to a Widget plus 1
            newWidgetId = this.widgetIds.last() + 1;
        }

        // Adds id of new Widget to widgetIds set
        this.widgetIds.add(newWidgetId);

        widget.setId(newWidgetId);

        // Stores new Widget with its generated id on map
        this.widgetsMap.put(newWidgetId, widget);

        // Add new Widget to sorted by Z-index set

        // Ensures that most updated Widget data is added to widgetsByZvalue
        this.widgetsByZvalue.remove(widget);
        this.widgetsByZvalue.add(widget);
    }

    // Checks that given Widget contains non-null and valid data
    private boolean checkValidWidgetData(Widget widget)
    {
        if(widget == null) {
            return false;
        }

        if(widget.getWidgetX() == null ||
           widget.getWidgetY() == null ||
           widget.getWidgetZIndex() == null ||
           widget.getHeight() == null ||
           widget.getWidth() == null) {
            return false;
        }

        if(widget.getHeight() <= 0 || widget.getWidth() <= 0) {
            return false;
        }
        return true;
    }

    // Returns the number of deleted entities
    @Override
    public void deleteById(Long id)
    {
        if(id == null || !this.widgetsMap.containsKey(id)) {
            return;
        }

        Widget widgetToBeRemoved = this.widgetsMap.get(id);
        this.widgetsByZvalue.remove(widgetToBeRemoved);
        this.widgetsMap.remove(id);
    }

    /*************************************************************************************************/

    /********************** FINDING AND COUNTING WIDGET DATA *****************************************/

    @Override
    public Optional<Widget> findById(Long id)
    {
        if(id == null || !this.widgetsMap.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(this.widgetsMap.get(id));
    }

    @Override
    public long count()
    {
        return this.widgetsMap.size();
    }

    // Return a list of Widgets whose Z-index is greater than or equal the parameter
    // The list is returned in ascending order
    @Override
    public List<Widget> findByWidgetZIndexGreaterThanEqualOrderByWidgetZIndexAsc(Long widgetZIndex)
    {
        if(widgetZIndex == null) {
            return new ArrayList<>();
        }

        Widget pivotWidget = new Widget();
        pivotWidget.setWidgetZIndex(widgetZIndex);

        return new ArrayList<>(this.widgetsByZvalue.tailSet(pivotWidget));
    }

    @Override
    public Long maxZIndex()
    {

        if(this.widgetsByZvalue.isEmpty()) {
            return 0L;
        }
        return this.widgetsByZvalue.last().getWidgetZIndex();
    }

    @Override
    public Page<Widget> findAll(Pageable pageable)
    {
        if(Objects.isNull(pageable)) {
            return new PageImpl<>(new ArrayList<>());
        }

        Pageable simplePageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        List<Widget> widgetsSortedByZvalue = new ArrayList<>(this.widgetsByZvalue);

        return new PageImpl<>(getPaginatedList(widgetsSortedByZvalue, pageable.getPageNumber(), pageable.getPageSize()),
                simplePageable, widgetsSortedByZvalue.size());
    }

    private List<Widget> getPaginatedList(List<Widget> sourceList, int page, int pageSize) {

        int fromIndex = page * pageSize;
        if(sourceList == null || sourceList.size() < fromIndex){
            return Collections.emptyList();
        }

        // ToIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }

    /*************************************************************************************************/
}
