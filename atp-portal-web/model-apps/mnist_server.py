from json import JSONDecodeError
from flask import Flask, jsonify, request
import numpy as np
import json
import re
import base64
from PIL import Image
from keras.models import load_model
import keras

def ok(obj):
    """
    返回客户端API调用成功
    :param obj: 结果
    :return: json格式
    """
    return jsonify(code=200, result=obj)


def error(msg):
    """
    返回客户端API调用失败
    :param msg:
    :return:
    """
    return jsonify(code=500, msg=str(msg))


def is_base64(img_str):
    """
    判断是否为Base64编码，如是，对参数进行解码
    :param img_str: Base64字符串
    :return:
    """
    base64_pattern = r"^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$"
    pattern = re.compile(base64_pattern)
    match = pattern.match(img_str)
    return True if match else False


app = Flask(__name__)


def get_input(req_params):
    """ req_params要和第二步的输入参数一致
    :req_params:json格式，通过get("XX")获取数据
    :return:json格式
    """
    # 标准代码
    predict_data=req_params.get("data")
    # base64图像调用代码
    img_data = base64.b64decode(predict_data)
    with open('tmp.jpg', 'wb') as f: # 存成临时文件
        f.write(img_data)
    img = Image.open('tmp.jpg')
    return np.array(img).reshape(1, 784)


def get_output(result):
    """
    将结果转化成json格式
    :param result:np.array类型
    :return:json格式
    """
    return ok(result.tolist())


@app.route('/api', methods=['POST'])
def model_api():
    """
    模型处理过程：
    1. 判断请求参数是否为空，为空直接返回，如果不为空，跳转到第2步
    2. 将请求参数转化为utf8编码，并转化为json格式
    3. 加载模型文件
    4. 处理请求数据
    5. 预测模型
    6. 处理返回结果
    :return: json格式
    ok:{code:200, result:[]}
    error:{code:500,msg:*}
    """
    # 1. 判断请求参数是否为空，为空直接返回，如果不为空，跳转到第2步
    data = request.get_data()
    if not data:
        return error("请输入模型参数")
    try:
        # 2. 将请求参数转化为utf8编码，并转化为json格式
        json_data = json.loads(data.decode('utf-8'))
        # 3. 加载模型文件
        keras.backend.clear_session()
        model = load_model('/opt/atp-shell-api/model/mnist.model.h5')
        # 4. 处理请求数据
        x_test = get_input(json_data)
        # 5. 预测模型
        result = model.predict_classes(x_test)
        # 6. 处理返回结果
        return get_output(result)
    except JSONDecodeError:
        return error("json格式转化错误%s" % data)
    except Exception as ex:
        if hasattr(ex, 'message'):
            return error(ex.message)
        else:
            return error(ex)
    except KeyError:
        return error("处理失败")


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)
