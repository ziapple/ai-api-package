package com.casic.atp.apiwrapper;

import com.casic.atp.model.ModelService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class APIWrapperController {
    @Resource
    ModelService modelService;

    @RequestMapping(value = "/wrapper", method = {RequestMethod.GET})
    public String wrapper() {
       return "wrapper";
    }

    @RequestMapping("/mvc")
    public String Hello(){
        return "hello";
    }

    /**
     * 获取模型
     * @return
     */
    @RequestMapping("/get_model")
    public String get_model() {
        return "iris.model";
    }
}
