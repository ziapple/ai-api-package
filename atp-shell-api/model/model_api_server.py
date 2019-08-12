from flask import Flask, jsonify, request
import numpy as np
from sklearn.externals import joblib
from sklearn.preprocessing import StandardScaler
import json
import os
 

# Flask工程
app = Flask(__name__)


# 模型封装服务
# @json 输入参数,'{"model":"iris模型", "data":[[6.5, 3.0, 5.8, 2.2]]}'
@app.route('/api', methods=['POST'])
def model_api():
    # 读取的body原始数据application/json
    data = request.get_data()
    print('data=%s' % data)
    if not data:
        return jsonify("请输入模型参数")
    json_data = json.loads(data.decode('utf-8'))
    print(json_data)
    x_test = np.array(json_data.get('data'))  # 数据特征
    dir = os.path.abspath(os.path.dirname(__file__)) #当前路径
    model = joblib.load(os.path.join(dir, 'iris.model'))
    result = model.predict(x_test)
    return jsonify(result.tolist())


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)