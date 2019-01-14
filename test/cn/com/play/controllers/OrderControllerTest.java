package cn.com.play.controllers;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.POST;
import static play.test.Helpers.route;

public class OrderControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void should_pay() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(routes.OrderController.apply().url());

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

}