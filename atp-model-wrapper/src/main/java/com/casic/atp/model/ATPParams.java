package com.casic.atp.model;

/**
 * 模型的输入和输出参数
 */
public class ATPParams {
    //参数名称
    public String name;

    //参数类型,INT,STR,BASE64
    public String type;

    //是否必填
    public Boolean isRequired;

    //默认值
    public String defaultValue;

    public ATPParams(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public ATPParams(String name, String type, Boolean isRequired, String defaultValue) {
        this.name = name;
        this.type = type;
        this.isRequired = isRequired;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
