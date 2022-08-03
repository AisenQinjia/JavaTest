package org.example.zhc.util.zhc.validation;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class WebController {
    @RequestMapping("/test/ClientMsgHead")
    public String index(){
        System.out.println("request!");
        return "";
    }

    @RequestMapping("/test/paramcheck")
    public String paramCheck(@ModelAttribute("paramtest") @Valid ParamTest paramTest, BindingResult bindingResult, ModelMap model){
        System.out.println("param check " + !bindingResult.hasErrors());
        return "pass";
    }
}
