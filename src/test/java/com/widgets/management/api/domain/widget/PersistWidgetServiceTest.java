package com.widgets.management.api.domain.widget;

import com.widgets.management.api.application.exception.ResourceNotFoundException;
import com.widgets.management.api.application.exception.WidgetPersistenceException;
import com.widgets.management.api.infrastructure.persistence.WidgetRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PersistWidgetServiceTest {

    private PersistWidgetService service;
    private SearchWidgetService searchService;
    private WidgetRepository repository;

    @Before
    public void setUp() {
        this.repository = PowerMockito.mock(WidgetRepository.class);
        this.searchService = PowerMockito.mock(SearchWidgetService.class);
        this.service = new PersistWidgetService(this.repository, this.searchService);
    }

    /********************** TESTS WIDGET SHIFT LOGIC  *****************************************************/

    // Adding Widget with Z-Index = 2 when widget Z-Index list is: 2,3
    @Test
    public void shouldUse_WidgetListWithZIndexValues3And4AsArgumentToSaveAll_WhenSavingWidgetWithZIndex2OnListThatItAlreadyExisted(){

        Widget widget2 = this.createWidgetWithParameters(0L, 0L, 4L, 5L, 2L, 1L);
        Widget widget3 = this.createWidgetWithParameters(0L, 0L, 5L, 6L, 3L, 2L);
        Widget newWidget = this.createWidgetWithParameters(1L, 1L, 4L, 5L, 2L, 3L);

        List<Widget> widgetsZValuesAlreadyCreated = Arrays.asList(new Widget[]{widget2, widget3});

        PowerMockito.when(this.repository.findByWidgetZIndexGreaterThanEqualOrderByWidgetZIndexAsc(2L))
                .thenReturn(widgetsZValuesAlreadyCreated);

        List<Widget> widgetsToBeShifted = this.service.getWidgetsToBeShifted(newWidget);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(widgetsToBeShifted.size()).isEqualTo(2L);

        // Widget 2 (with id 1) must be updated to Z-index 3
        softly.assertThat(widgetsToBeShifted.get(0).getId()).isEqualTo(1L);
        softly.assertThat(widgetsToBeShifted.get(0).getWidgetZIndex()).isEqualTo(3L);

        // Widget 3 (with id 2) must be updated to Z-index 4
        softly.assertThat(widgetsToBeShifted.get(1).getId()).isEqualTo(2L);
        softly.assertThat(widgetsToBeShifted.get(1).getWidgetZIndex()).isEqualTo(4L);

        softly.assertAll();
    }

    // Adding Widget with Z-Index = 2 when widget Z-Index list is: 2,4
    @Test
    public void shouldUse_WidgetListWithZIndexValue3AsArgumentToSaveAll_WhenSavingWidgetWithZIndex2OnListThatItAlreadyExisted(){

        Widget widget2 = this.createWidgetWithParameters(0L, 0L, 4L, 5L, 2L, 1L);
        Widget widget4 = this.createWidgetWithParameters(0L, 0L, 5L, 6L, 4L, 2L);
        Widget newWidget = this.createWidgetWithParameters(1L, 1L, 4L, 5L, 2L, 3L);

        List<Widget> widgetsZValuesAlreadyCreated = Arrays.asList(new Widget[]{widget2, widget4});

        PowerMockito.when(this.repository.findByWidgetZIndexGreaterThanEqualOrderByWidgetZIndexAsc(2L))
                .thenReturn(widgetsZValuesAlreadyCreated);

        List<Widget> widgetsToBeShifted = this.service.getWidgetsToBeShifted(newWidget);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(widgetsToBeShifted.size()).isEqualTo(1L);

        // Widget 2 (with id 1) must be updated to Z-index 3
        softly.assertThat(widgetsToBeShifted.get(0).getId()).isEqualTo(1L);
        softly.assertThat(widgetsToBeShifted.get(0).getWidgetZIndex()).isEqualTo(3L);

        softly.assertAll();
    }

    // Adding Widget with Z-Index = 2 when widget Z-Index list is: 2,3,4,8
    @Test
    public void shouldUse_WidgetListWithZIndexValues3And4And5AsArgumentToSaveAll_WhenSavingWidgetWithZIndex2OnListThatItAlreadyExisted(){

        Widget widget2 = this.createWidgetWithParameters(0L, 0L, 4L, 5L, 2L, 1L);
        Widget widget3 = this.createWidgetWithParameters(0L, 0L, 5L, 6L, 3L, 2L);
        Widget widget4 = this.createWidgetWithParameters(1L, 2L, 5L, 6L, 4L, 3L);
        Widget widget8 = this.createWidgetWithParameters(1L, 5L, 5L, 6L, 8L, 4L);
        Widget newWidget = this.createWidgetWithParameters(1L, 1L, 4L, 5L, 2L, 5L);

        List<Widget> widgetsZValuesAlreadyCreated = Arrays.asList(new Widget[]{widget2, widget3, widget4, widget8});

        PowerMockito.when(this.repository.findByWidgetZIndexGreaterThanEqualOrderByWidgetZIndexAsc(2L))
                .thenReturn(widgetsZValuesAlreadyCreated);

        List<Widget> widgetsToBeShifted = this.service.getWidgetsToBeShifted(newWidget);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(widgetsToBeShifted.size()).isEqualTo(3L);

        // Widget 2 (with id 1) must be updated to Z-index 3
        softly.assertThat(widgetsToBeShifted.get(0).getId()).isEqualTo(1L);
        softly.assertThat(widgetsToBeShifted.get(0).getWidgetZIndex()).isEqualTo(3L);

        // Widget 3 (with id 2) must be updated to Z-index 4
        softly.assertThat(widgetsToBeShifted.get(1).getId()).isEqualTo(2L);
        softly.assertThat(widgetsToBeShifted.get(1).getWidgetZIndex()).isEqualTo(4L);

        // Widget 4 (with id 3) must be updated to Z-index 5
        softly.assertThat(widgetsToBeShifted.get(2).getId()).isEqualTo(3L);
        softly.assertThat(widgetsToBeShifted.get(2).getWidgetZIndex()).isEqualTo(5L);

        softly.assertAll();
    }

    // Adding Widget with Z-Index = 2 when widget Z-Index list is: 3
    @Test
    public void shouldUse_EmptyListAsArgumentToSaveAll_WhenSavingWidgetWithZIndex2OnListThatItDoeNotExisted(){

        Widget widget3 = this.createWidgetWithParameters(0L, 0L, 5L, 6L, 3L, 2L);
        Widget newWidget = this.createWidgetWithParameters(1L, 1L, 4L, 5L, 2L, 5L);

        List<Widget> widgetsZValuesAlreadyCreated = Arrays.asList(new Widget[]{widget3});

        PowerMockito.when(this.repository.findByWidgetZIndexGreaterThanEqualOrderByWidgetZIndexAsc(2L))
                .thenReturn(widgetsZValuesAlreadyCreated);

        List<Widget> widgetsToBeShifted = this.service.getWidgetsToBeShifted(newWidget);

        assertThat(widgetsToBeShifted.size()).isEqualTo(0L);
    }

    // Adding Widget with Z-Index = 2 when widget Z-Index list is empty
    @Test
    public void shouldUse_EmptyListAsArgumentToSaveAll_WhenSavingWidgetWithZIndex2OnEmptyList(){

        Widget newWidget = this.createWidgetWithParameters(1L, 1L, 4L, 5L, 2L, 5L);

        PowerMockito.when(this.repository.findByWidgetZIndexGreaterThanEqualOrderByWidgetZIndexAsc(2L))
                .thenReturn(new ArrayList<>());

        this.service.saveWidget(newWidget, true);

        List<Widget> widgetsToBeShifted = this.service.getWidgetsToBeShifted(newWidget);
        assertThat(widgetsToBeShifted.size()).isEqualTo(0L);
    }

    /******************************************************************************************************/

    /********************** TESTS SAVING, UPDATING AND DELETING *******************************************/

    @Test
    public void shouldReturnAndUseAsArgumentToSave_WidgetWithMaxZIndex_WhenSavingWidgetWithNullZIndex() {

        Long maxZIndex = 2L;
        PowerMockito.when(this.repository.maxZIndex()).thenReturn(maxZIndex);

        Widget newWidget = this.createWidgetWithParameters(1L, 1L, 4L, 5L, null, null);
        Widget persistedNewWidget = this.createWidgetWithParameters(1L, 1L, 4L, 5L, (maxZIndex + 1), 1L);

        PowerMockito.when(this.repository.save(any(Widget.class))).thenReturn(persistedNewWidget);

        Widget returnedWidget = this.service.saveWidget(newWidget, true);

        ArgumentCaptor<Widget> saveArgument = ArgumentCaptor.forClass(Widget.class);
        verify(this.repository, times(1)).save(saveArgument.capture());

        SoftAssertions softly = new SoftAssertions();

        // Tests that returned value is the one persisted by repository save method
        softly.assertThat(returnedWidget.getId()).isEqualTo(persistedNewWidget.getId());
        softly.assertThat(returnedWidget.getWidgetX()).isEqualTo(persistedNewWidget.getWidgetX());
        softly.assertThat(returnedWidget.getWidgetY()).isEqualTo(persistedNewWidget.getWidgetY());
        softly.assertThat(returnedWidget.getWidgetZIndex()).isEqualTo(persistedNewWidget.getWidgetZIndex());
        softly.assertThat(returnedWidget.getWidth()).isEqualTo(persistedNewWidget.getWidth());
        softly.assertThat(returnedWidget.getHeight()).isEqualTo(persistedNewWidget.getHeight());
        softly.assertThat(returnedWidget.getLastModificationDate()).isEqualTo(persistedNewWidget.getLastModificationDate());

        // Tests that Widget used as parameter to repository save has correct data
        softly.assertThat(saveArgument.getValue().getWidgetX()).isEqualTo(newWidget.getWidgetX());
        softly.assertThat(saveArgument.getValue().getWidgetY()).isEqualTo(newWidget.getWidgetY());
        softly.assertThat(saveArgument.getValue().getWidgetZIndex()).isEqualTo(maxZIndex + 1);
        softly.assertThat(saveArgument.getValue().getWidth()).isEqualTo(newWidget.getWidth());
        softly.assertThat(saveArgument.getValue().getHeight()).isEqualTo(newWidget.getHeight());

        softly.assertAll();
    }

    @Test
    public void shouldReturnAndUseAsArgumentToSave_WidgetWithCorrectData_WhenSavingWidgetWithNonNullZIndex() {

        Widget newWidget = this.createWidgetWithParameters(1L, 1L, 4L, 5L, 2L, null);
        Widget persistedNewWidget = this.createWidgetWithParameters(1L, 1L, 4L, 5L, 2L, 1L);

        PowerMockito.when(this.repository.save(any(Widget.class))).thenReturn(persistedNewWidget);

        Widget returnedWidget = this.service.saveWidget(newWidget, true);

        ArgumentCaptor<Widget> saveArgument = ArgumentCaptor.forClass(Widget.class);
        verify(this.repository, times(1)).save(saveArgument.capture());

        SoftAssertions softly = new SoftAssertions();

        // Tests that returned value is the one persisted by repository save method
        softly.assertThat(returnedWidget.getId()).isEqualTo(persistedNewWidget.getId());
        softly.assertThat(returnedWidget.getWidgetX()).isEqualTo(persistedNewWidget.getWidgetX());
        softly.assertThat(returnedWidget.getWidgetY()).isEqualTo(persistedNewWidget.getWidgetY());
        softly.assertThat(returnedWidget.getWidgetZIndex()).isEqualTo(persistedNewWidget.getWidgetZIndex());
        softly.assertThat(returnedWidget.getWidth()).isEqualTo(persistedNewWidget.getWidth());
        softly.assertThat(returnedWidget.getHeight()).isEqualTo(persistedNewWidget.getHeight());
        softly.assertThat(returnedWidget.getLastModificationDate()).isEqualTo(persistedNewWidget.getLastModificationDate());

        // Tests that Widget used as parameter to repository save has correct data
        softly.assertThat(saveArgument.getValue().getWidgetX()).isEqualTo(newWidget.getWidgetX());
        softly.assertThat(saveArgument.getValue().getWidgetY()).isEqualTo(newWidget.getWidgetY());
        softly.assertThat(saveArgument.getValue().getWidgetZIndex()).isEqualTo(newWidget.getWidgetZIndex());
        softly.assertThat(saveArgument.getValue().getWidth()).isEqualTo(newWidget.getWidth());
        softly.assertThat(saveArgument.getValue().getHeight()).isEqualTo(newWidget.getHeight());

        softly.assertAll();
    }

    @Test
    public void shouldThrow_WidgetPersistenceException_WhenExceptionOccursOnRepository(){

        PowerMockito.when(this.repository.save(any(Widget.class))).thenThrow(new RuntimeException());

        assertThatThrownBy(() -> this.service.saveWidget(new Widget(), true))
                .isInstanceOf(WidgetPersistenceException.class)
                .hasMessage("An internal error ocurred while persisting Widget, invalid data provided");
    }

    @Test
    public void shouldThrow_WidgetPersistenceException_WhenPersistingNullWidget(){

        assertThatThrownBy(() -> this.service.saveWidget(null, true))
                .isInstanceOf(WidgetPersistenceException.class)
                .hasMessage("An internal error ocurred while persisting Widget, invalid data provided");
    }

    @Test
    public void shouldReturn_WidgetOnDB_WhenUpdateWidgetDataHasAllNullFields(){

        Widget widgetOnDB = this.createWidgetWithParameters(1L, 1L, 4L, 5L, 2L, 1L);
        Widget widgetToUpdate = new Widget();
        widgetToUpdate.setId(1L);

        PowerMockito.when(this.searchService.findWidgetById(1L)).thenReturn(widgetOnDB);
        Widget persistedWidget = this.service.updateWidget(widgetToUpdate);

        SoftAssertions softly = new SoftAssertions();

        // Tests that returned value is the one persisted by repository save method
        softly.assertThat(persistedWidget.getId()).isEqualTo(widgetOnDB.getId());
        softly.assertThat(persistedWidget.getWidgetX()).isEqualTo(widgetOnDB.getWidgetX());
        softly.assertThat(persistedWidget.getWidgetY()).isEqualTo(widgetOnDB.getWidgetY());
        softly.assertThat(persistedWidget.getWidgetZIndex()).isEqualTo(widgetOnDB.getWidgetZIndex());
        softly.assertThat(persistedWidget.getWidth()).isEqualTo(widgetOnDB.getWidth());
        softly.assertThat(persistedWidget.getHeight()).isEqualTo(widgetOnDB.getHeight());
        softly.assertThat(persistedWidget.getLastModificationDate()).isEqualTo(widgetOnDB.getLastModificationDate());

        softly.assertAll();
    }

    @Test
    public void shouldReturn_WidgetOnDB_WhenUpdateWidgetDataHasAllFieldsEqualToTheDBOne(){

        Widget widgetOnDB = this.createWidgetWithParameters(0L, 0L, 4L, 5L, 2L, 1L);
        Widget widgetToUpdate = this.createWidgetWithParameters(0L, 0L, 4L, 5L, 2L, 1L);

        PowerMockito.when(this.searchService.findWidgetById(1L)).thenReturn(widgetOnDB);
        Widget persistedWidget = this.service.updateWidget(widgetToUpdate);

        SoftAssertions softly = new SoftAssertions();

        // Tests that returned value is the one persisted by repository save method
        softly.assertThat(persistedWidget.getId()).isEqualTo(widgetOnDB.getId());
        softly.assertThat(persistedWidget.getWidgetX()).isEqualTo(widgetOnDB.getWidgetX());
        softly.assertThat(persistedWidget.getWidgetY()).isEqualTo(widgetOnDB.getWidgetY());
        softly.assertThat(persistedWidget.getWidgetZIndex()).isEqualTo(widgetOnDB.getWidgetZIndex());
        softly.assertThat(persistedWidget.getWidth()).isEqualTo(widgetOnDB.getWidth());
        softly.assertThat(persistedWidget.getHeight()).isEqualTo(widgetOnDB.getHeight());
        softly.assertThat(persistedWidget.getLastModificationDate()).isEqualTo(widgetOnDB.getLastModificationDate());

        softly.assertAll();
    }

    @Test
    public void shouldReturn_WidgetWithUpdatedZIndexAndCallRepositorySaveAll_WhenUpdateWidgetDataChangesZIndexOfWidget(){

        Widget widgetOnDB = this.createWidgetWithParameters(0L, 0L, 4L, 5L, 2L, 1L);
        Widget widgetToUpdate = this.createWidgetWithParameters(0L, 0L, 4L, 5L, 3L, 1L);

        PowerMockito.when(this.searchService.findWidgetById(1L)).thenReturn(widgetOnDB);
        PowerMockito.when(this.repository.save(any(Widget.class))).thenReturn(widgetToUpdate);

        Widget persistedWidget = this.service.updateWidget(widgetToUpdate);

        ArgumentCaptor<Widget> saveArgument = ArgumentCaptor.forClass(Widget.class);
        verify(this.repository, times(1)).save(saveArgument.capture());

        SoftAssertions softly = new SoftAssertions();

        // Tests that returned value is the one persisted by repository save method
        softly.assertThat(persistedWidget.getId()).isEqualTo(widgetToUpdate.getId());
        softly.assertThat(persistedWidget.getWidgetX()).isEqualTo(widgetToUpdate.getWidgetX());
        softly.assertThat(persistedWidget.getWidgetY()).isEqualTo(widgetToUpdate.getWidgetY());
        softly.assertThat(persistedWidget.getWidgetZIndex()).isEqualTo(widgetToUpdate.getWidgetZIndex());
        softly.assertThat(persistedWidget.getWidth()).isEqualTo(widgetToUpdate.getWidth());
        softly.assertThat(persistedWidget.getHeight()).isEqualTo(widgetToUpdate.getHeight());
        softly.assertThat(persistedWidget.getLastModificationDate()).isAfterOrEqualsTo(widgetToUpdate.getLastModificationDate());

        // Tests that parameter used on save method has correct values
        softly.assertThat(saveArgument.getValue().getId()).isEqualTo(widgetToUpdate.getId());
        softly.assertThat(saveArgument.getValue().getWidgetX()).isEqualTo(widgetToUpdate.getWidgetX());
        softly.assertThat(saveArgument.getValue().getWidgetY()).isEqualTo(widgetToUpdate.getWidgetY());
        softly.assertThat(saveArgument.getValue().getWidgetZIndex()).isEqualTo(widgetToUpdate.getWidgetZIndex());
        softly.assertThat(saveArgument.getValue().getWidth()).isEqualTo(widgetToUpdate.getWidth());
        softly.assertThat(saveArgument.getValue().getHeight()).isEqualTo(widgetToUpdate.getHeight());
        softly.assertThat(saveArgument.getValue().getLastModificationDate()).isAfterOrEqualsTo(widgetToUpdate.getLastModificationDate());

        softly.assertAll();
    }

    @Test
    public void shouldReturn_WidgetWithAllDataUpdatedAndCallRepositorySaveAll_WhenUpdateWidgetDataChangesAllOriginalWidgetData(){

        Widget widgetOnDB = this.createWidgetWithParameters(0L, 0L, 4L, 5L, 2L, 1L);
        Widget widgetToUpdate = this.createWidgetWithParameters(1L, 1L, 6L, 7L, 3L, 1L);

        PowerMockito.when(this.searchService.findWidgetById(1L)).thenReturn(widgetOnDB);
        PowerMockito.when(this.repository.save(any(Widget.class))).thenReturn(widgetToUpdate);

        Widget persistedWidget = this.service.updateWidget(widgetToUpdate);

        ArgumentCaptor<Widget> saveArgument = ArgumentCaptor.forClass(Widget.class);
        verify(this.repository, times(1)).save(saveArgument.capture());

        SoftAssertions softly = new SoftAssertions();

        // Tests that returned value is the one persisted by repository save method
        softly.assertThat(persistedWidget.getId()).isEqualTo(widgetToUpdate.getId());
        softly.assertThat(persistedWidget.getWidgetX()).isEqualTo(widgetToUpdate.getWidgetX());
        softly.assertThat(persistedWidget.getWidgetY()).isEqualTo(widgetToUpdate.getWidgetY());
        softly.assertThat(persistedWidget.getWidgetZIndex()).isEqualTo(widgetToUpdate.getWidgetZIndex());
        softly.assertThat(persistedWidget.getWidth()).isEqualTo(widgetToUpdate.getWidth());
        softly.assertThat(persistedWidget.getHeight()).isEqualTo(widgetToUpdate.getHeight());
        softly.assertThat(persistedWidget.getLastModificationDate()).isAfterOrEqualsTo(widgetToUpdate.getLastModificationDate());

        // Tests that parameter used on save method has correct values
        softly.assertThat(saveArgument.getValue().getId()).isEqualTo(widgetToUpdate.getId());
        softly.assertThat(saveArgument.getValue().getWidgetX()).isEqualTo(widgetToUpdate.getWidgetX());
        softly.assertThat(saveArgument.getValue().getWidgetY()).isEqualTo(widgetToUpdate.getWidgetY());
        softly.assertThat(saveArgument.getValue().getWidgetZIndex()).isEqualTo(widgetToUpdate.getWidgetZIndex());
        softly.assertThat(saveArgument.getValue().getWidth()).isEqualTo(widgetToUpdate.getWidth());
        softly.assertThat(saveArgument.getValue().getHeight()).isEqualTo(widgetToUpdate.getHeight());
        softly.assertThat(saveArgument.getValue().getLastModificationDate()).isAfterOrEqualsTo(widgetToUpdate.getLastModificationDate());

        softly.assertAll();
    }

    @Test
    public void shouldThrow_ResourceNotFoundException_WhenDeletingNullWidget(){

        PersistWidgetService testService;
        SearchWidgetService testSearchService;
        WidgetRepository testRepository;

        testRepository = PowerMockito.mock(WidgetRepository.class);
        testSearchService = new SearchWidgetService(testRepository);
        testService = new PersistWidgetService(testRepository, testSearchService);

        PowerMockito.when(testRepository.findById(null)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> testService.deleteWidgetById(null))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Couldn't find Widget with id %s", null));
    }

    @Test
    public void shouldThrow_ResourceNotFoundException_WhenDeletingWidgetNotFound(){

        PersistWidgetService testService;
        SearchWidgetService testSearchService;
        WidgetRepository testRepository;

        testRepository = PowerMockito.mock(WidgetRepository.class);
        testSearchService = new SearchWidgetService(testRepository);
        testService = new PersistWidgetService(testRepository, testSearchService);

        PowerMockito.when(testRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> testService.deleteWidgetById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Couldn't find Widget with id %s", 1L));
    }

    @Test
    public void shouldThrow_WidgetPersistenceException_WhenProblemDeletingWidget(){

        doThrow(new RuntimeException()).when(this.repository).deleteById(1L);

        assertThatThrownBy(() -> this.service.deleteWidgetById(1L))
                .isInstanceOf(WidgetPersistenceException.class)
                .hasMessage("An internal error ocurred while deleting Widget data");
    }

    @Test
    public void shouldCallDeleteByIdOnce_WhenDeletingValidWidget(){

        this.service.deleteWidgetById(1L);
        verify(this.repository, times(1)).deleteById(1L);
    }

    /******************************************************************************************************/

    private Widget createWidgetWithParameters(Long widgetX, Long widgetY,
                                              Long width, Long height, Long zIndex, Long id) {

        Widget result = new Widget(widgetX, widgetY, width, height);
        result.setId(id);
        result.setWidgetZIndex(zIndex);
        result.setLastModificationDate(new Date());

        return result;
    }
}