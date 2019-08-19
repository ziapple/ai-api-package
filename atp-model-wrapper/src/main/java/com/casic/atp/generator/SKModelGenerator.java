package com.casic.atp.generator;


/**
 * @author zouping
 * @date 2019-08-13
 * SKModel的模板文件生成器
 */
public class SKModelGenerator extends AbstractModelGenerator {
    public String buildImportLine(){
        return "from sklearn.externals import joblib";
    }

    public String buildLoadModel(){
        return "model = joblib.load('" +  model.getFilePath() + "')";
    }
}
