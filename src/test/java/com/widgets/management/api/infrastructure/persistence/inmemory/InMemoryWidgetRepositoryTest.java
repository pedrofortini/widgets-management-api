package com.widgets.management.api.infrastructure.persistence.inmemory;

import com.widgets.management.api.application.exception.PersistenceRepositoryException;
import com.widgets.management.api.domain.widget.Widget;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InMemoryWidgetRepositoryTest {

    private InMemoryWidgetRepository inMemoryWidgetRepository;
    private Widget firstSavedWidget;
    private Widget secondSavedWidget;
    private Widget updatedFirstWidget;
    private Widget updatedSecondWidget;

    @Before
    public void init()
    {
        this.inMemoryWidgetRepository = new InMemoryWidgetRepository();
        Widget firstWidget = new Widget(0L, 0L, 4L, 5L);
        firstWidget.setWidgetZIndex(1L);
        firstWidget.setLastModificationDate(new Date());
        this.firstSavedWidget = this.inMemoryWidgetRepository.save(firstWidget);

        Widget updateFirstWidget = new Widget(0L, 0L, 5L, 6L);
        updateFirstWidget.setWidgetZIndex(2L);
        updateFirstWidget.setLastModificationDate(new Date());
        updateFirstWidget.setId(1L);

        this.updatedFirstWidget = this.inMemoryWidgetRepository.save(updateFirstWidget);

        Widget secondWidget = new Widget(1L, 1L, 4L, 5L);
        secondWidget.setWidgetZIndex(3L);
        secondWidget.setLastModificationDate(new Date());
        this.secondSavedWidget = this.inMemoryWidgetRepository.save(secondWidget);

        Widget updateSecondWidget = new Widget(0L, 0L, 5L, 6L);
        updateSecondWidget.setWidgetZIndex(2L);
        updateSecondWidget.setLastModificationDate(new Date());
        updateSecondWidget.setId(2L);
        this.updatedSecondWidget = this.inMemoryWidgetRepository.save(updateSecondWidget);
    }

    @After
    public void teardown()
    {
        this.inMemoryWidgetRepository = null;
        this.firstSavedWidget = null;
        this.secondSavedWidget = null;
        this.updatedFirstWidget = null;
    }

    /********************** SAVING AND UPDATING  *****************************************************/

    @Test
    public void shouldReturn_WidgetInstanceWithCorrectData_WhenSavingNewWidget() {

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(this.firstSavedWidget.getId()).isEqualTo(1L);
        softly.assertThat(this.firstSavedWidget.getWidgetX()).isEqualTo(0L);
        softly.assertThat(this.firstSavedWidget.getWidgetY()).isEqualTo(0L);
        softly.assertThat(this.firstSavedWidget.getWidgetZIndex()).isEqualTo(1L);
        softly.assertThat(this.firstSavedWidget.getWidth()).isEqualTo(4L);
        softly.assertThat(this.firstSavedWidget.getHeight()).isEqualTo(5L);
        softly.assertThat(this.firstSavedWidget.getLastModificationDate()).isNotNull();
        softly.assertThat(this.firstSavedWidget.getLastModificationDate()).isBeforeOrEqualsTo(new Date());
        softly.assertAll();
    }

    @Test
    public void shouldReturn_WidgetInstanceWithCorrectUpdatedData_WhenUpdatingWidget(){

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(this.inMemoryWidgetRepository.count()).isEqualTo(2L);
        softly.assertThat(this.updatedFirstWidget.getId()).isEqualTo(1L);
        softly.assertThat(this.updatedFirstWidget.getWidgetX()).isEqualTo(0L);
        softly.assertThat(this.updatedFirstWidget.getWidgetY()).isEqualTo(0L);
        softly.assertThat(this.updatedFirstWidget.getWidgetZIndex()).isEqualTo(2L);
        softly.assertThat(this.updatedFirstWidget.getWidth()).isEqualTo(5L);
        softly.assertThat(this.updatedFirstWidget.getHeight()).isEqualTo(6L);
        softly.assertThat(this.updatedFirstWidget.getLastModificationDate()).isNotNull();
        softly.assertThat(this.updatedFirstWidget.getLastModificationDate()).isBeforeOrEqualsTo(new Date());
        softly.assertAll();
    }

    @Test
    public void shouldReturn_WidgetInstanceWithCorrectData_WhenCreatingSecondWidget(){

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(this.secondSavedWidget.getId()).isEqualTo(2L);
        softly.assertThat(this.secondSavedWidget.getWidgetX()).isEqualTo(1L);
        softly.assertThat(this.secondSavedWidget.getWidgetY()).isEqualTo(1L);
        softly.assertThat(this.secondSavedWidget.getWidgetZIndex()).isEqualTo(3L);
        softly.assertThat(this.secondSavedWidget.getWidth()).isEqualTo(4L);
        softly.assertThat(this.secondSavedWidget.getHeight()).isEqualTo(5L);
        softly.assertThat(this.secondSavedWidget.getLastModificationDate()).isNotNull();
        softly.assertThat(this.secondSavedWidget.getLastModificationDate()).isBeforeOrEqualsTo(new Date());
        softly.assertAll();
    }

    @Test
    public void shouldReturn_WidgetInstanceWithCorrectData_WhenUpdatingSecondWidget(){

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(this.updatedSecondWidget.getId()).isEqualTo(2L);
        softly.assertThat(this.updatedSecondWidget.getWidgetX()).isEqualTo(0L);
        softly.assertThat(this.updatedSecondWidget.getWidgetY()).isEqualTo(0L);
        softly.assertThat(this.updatedSecondWidget.getWidgetZIndex()).isEqualTo(2L);
        softly.assertThat(this.updatedSecondWidget.getWidth()).isEqualTo(5L);
        softly.assertThat(this.updatedSecondWidget.getHeight()).isEqualTo(6L);
        softly.assertThat(this.updatedSecondWidget.getLastModificationDate()).isNotNull();
        softly.assertThat(this.updatedSecondWidget.getLastModificationDate()).isBeforeOrEqualsTo(new Date());
        softly.assertAll();
    }

    @Test
    public void shouldThrow_PersistenceRepositoryException_WhenSavingNullWidget(){

        assertThatThrownBy(() -> this.inMemoryWidgetRepository.save(null))
                .isInstanceOf(PersistenceRepositoryException.class)
                .hasMessage("Internal error while persisting Widget entity");
    }

    @Test
    public void shouldThrow_PersistenceRepositoryException_WhenSavingWidgetWithEmptyData(){

        assertThatThrownBy(() -> this.inMemoryWidgetRepository.save(new Widget()))
                .isInstanceOf(PersistenceRepositoryException.class)
                .hasMessage("Internal error while persisting Widget entity");
    }

    @Test
    public void shouldThrow_PersistenceRepositoryException_WhenSavingWidgetWithInvalidData(){

        Widget widget = new Widget(0L, 0L, -4L, -5L);
        widget.setWidgetZIndex(2L);
        assertThatThrownBy(() -> this.inMemoryWidgetRepository.save(widget))
                .isInstanceOf(PersistenceRepositoryException.class)
                .hasMessage("Internal error while persisting Widget entity");
    }

    @Test
    public void shouldNotDelete_Widget_WhenIdForDeletionIsNull(){

        this.inMemoryWidgetRepository.deleteById(null);
        assertThat(
                this.inMemoryWidgetRepository.count()).
                isEqualTo(2L);
    }

    @Test
    public void shouldNotDelete_Widget_WhenIdForDeletionIsNotPresent(){

        this.inMemoryWidgetRepository.deleteById(5L);
        assertThat(
                this.inMemoryWidgetRepository.count()).
                isEqualTo(2L);
    }

    @Test
    public void shouldDelete_Widget_WhenIdForDeletionIsPresent(){

        this.inMemoryWidgetRepository.deleteById(1L);
        assertThat(
                this.inMemoryWidgetRepository.count()).
                isEqualTo(1L);
    }

    /*************************************************************************************************/

    /********************** FINDING WIDGET DATA *****************************************/

    @Test
    public void shouldReturn_WidgetInstanceWithCorrectExpectedData_WhenSearchingExistingWidget(){

        Optional<Widget> foundWidget = this.inMemoryWidgetRepository.findById(1L);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(foundWidget.isPresent()).isTrue();
        softly.assertThat(foundWidget.get().getId()).isEqualTo(1L);
        softly.assertThat(foundWidget.get().getWidgetX()).isEqualTo(0L);
        softly.assertThat(foundWidget.get().getWidgetY()).isEqualTo(0L);
        softly.assertThat(foundWidget.get().getWidgetZIndex()).isEqualTo(2L);
        softly.assertThat(foundWidget.get().getWidth()).isEqualTo(5L);
        softly.assertThat(foundWidget.get().getHeight()).isEqualTo(6L);
        softly.assertThat(foundWidget.get().getLastModificationDate()).isNotNull();
        softly.assertThat(foundWidget.get().getLastModificationDate()).isBeforeOrEqualsTo(new Date());
        softly.assertAll();
    }

    @Test
    public void shouldReturn_EmptyOptinalWidget_WhenSearchingInexistentWidgetId(){

        Optional<Widget> foundWidget = this.inMemoryWidgetRepository.findById(4L);
        assertThat(foundWidget.isPresent()).isFalse();
    }

    @Test
    public void shouldReturn_EmptyOptinalWidget_WhenSearchingNullWidgetId(){

        Optional<Widget> foundWidget = this.inMemoryWidgetRepository.findById(null);
        assertThat(foundWidget.isPresent()).isFalse();
    }

    @Test
    public void shouldReturn_ListWithOneWidgetWhoseZIndexIsGreaterThanOrEqualParameter(){

        List<Widget> widgetsGreaterZIndex = this.inMemoryWidgetRepository.findByWidgetZIndexGreaterThanEqualOrderByWidgetZIndexAsc(2L);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(widgetsGreaterZIndex.size()).isEqualTo(1L);

        // Test first Widget Data
        softly.assertThat(widgetsGreaterZIndex.get(0).getId()).isEqualTo(2L);
        softly.assertThat(widgetsGreaterZIndex.get(0).getWidgetX()).isEqualTo(0L);
        softly.assertThat(widgetsGreaterZIndex.get(0).getWidgetY()).isEqualTo(0L);
        softly.assertThat(widgetsGreaterZIndex.get(0).getWidgetZIndex()).isEqualTo(2L);
        softly.assertThat(widgetsGreaterZIndex.get(0).getWidth()).isEqualTo(5L);
        softly.assertThat(widgetsGreaterZIndex.get(0).getHeight()).isEqualTo(6L);
        softly.assertThat(widgetsGreaterZIndex.get(0).getLastModificationDate()).isNotNull();

        softly.assertAll();
    }

    @Test
    public void shouldReturn_EmptyList_WhenParameterToFunctionObtainingWidgetsWithZIndexGreaterThanIsNull(){
        assertThat(
                inMemoryWidgetRepository.findByWidgetZIndexGreaterThanEqualOrderByWidgetZIndexAsc(null).size()).
                isEqualTo(0);
    }

    @Test
    public void shouldReturn_EmptyPage_WhenPageableParameterIsNull(){

        assertThat(
                inMemoryWidgetRepository.findAll(null).getTotalElements()).
                isEqualTo(0);
    }

    @Test
    public void shouldReturn_PageWithAllRegisteredWidgets_WhenPageableParameterIsValid(){

        Pageable simplePageable = PageRequest.of(0, 10);
        Page<Widget> page = inMemoryWidgetRepository.findAll(simplePageable);

        SoftAssertions softly = new SoftAssertions();

        // Must return all Widgets on Repository
        softly.assertThat(page.getTotalElements()).isEqualTo(1L);
        softly.assertThat(page.getTotalPages()).isEqualTo(1L);

        // Page must be with correct Data
        softly.assertThat(page.get().findAny().get().getId()).isEqualTo(this.updatedSecondWidget.getId());
        softly.assertThat(page.get().findAny().get().getWidgetX()).isEqualTo(this.updatedSecondWidget.getWidgetX());
        softly.assertThat(page.get().findAny().get().getWidgetY()).isEqualTo(this.updatedSecondWidget.getWidgetY());
        softly.assertThat(page.get().findAny().get().getWidgetZIndex()).isEqualTo(this.updatedSecondWidget.getWidgetZIndex());
        softly.assertThat(page.get().findAny().get().getWidth()).isEqualTo(this.updatedSecondWidget.getWidth());
        softly.assertThat(page.get().findAny().get().getHeight()).isEqualTo(this.updatedSecondWidget.getHeight());
        softly.assertThat(page.get().findAny().get().getLastModificationDate()).isEqualTo(this.updatedSecondWidget.getLastModificationDate());

        softly.assertAll();
    }

    @Test
    public void shouldReturn_0AsMaxValue_WhenWidgetsByZvalueSetIsEmpty(){

        InMemoryWidgetRepository inMemoryWidgetRepositoryTest = new InMemoryWidgetRepository();
        assertThat(
                inMemoryWidgetRepositoryTest.maxZIndex()).
                isEqualTo(0L);
    }

    @Test
    public void shouldReturn_MaxValueAs2_WhenWidgetsByZvalueSetIsNotEmpty(){

        assertThat(
                this.inMemoryWidgetRepository.maxZIndex()).
                isEqualTo(2L);
    }


    /*************************************************************************************************/
}