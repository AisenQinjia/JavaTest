package org.example.zhc.resource.subdir;

import java.net.URL;

public class ReadFile {
    public URL getTestUrl(){
        return this.getClass().getResource("/test");
    }
}
