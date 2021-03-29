package com.widgets.management.api.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.WidgetCreateRequest;
import io.swagger.model.WidgetResponse;
import io.swagger.model.WidgetUpdateRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WidgetsControllerIT {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void integratedTestSaveWidgetsSearchAndDelete() throws Exception {

        // Common create request for generating widgets with sequential ids
        WidgetCreateRequest createRequest = new WidgetCreateRequest();
        createRequest.setWidgetX(0L);
        createRequest.setWidgetY(0L);
        createRequest.setWidth(2L);
        createRequest.setHeight(3L);

        // Request for generating widget with Z-index 8
        WidgetCreateRequest widget8Request = new WidgetCreateRequest();
        widget8Request.setWidgetX(0L);
        widget8Request.setWidgetY(0L);
        widget8Request.setWidth(2L);
        widget8Request.setHeight(3L);
        widget8Request.setWidgetZIndex(8L);

        // Request for generating widget with Z-index 2
        WidgetCreateRequest widget2Request = new WidgetCreateRequest();
        widget2Request.setWidgetX(0L);
        widget2Request.setWidgetY(0L);
        widget2Request.setWidth(2L);
        widget2Request.setHeight(3L);
        widget2Request.setWidgetZIndex(2L);

        // Request for updating widget with id 2 to Z-index 4
        WidgetUpdateRequest widgetUpdateRequest = new WidgetUpdateRequest();
        widgetUpdateRequest.setWidgetZIndex(4L);


        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entityJustHeader = new HttpEntity<>(headers);
        HttpEntity<WidgetCreateRequest> entityCommonCreateRequest = new HttpEntity<>(createRequest, headers);
        HttpEntity<WidgetCreateRequest> entityWidget8CreateRequest = new HttpEntity<>(widget8Request, headers);
        HttpEntity<WidgetCreateRequest> entityWidget2CreateRequest = new HttpEntity<>(widget2Request, headers);
        HttpEntity<WidgetUpdateRequest> entityWidgetUpdateRequest = new HttpEntity<>(widgetUpdateRequest, headers);

        SoftAssertions softly = new SoftAssertions();

        /****** Creating 6 Widgets ending with Z-indexes 1,2,3,4,5,8 *********/

        ResponseEntity<Void> responseSaveMachine1 = restTemplate.exchange(
                createURLWithPort("/widgets-management-api/v1/widgets"), HttpMethod.POST,
                entityCommonCreateRequest, Void.class);

        softly.assertThat(responseSaveMachine1.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Void> responseSaveMachine2 = restTemplate.exchange(
                createURLWithPort("/widgets-management-api/v1/widgets"), HttpMethod.POST,
                entityCommonCreateRequest, Void.class);

        softly.assertThat(responseSaveMachine2.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Void> responseSaveMachine3 = restTemplate.exchange(
                createURLWithPort("/widgets-management-api/v1/widgets"), HttpMethod.POST,
                entityCommonCreateRequest, Void.class);

        softly.assertThat(responseSaveMachine3.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Void> responseSaveMachine4 = restTemplate.exchange(
                createURLWithPort("/widgets-management-api/v1/widgets"), HttpMethod.POST,
                entityCommonCreateRequest, Void.class);

        softly.assertThat(responseSaveMachine4.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Void> responseSaveMachine8 = restTemplate.exchange(
                createURLWithPort("/widgets-management-api/v1/widgets"), HttpMethod.POST,
                entityWidget8CreateRequest, Void.class);

        softly.assertThat(responseSaveMachine8.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Void> responseSaveMachineWidget2 = restTemplate.exchange(
                createURLWithPort("/widgets-management-api/v1/widgets"), HttpMethod.POST,
                entityWidget2CreateRequest, Void.class);

        softly.assertThat(responseSaveMachineWidget2.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> responseFindAllWidgets = restTemplate.exchange(
                createURLWithPort("/widgets-management-api/v1/widgets?currentPage=0&pageSize=10"), HttpMethod.GET, entityJustHeader, String.class);

        String widgetsList = responseFindAllWidgets.getBody();
        ObjectMapper mapper = new ObjectMapper();
        List<WidgetResponse> widgetResponseList =
                Arrays.asList(mapper.readValue(widgetsList, WidgetResponse[].class));

        softly.assertThat(responseFindAllWidgets.getStatusCode()).isEqualTo(HttpStatus.OK);
        softly.assertThat(widgetResponseList.size()).isEqualTo(6);
        softly.assertThat(widgetResponseList.get(0).getId()).isEqualTo(1L);
        softly.assertThat(widgetResponseList.get(0).getWidgetZIndex()).isEqualTo(1L);
        softly.assertThat(widgetResponseList.get(1).getId()).isEqualTo(6L);
        softly.assertThat(widgetResponseList.get(1).getWidgetZIndex()).isEqualTo(2L);
        softly.assertThat(widgetResponseList.get(2).getId()).isEqualTo(2L);
        softly.assertThat(widgetResponseList.get(2).getWidgetZIndex()).isEqualTo(3L);
        softly.assertThat(widgetResponseList.get(3).getId()).isEqualTo(3L);
        softly.assertThat(widgetResponseList.get(3).getWidgetZIndex()).isEqualTo(4L);
        softly.assertThat(widgetResponseList.get(4).getId()).isEqualTo(4L);
        softly.assertThat(widgetResponseList.get(4).getWidgetZIndex()).isEqualTo(5L);
        softly.assertThat(widgetResponseList.get(5).getId()).isEqualTo(5L);
        softly.assertThat(widgetResponseList.get(5).getWidgetZIndex()).isEqualTo(8L);

        /***************/

        /****** Updating Widgets with Id 2 to Z-index 4 ending with Z-indexes 1,2,4,5,6,8 *********/

        ResponseEntity<String> responseUpdateWidgetId2 = restTemplate.exchange(
                createURLWithPort("/widgets-management-api/v1/widgets/2"), HttpMethod.PUT, entityWidgetUpdateRequest, String.class);

        softly.assertThat(responseUpdateWidgetId2.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> responseFindWidgetId2 = restTemplate.exchange(
                createURLWithPort("/widgets-management-api/v1/widgets/2"), HttpMethod.GET, entityJustHeader, String.class);

        String widgetStringBody = responseFindWidgetId2.getBody();
        WidgetResponse findWidgetByIdResponse =
                mapper.readValue(widgetStringBody, WidgetResponse.class);
        softly.assertThat(findWidgetByIdResponse.getId()).isEqualTo(2L);
        softly.assertThat(findWidgetByIdResponse.getWidgetZIndex()).isEqualTo(4L);

        /***************/

        /****** Deleting Widgets with Id 2 and not being able to find it anymore *********/

        ResponseEntity<String> responseDeleteWidgetId1 = restTemplate.exchange(
                createURLWithPort("/widgets-management-api/v1/widgets/1"), HttpMethod.DELETE, entityJustHeader, String.class);

        softly.assertThat(responseDeleteWidgetId1.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> responseFindWidgetId1 = restTemplate.exchange(
                createURLWithPort("/widgets-management-api/v1/widgets/1"), HttpMethod.GET, entityJustHeader, String.class);

        softly.assertThat(responseFindWidgetId1.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        /***************/

        softly.assertAll();
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
