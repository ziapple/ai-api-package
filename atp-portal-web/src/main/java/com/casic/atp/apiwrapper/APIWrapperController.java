package com.casic.atp.apiwrapper;

import com.alibaba.fastjson.JSONObject;
import com.casic.atp.controller.RetResult;
import com.casic.atp.model.ATPModel;
import com.casic.atp.model.ATPModelService;
import com.casic.atp.model.ATPParams;
import com.casic.atp.util.HttpUtils;
import com.fasterxml.jackson.core.util.InternCache;
import jdk.nashorn.internal.parser.JSONParser;
import net.sf.json.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // 返回打包后的模型列表页面
    @RequestMapping("/list")
    public String list(Model model){
        //模拟模型文件
        model.addAttribute("models", modelService.listAll());
        return "model-list";
    }

    // 返回打包后的模型列表页面
    @RequestMapping("/test")
    public String test(HttpServletRequest request, Model model){
        ATPModel atpModel = modelService.getModel(request.getParameter("name"));
        //模拟模型文件
        model.addAttribute("model", atpModel);
        return "model-test";
    }


    /**
     * 暂存模型
     * @return
     */
    @RequestMapping(value = "/save",  method={RequestMethod.POST})
    @ResponseBody
    public RetResult<Map<String,String>> saveModel(HttpServletRequest request){
        try {
            ATPModel atpModel = this.toATPModel(request);
            modelService.saveModel(atpModel);
            Map<String,String> attr = new HashMap<String,String>();
            attr.put("name", atpModel.getName());
            return RetResult.OK(attr);
        }catch (Exception e){
            e.printStackTrace();
            if(e.getCause() != null)
                return RetResult.ERROR(e.getCause().getMessage());
            else
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
    public RetResult<Map<String,String>> genSanBox(HttpServletRequest request){
        try {
            //1. 保存模型文件request->model
            ATPModel atpModel = this.toATPModel(request);
            modelService.saveModel(atpModel);
            //2. 生成工程文件model->app
            modelService.generate(atpModel);
            //3. 上传工程文件到环境app->environment
            modelService.deploy(atpModel.getName());
            //4. 启动app环境
            modelService.start(atpModel.getName());
            Map<String,String> attr = new HashMap<String,String>();
            attr.put("name", atpModel.getName());
            return RetResult.OK(attr);
        }catch (Exception e){
            e.printStackTrace();
            if(e.getCause() != null)
                return RetResult.ERROR(e.getCause().getMessage());
            else
                return RetResult.ERROR(e.getMessage());
        }
    }

    /**
     * 测试模型
     * @return
     */
    @RequestMapping(value = "/invoke",  method={RequestMethod.POST})
    @ResponseBody
    public RetResult<Map<String,Object>> invokeModel(HttpServletRequest request){
        try {
            String modelName = request.getParameter("name");
            //请求数据json格式，系统自动传递data参数
            String jsonStr = request.getParameter("data");
            ATPModel atpModel = modelService.getModel(modelName);
            //直接将请求转发给真实的API服务
            String result = HttpUtils.post(atpModel.getEnvironment().getInvokeCmd(), jsonStr, null);
            Map<String, Object> data = JSONObject.parseObject(result);
            if(Integer.parseInt(data.get("code").toString())==200)
                return RetResult.OK(data);
            else
                return RetResult.ERROR(data.get("msg").toString());
        }catch (Exception e){
            e.printStackTrace();
            if(e.getCause() != null)
                return RetResult.ERROR(e.getCause().getMessage());
            else
                return RetResult.ERROR(e.getMessage());
        }
    }

    /**
     * 模型转化
     * @param request
     * @return
     */
    public ATPModel toATPModel(HttpServletRequest request){
        ATPModel atpModel = new ATPModel();
        //TODO 从项目中获取容器环境IP，此处模拟
        atpModel.getEnvironment().setIp(request.getParameter("ip"));
        //模型名称
        atpModel.setName(request.getParameter("modelName"));
        //模型文件路径, 默认只能选取一个文件,
        //TODO 共享存储需要改此路径
        atpModel.setModelFilePath(request.getParameter("modelFile"));
        //模型格式joblib还是keras
        String modelType = request.getParameter("modelType");
        if(modelType.equals("joblib"))
            atpModel.setType(ATPModel.MODEL_JOBLIB);
        else if(modelType.equals("keras"))
            atpModel.setType(ATPModel.MODEL_KERAS);
        //模型输入参数
        String[] paramsInName = request.getParameterValues("params_in_name");
        String[] paramsInType = request.getParameterValues("params_in_type");
        String[] paramsInIsRequired = request.getParameterValues("params_in_isRequired");
        String[] paramsInDefault = request.getParameterValues("params_in_default");
        for(int i = 0; i<paramsInName.length;  i++){
            ATPParams param = new ATPParams(paramsInName[i], paramsInType[i], Boolean.parseBoolean(paramsInIsRequired[i]), paramsInDefault[i]);
            atpModel.getInParams().add(param);
        }
        //模型输出参数
        String[] paramsOutName = request.getParameterValues("params_out_name");
        String[] paramsOutType = request.getParameterValues("params_out_type");
        for(int i = 0; i<paramsInName.length;  i++){
            ATPParams param = new ATPParams(paramsOutName[i], paramsOutType[i]);
            atpModel.getOutParams().add(param);
        }
        //自定义解析函数
        if(Integer.parseInt(request.getParameter("params_def_input")) == 1)
            atpModel.setDefGetInput(request.getParameter("params_def_input_text"));
        if(Integer.parseInt(request.getParameter("params_def_output")) == 1)
            atpModel.setDefGetOutput(request.getParameter("params_def_output_text"));
        return atpModel;
    }
}
