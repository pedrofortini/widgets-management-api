package com.widgets.management.api.infrastructure.persistence;

import com.widgets.management.api.domain.widget.Widget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface WidgetRepository {

    Widget save(Widget widget);

    Optional<Widget> findById(Long id);

    long count();

    // Return a list of Widgets whose Z-index is greater than or equal the parameter
    // The list is returned in ascending order
    List<Widget> findByWidgetZIndexGreaterThanEqualOrderByWidgetZIndexAsc(Long widgetZIndex);

    Long maxZIndex();

    // Returns the number of deleted entities
    void deleteById(Long id);

    Page<Widget> findAll(Pageable pageable);
}
