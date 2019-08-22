import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class JSONTest {

    public void testStr2JSON(){
        String jsonStr = "{\"data\":\"[[6.5, 3.0, 5.8, 2.2]]\"}";
        Map<String,Object> json = JSONObject.parseObject(jsonStr);
        System.out.println(json.get("data"));
    }

    @Test
    public void testBytes() throws UnsupportedEncodingException {
        String a = "%5B%5B6.5%2C+3.0%2C+5.8%2C+2.2%5D%5D";
        String s = "\"data\":\"[[6.5, 3.0, 5.8, 2.2]]\"";
        byte[] b = s.getBytes("utf-8");
        System.out.println(b);
        System.out.println(new String(b));
    }
}
