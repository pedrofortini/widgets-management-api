package com.widgets.management.api.domain.widget;

import com.widgets.management.api.application.exception.ResourceNotFoundException;
import com.widgets.management.api.infrastructure.persistence.WidgetRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SearchWidgetServiceTest {

    private SearchWidgetService service;
    private WidgetRepository repository;

    @Before
    public void setUp() {
        this.repository = PowerMockito.mock(WidgetRepository.class);
        this.service = new SearchWidgetService(this.repository);
    }

    /********************** TESTS WIDGET SEARCH *****************************************************/

    @Test
    public void shouldReturn_WidgetWithCorrectData_WhenSearchingByIdExistingWidget(){

        Widget widget1 = this.createdWidgetWithParameters(0L, 0L, 4L, 5L, 2L, 1L);

        PowerMockito.when(this.repository.findById(1L)).thenReturn(Optional.of(widget1));

        Widget foundWidget = this.service.findWidgetById(1L);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(foundWidget.getId()).isEqualTo(1L);
        softly.assertThat(foundWidget.getWidgetX()).isEqualTo(0L);
        softly.assertThat(foundWidget.getWidgetY()).isEqualTo(0L);
        softly.assertThat(foundWidget.getWidth()).isEqualTo(4L);
        softly.assertThat(foundWidget.getHeight()).isEqualTo(5L);
        softly.assertThat(foundWidget.getWidgetZIndex()).isEqualTo(2L);
        softly.assertThat(foundWidget.getLastModificationDate()).isBeforeOrEqualsTo(new Date());

        softly.assertAll();
    }

    @Test
    public void shouldThrow_ResourceNotFoundException_WhenSearchingInexistentWidget(){

        PowerMockito.when(this.repository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.service.findWidgetById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Couldn't find Widget with id %s", 1L));
    }

    @Test
    public void shouldUse_GivenPageSizeAndPageNumber_WhenBothParametersAreNotNull(){

        this.service.findAllWidgetsSortedByZIndexPaginated(3L, 40L);

        ArgumentCaptor<Pageable> pageableArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(this.repository, times(1)).findAll(pageableArgument.capture());

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(pageableArgument.getValue().getPageNumber()).isEqualTo(3);
        softly.assertThat(pageableArgument.getValue().getPageSize()).isEqualTo(40);

        softly.assertAll();
    }

    @Test
    public void shouldReturn_CorrectPageDataSize_WhenThereAreValidWidgetsOnRepository(){

        Widget widget1 = this.createdWidgetWithParameters(0L, 0L, 4L, 5L, 2L, 1L);
        List<Widget> widgetList = Arrays.asList(new Widget[] {widget1});
        Pageable pageable = PageRequest.of(0, 10, Sort.by("widgetZIndex").ascending());

        Page<Widget> resultPage = new PageImpl<Widget>(widgetList , pageable, 1);

        PowerMockito.when(this.repository.findAll(any(Pageable.class))).thenReturn(resultPage);

        Page<Widget> actualResultPage = this.service.findAllWidgetsSortedByZIndexPaginated(0L, 10L);

        SoftAssertions softly = new SoftAssertions();

        // Pages must be of same size
        softly.assertThat(resultPage.getTotalElements()).isEqualTo(actualResultPage.getTotalElements());
        softly.assertThat(resultPage.getTotalPages()).isEqualTo(actualResultPage.getTotalElements());

        softly.assertAll();
    }

    /***********************************************************************************************/

    private Widget createdWidgetWithParameters(Long widgetX, Long widgetY,
                                               Long width,Long height,Long zIndex, Long id) {

        Widget result = new Widget(widgetX, widgetY, width, height);
        result.setId(id);
        result.setWidgetZIndex(zIndex);
        result.setLastModificationDate(new Date());

        return result;
    }
}