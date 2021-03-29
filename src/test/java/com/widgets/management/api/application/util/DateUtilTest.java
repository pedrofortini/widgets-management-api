package com.widgets.management.api.application.util;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DateUtilTest {

    @Test
    public void shouldReturn_EmptyString_WhenDateParemeterIsNull(){
        assertThat(DateUtil.convertDateToString(null)).isEqualTo("");
    }

    @Test
    public void shouldReturn_ProperlyFormatedString_WhenDateParemeterIsValid(){

        DateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss");
        String currentDate = dateFormat.format(new Date());
        assertThat(DateUtil.convertDateToString(new Date())).contains(currentDate);
    }
}