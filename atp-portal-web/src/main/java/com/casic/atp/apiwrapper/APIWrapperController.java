package com.casic.atp.apiwrapper;

import com.casic.atp.model.ATPModel;
import com.casic.atp.model.ATPModelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class APIWrapperController {
    @Resource
    ATPModelService modelService;

    // 返回wrapper页面
    @RequestMapping("/wrapper")
    public String wrapper(Model model){
        //模拟模型文件
        List<String> list = new ArrayList<String>();
        list.add("iris.model");
        model.addAttribute("models", list);
        return "wrapper";
    }

    /**
     * 生成沙盒模型
     * 1. 保存模型文件request->model
     * 2. 生成工程文件model->app
     * 3. 上传工程文件到环境app->environment
     */
    public String genSanBox(Model model){
        //1. 保存模型文件request->model
        ATPModel atpModel = this.toATPModel(model);
        modelService.saveModel(atpModel);
        //2. 生成工程文件model->app
        modelService.generate(atpModel);
        //3. 上传工程文件到环境app->environment
        return "";
    }

    public ATPModel toATPModel(Model model){
        return new ATPModel();
    }
}
