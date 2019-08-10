package com.casic.atp.apiwrapper;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIWrapperController {

    /**
     * 获取模型
     * @return
     */
    @RequestMapping("/get_model")
    public String get_model() {
        return "iris.model";
    }
}
