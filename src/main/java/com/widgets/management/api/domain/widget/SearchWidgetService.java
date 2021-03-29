package com.widgets.management.api.domain.widget;

import com.widgets.management.api.application.MessageConstants;
import com.widgets.management.api.application.exception.ResourceNotFoundException;
import com.widgets.management.api.infrastructure.persistence.WidgetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SearchWidgetService {

    private WidgetRepository widgetRepository;

    public SearchWidgetService(WidgetRepository widgetRepository){
        this.widgetRepository = widgetRepository;
    }

    public Widget findWidgetById(Long id) {

        Optional<Widget> widgetOptional = this.widgetRepository.findById(id);

        if(!widgetOptional.isPresent()) {
            throw new ResourceNotFoundException(String.format(MessageConstants.MESSAGE_WIDGET_NOT_FOUND, id));
        }
        return widgetOptional.get();
    }

    public Page<Widget> findAllWidgetsSortedByZIndexPaginated(Long currentPage, Long pageSize) {

        // Creates Pageable object, defining sorting by Z-index
        Pageable pageable = PageRequest.of(currentPage.intValue(),
                pageSize.intValue(), Sort.by("widgetZIndex").ascending());
        return this.widgetRepository.findAll(pageable);
    }
}
