package com.casic.atp.graph.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

@Controller
@RequestMapping("/graph")
public class ModelGraphController {

    // 返回wrapper页面
    @RequestMapping("/design")
    public String design(){
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return "model-graph-design";
    }
}
