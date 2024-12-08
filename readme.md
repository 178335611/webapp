《网络应用开发》课程实验报告
实验题目： 设计并实现一个电子商务网站的开发和在线部署   __
姓名：     陈奕宋      学号：  202230440278 ____
班级：   网络工程班        
提交日期：       2024-12-8             

【基本功能要求】
顾客：
用户的注册、登录、注销
展示产品列表
购买流程（浏览/查询->添加至购物车->付款->发送电子邮件确认收货）
可以查看订单状态和历史
销售：
商品目录的管理（包括最基本的增删改等操作）
订单管理、以及销售统计报表
客户管理、以及客户的 浏览/购买 日志 记录

【实验环境和工具】
jdk17.0.8,Tomcat9.0.97,mysql8.0.40,IDEA

1.	系统设计：
给出系统整体架构，并分别介绍各个模块。

		    设计了用户登录和商家登录模块，设计了商城页面，仓库页面，用户和商家的个人页面，以及个人和商家的注册页面。
		    首先是注册登录，要求账户密码。登录后在http的session中加入用户属性和用户编号便于查询。
            用户登录后进入商城页面，可以看到所有的产品，考虑到产品的数量提供搜索功能。可以下单，调整购买数量。进入个人页面，可以修改个人信息，可以删除账号，删除前有提示。可以管理订单，显示全部订单可以看到历史订单，还可以选择显示取消的订单和完成的订单，默认是显示当前正在进行的订单。可以进行付款，收到，取消等等功能，依订单的状态而判定能否执行功能。
		    商家登录后进入仓库页面，在这里可以管理自己的产品，包括新建删除修改等等，允许同名产品，依靠产品编号区分。右上角进入账号页面，可以修改个人信息，删除账号。订单的显示和管理都在这个页面，可以完成订单，取消订单等等，也以订单状态而定。可以选择不同状态的订单展示。
	综上所述实现了商家和顾客的用户的注册、登录、注销。实现了展示产	品列表，购买流程为查看商城，选择商品，添加订单（未付款），在订单页	面付款（未完成），然后商家可以取消或完成之，然后用户收到后可以将接	收订单。个人和商家都可以可以查看订单状态和历史。实现了商品目录的管	理（包括最基本的增删改等操作），实现了订单管理、以及可以看到销售完	成的订单，可以看到客户的下单/付款的所有订单


2.	代码实现
给出github或gitee代码地址，以及代码文件的简单说明。
项目网站的包叫做star32k只是war导出包时使用的名字。
一开始登录login.jsp，这是用户登录界面，可以跳转到merchantlogin.jsp。可以注册。商城界面是shoppingPage.jsp,仓库页面是warehouse.jsp，账户页面分两个accout.jsp。每个jsp有对应的servlet。详情见报告。
