package com.vinay.wizdem.spacestationfinder;

import com.vinay.wizdem.spacestationfinder.model.flyby.IssResponse;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by vinay_1 on 1/6/2018.
 */

public class ResponseTest {

    IssResponse response;
    @Test
    public void ResponseTest(){
    }

    @Test
    public void dateStampTest()throws Exception{
        response = new IssResponse();
        response.setRisetime(1515305634);

        Assert.assertEquals("Sun, 7 Jan 2018 01:13:54 AM EST",response.getRisetime());
    }
}
