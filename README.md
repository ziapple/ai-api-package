# ai-api-package
* 将Python算法（含tensorflow）自动封装成API服务

# 1.封装过程
![封装过程图]('./images/proccess.png')
# 2.详细封装过程
![详细封装过程]('./images/process_detail.png')
# 3.包结构
![包结构]('./images/package.png')
# 4.模型封装过程
![模型封装过程]('./images/model_package.png')
---
# 5.启动工程
## 5.1 两台服务器S1，S2，环境如下：
###（1）S1：模拟容器环境，ip地址：192.168.56.102，这个就是将来启动模型容器运行环境后的IP地址，此处模拟已经获得IP地址  
S1主要运行两个服务shell服务atp-shell-api工程和模型API服务xx-server工程，
   - atp-shell-api工程介绍
     * atp-shell-api工程是一个python运行的web服务，用来接收客户端发送的远程执行环境命令shell请求，默认接口为5000，例如:
     * /shell/<cmd>，在远程执行cmd命令，/python/<file_path>，执行python文件</li>
     * /upload_model,上传模型文件
     * /upload_app,上传封装好的API工程
	- atp-shell-api安装
	 * 拷贝到/opt/atp-shell-api下，运行python main.py,默认端口为5000
    - 模型API服务工程会自动打包封装到S1,默认端口为5001，调用接口地址为,http://192.168.56.102:5001/api