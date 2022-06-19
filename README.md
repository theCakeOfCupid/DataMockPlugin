# DataMockPlugin
[![](https://jitpack.io/v/theCakeOfCupid/DataMockPlugin.svg)](https://jitpack.io/#theCakeOfCupid/DataMockPlugin)

a plugin use to hook third part locate development kit

## 详细说明
该插件需要结合[DataMock](https://github.com/theCakeOfCupid/DataMock)一起使用，用于扩展hook第三方定位SDK，目前支持高德、百度
## 实现原理
通过字节插桩hook第三方定位SDK的定位接口来完成定位信息的篡改
## Reference

```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

	dependencies {
	        implementation 'com.github.theCakeOfCupid:DataMockPlugin:1.0.0'
	}
```

hook方式均参见[DataMock](https://github.com/theCakeOfCupid/DataMock)
