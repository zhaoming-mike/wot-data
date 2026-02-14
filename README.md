# 效果
![数据看板](https://github.com/zhaoming-mike/wot-data/blob/main/others/%E9%A3%9E%E4%B9%A6%E5%A4%9A%E7%BB%B4%E8%A1%A8%E6%A0%BC%20BI.png)

# 本项目初衷

没有发现以下哪个网站可以找出：例如：X 级车中百米散布最小的车，因此有了这个项目
- tomato.gg (偏个人数据分析)
- tanks.gg（偏车辆数据分析）
- wotinspector.com（装甲透视分析）
- skill4ltu.eu（国外知名主播建设网站）
- stratsketch.com （战术地图制作）

# 項目信息
## WG API
入口：https://developers.wargaming.net

# 步骤1
注册成为 WG 的开发者

# 步骤2
- 在 WG 的开发者平台创建应用（替换本项目中的 application_id）
- 在 WG 的开发平台配置你的程序出口 IP 地址白名单
- 修改相关 BI 工具的授权信息（飞书、企微）

# 步骤3 
~~在企业微信中创建智能表格（配置好表头的对应关系）~~
- 创建飞书多维表格（配置好表头的对应关系）
- 执行 Main 函数，复制控制台打印的全量坦克数据
