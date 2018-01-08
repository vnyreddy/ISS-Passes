package com.vinay.wizdem.spacestationfinder;

import com.vinay.wizdem.spacestationfinder.model.flyby.Response;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by vinay_1 on 1/6/2018.
 */

public class ResponseTest {

    Response response;
    @Test
    public void ResponseTest(){
    }

    @Test
    public void dateStampTest()throws Exception{
        response = new Response();
        response.setRisetime(1515305634);

        Assert.assertEquals("Sun, 7 Jan 2018 01:13:54 AM EST",response.getRisetime());
    }
}
