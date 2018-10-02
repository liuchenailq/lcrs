# lcrs
## 综述
lcrs是用纯Java语言开发的推荐系统库。lcrs借鉴了[Python Surprise](https://surprise.readthedocs.io/en/stable/index.html)库的设计思路，并对其进行了大量拓展。设计lcrs的主要目的有如下几点：    
&ensp;&ensp;1.掌握推荐系统中经典的算法模型。lcrs实现了推荐系统中大部分的经典算法模型，每一个算法都是按照经典的论文（已在resource目录下整理好）进行实现的，这对深入理解算法的原理及其优劣很有好处。   
&ensp;&ensp;2.提高工程设计水平。为了方便使用，必须从整体上将lcrs架构好，这对提高工程设计水平很有帮助。     

目前lcrs的主要功能模块如下：   
&ensp;&ensp;1.数据集模块。此模块为用户提供了丰富的数据集加载、管理方式，常用的有加载内置数据集、从本地文件系统中加载数据集等方式。此模块的详细信息请看[这里](https://github.com/liuchenailq/lcrs/tree/master/src/dataset)。    
&ensp;&ensp;2.预测算法模块。此模块为用户提供了大量的评分预测算法，常见的有UserCF、ItemCF、LFM等。此模块的详细信息请看[这里](https://github.com/liuchenailq/lcrs/tree/master/src/prediction_algorithms)。   
&ensp;&ensp;3.模型评价模块。此模块为用户提供了几种评价模型的方法。常见的评价指标有rmse、mae等。此模块的详细信息请看[这里](https://github.com/liuchenailq/lcrs/tree/master/src/accuracy)。   
&ensp;&ensp;4.模型选择模块。此模块为用户提供了基于交叉验证思想的模型选择方法。常见的交叉验证有K折-交叉验证、留一法等。此模的详细信息请看[这里](https://github.com/liuchenailq/lcrs/tree/master/src/model_selection)。    


     

