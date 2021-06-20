# LayoutLevelDetector
布局层级检测

### 项目起源
众所周知，项目页面布局的嵌套层级会影响到页面的渲染效率，目前我们大多通过android studio的Layout Inspector工具，去找到对应的页面再去看xml文件是否存在过深的嵌套。
但对于一些老项目来说，几十，上百个页面，一个个去查看对应xml文件，确定是否存在过深的布局嵌套，显示是一件很费时的事情。
因此，如果在项目测试过程中，进入当前页面，就能知道，该页面是否存在过深的布局嵌套，相对而言就能更加省力一些。
本项目就是希望能实现这么一个效果。
![效果]()

### 使用方式

1. 在根项目的build.gradle中，配置如下：
Add it in your root build.gradle at the end of repositories:

```
    allprojects {
      repositories {
        ...
        maven { url 'https://jitpack.io' }
      }
    }
  
```

2. 在主项目的 build.gradle中，添加对应的依赖：
Add the dependency

```
    debugImplementation 'com.github.zxhandroid:LayoutLevelDetector:v1.0-alpha'

```

3. 可通过如下代码，设置最大的布局层级限制，默认是4,大于或等于4个层级，则会弹窗提示：
  如果可以，越早设置越好，建议在Application oncreate方法中，就开始设置，避免有些页面未按设置值进行检测。

```
    LayoutLevelDetector.getInstance().setLimitLevel(limitLevel);
    
```

