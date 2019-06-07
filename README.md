# CNLab5_IPLayer_softRouter
北理工计算机网络第五章实验-ip层_simple sorftRouter（with many bugs)

运行方式：

Step1. 运行exeCopyBat.jar（生成相同的softRouter.exe: 1 - 5, 数目由配置文件中的路由器数目决定）

Step2. 运行turnkeySystem.jar（总控程序，运行上面的5个softRouter.exe，并分配端口号，即：开启各个路由器）

Step3. ok

注：路由器之间的信息以及分组的转发可能比较慢，因为为了上课展示，表现出它的变化，所以程序中Send线程每次发送信息前设置了Thread.sleep()，注释掉即可。

1. 实验要求：

![image](https://github.com/ItsSoHardToIntitle/CNLab5_IPLayer_softRouter/blob/master/image/1.png)
![image](https://github.com/ItsSoHardToIntitle/CNLab5_IPLayer_softRouter/blob/master/image/2.png)

2. 实验分析：

![image](https://github.com/ItsSoHardToIntitle/CNLab5_IPLayer_softRouter/blob/master/image/%E8%A6%81%E6%B1%82%E5%88%86%E8%A7%A3%E5%9B%BE.jpg)
![image](https://github.com/ItsSoHardToIntitle/CNLab5_IPLayer_softRouter/blob/master/image/%E6%B5%81%E7%A8%8B%E5%9B%BE.jpg)

3. 结果展示：

![image](https://github.com/ItsSoHardToIntitle/CNLab5_IPLayer_softRouter/blob/master/image/3.png)

4. 水平有限
