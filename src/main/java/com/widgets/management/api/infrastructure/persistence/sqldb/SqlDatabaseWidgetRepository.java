package com.widgets.management.api.infrastructure.persistence.sqldb;

import com.widgets.management.api.domain.widget.Widget;
import com.widgets.management.api.infrastructure.persistence.WidgetRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("sql")
public interface SqlDatabaseWidgetRepository extends JpaRepository<Widget, Long>, WidgetRepository {

    // Return a list of Widgets whose Z-index is greater than or equal the parameter
    // The list is returned in ascending order
    List<Widget> findByWidgetZIndexGreaterThanEqualOrderByWidgetZIndexAsc(Long widgetZIndex);

    @Query(value = "SELECT max(widgetZIndex) FROM Widget")
    Long maxZIndex();

    Page<Widget> findAll(Pageable pageable);
}
