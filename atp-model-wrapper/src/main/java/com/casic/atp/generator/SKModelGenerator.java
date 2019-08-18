package com.casic.atp.generator;
import com.casic.atp.model.Model;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


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
