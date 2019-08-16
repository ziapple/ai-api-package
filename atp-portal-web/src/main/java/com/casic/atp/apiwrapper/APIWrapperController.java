package com.casic.atp.apiwrapper;

import com.casic.atp.model.ModelService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class APIWrapperController {
    @Resource
    ModelService modelService;


    /**
     * 获取模型
     * @return
     */
    @RequestMapping("/get_model")
    public String get_model() {
        return "iris.model";
    }
}
