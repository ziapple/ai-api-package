package com.casic.atp.model;

/**
 * 算法模型，用来保存模型的名称，路径，输入输出参数，解析函数
 */
public class Model {
    public static String MODEL_JOBLIB = "joblib";
    public static String MODEL_KERAS = "keras";

    //模型名称
    private String name;

    //模型格式,目前支持joblib和keras两种格式
    private String type;

    //模型文件路径
    private String filePath;

    //模型输入参数
    private String params_input;

    //模型输出参数
    private String params_output;

    //模型解析函数->输出
    private String parser_input;

    //模型解析函数->输出
    private String parser_output;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getParams_input() {
        return params_input;
    }

    public void setParams_input(String params_input) {
        this.params_input = params_input;
    }

    public String getParams_output() {
        return params_output;
    }

    public void setParams_output(String params_output) {
        this.params_output = params_output;
    }

    public String getParser_input() {
        return parser_input;
    }

    public void setParser_input(String parser_input) {
        this.parser_input = parser_input;
    }

    public String getParser_output() {
        return parser_output;
    }

    public void setParser_output(String parser_output) {
        this.parser_output = parser_output;
    }
}
