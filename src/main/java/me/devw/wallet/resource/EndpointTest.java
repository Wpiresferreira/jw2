package me.devw.wallet.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;


@Path("/test")
public class EndpointTest {

    @GET
    public String ok() {
        return "OK";
    }
}
