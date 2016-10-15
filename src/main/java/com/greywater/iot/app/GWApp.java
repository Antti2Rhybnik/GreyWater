package com.greywater.iot.app;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("rest")
public class GWApp extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        return super.getClasses();
    }
}
