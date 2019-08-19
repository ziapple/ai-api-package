package com.casic.atp.apiwrapper;

import com.casic.atp.controller.RetResult;
import com.casic.atp.model.ATPModel;
import com.casic.atp.model.ATPModelService;
import org.apache.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/model")
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
        return "model-wrapper";
    }

    /**
     * 暂存模型
     * @return
     */
    @RequestMapping(value = "/save",  method={RequestMethod.POST})
    @ResponseBody
    public RetResult<ATPModel> saveModel(HttpServletRequest request){
        try {
            String name = request.getParameter("name");
            //1. 保存模型文件request->model
            ATPModel atpModel = new ATPModel();
            modelService.saveModel(atpModel);
            return RetResult.OK(atpModel);
        }catch (Exception e){
            return RetResult.ERROR(e.getMessage());
        }
    }

    /**
     * 生成沙盒模型
     * 1. 保存模型文件request->model
     * 2. 生成工程文件model->app
     * 3. 上传工程文件到环境app->environment
     */
    @RequestMapping(value = "/genSanBox",  method={RequestMethod.POST})
    @ResponseBody
    public RetResult<ATPModel> genSanBox(Model model){
        try {
            //1. 保存模型文件request->model
            ATPModel atpModel = this.toATPModel(model);
            modelService.saveModel(atpModel);
            //2. 生成工程文件model->app
            modelService.generate(atpModel);
            //3. 上传工程文件到环境app->environment
            modelService.deploy(atpModel.getName());
            //4. 启动app环境
            modelService.start(atpModel.getName());
            return RetResult.OK(atpModel);
        }catch (Exception e){
            return RetResult.ERROR(e.getMessage());
        }
    }

    public ATPModel toATPModel(Model model){
        return new ATPModel();
    }
}
