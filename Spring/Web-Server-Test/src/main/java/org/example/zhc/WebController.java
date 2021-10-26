package org.example.zhc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
    @RequestMapping("/test/ClientMsgHead")
    public String index(){
        System.out.println("request!");
        return "";
    }
}
