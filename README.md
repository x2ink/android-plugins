# 📱 uni-app 安卓原生插件：获取手机应用列表

## 📖 插件简介

本插件用于 **获取 Android 手机上已安装的应用列表**，支持：

* 获取 **用户安装的应用** 或 **全部应用**
* 获取应用详细信息：

    * 应用图标（Base64 格式）
    * 应用大小
    * 应用名称
    * 应用包名
    * 应用版本
    * APK 路径
    * 安装时间
    * 最后更新时间
    * 应用类型（用户应用 / 系统应用）

---

## 🔐 权限说明

* 需要 **读取应用列表权限**。
* 如果需要根据 APK 路径上传到云服务器，还需要 **所有文件访问权限**，否则无法读取文件。

---

## 🧩 使用说明

### 1️⃣ 获取插件

```js
const getapplist = uni.requireNativePlugin('Fengci-AppList')
```

---

### 2️⃣ 获取应用列表

#### 获取用户安装的应用：

```js
getapplist.userapp((res) => {
  console.log(res)
})
```

#### 获取全部应用：

```js
getapplist.allapp((res) => {
  console.log(res)
})
```

#### 搜索应用（可按名称或包名）：

```js
getapplist.search('微信', (res) => {
  console.log(res)
})
```

---

## 💡 代码示例

```vue
<template>
	<view>
		<input type="text" placeholder="请输入应用名称或者包名" style="height: 50px;" @blur="search">
		<button @click="user">获取用户应用</button>
		<button @click="all">获取全部应用</button>
		<view>
			<view v-for="item in list" @click="up(item.path,item.name)"
				style="padding: 5px;display: flex;align-items: center;">
				<image :src="item.icon" style="width: 50px;height: 50px;border-radius: 10px;"></image>
				<view style="margin-left: 5px;font-size: 12px;">
					<p style="font-size: 16px;">{{item.name}} V{{item.version}}</p>
					<p>{{item.package}} | {{gsbytes(item.size)}}</p>
					<p>安装时间:{{gstime(item.firstDate)}}</p>
					<p>更新时间:{{gstime(item.lastDate)}}</p>
					<p>类型:{{item.flags==0?'本地应用':'系统应用'}}</p>
				</view>
			</view>
		</view>
	</view>
</template>
<script setup>
	import {
		ref
	} from 'vue'
	import {
		onUnload
	} from "@dcloudio/uni-app";
	const getapplist = uni.requireNativePlugin('Fengci-AppList')
	const list = ref([])
	// 获取用户的应用
	const user = () => {
		list.value = []
		getapplist.userapp((res) => {
			list.value = list.value.concat(res)
		})
	}
	// 获取全部的应用
	const all = async () => {
		list.value = []
		getapplist.allapp((res) => {
			list.value = list.value.concat(res)
		})
	}
	//搜索应用，关键词可以是app名称或者包名，返回所有的app类型
	const search = (val) => {
		list.value = []
		getapplist.search(val.detail.value, (res) => {
			if (res == null) {
				uni.showToast({
					title: "没有搜到应用",
					icon:"error"
				})
			} else {
				list.value = list.value.concat(res)
			}

		})
	}
	const up = (path, name) => {
		// 上传应用到服务器需要该应用获取所有文件访问权限，否则无法上传
		uniCloud.uploadFile({
			filePath: path,
			cloudPath: name + '.apk',
			success(res) {
				uni.showToast({
					title: '上传成功',
					duration: 2000
				});
			},
			fail(res) {
				console.log(res);
			}
		});
	}
	const gstime = (time) => {
		time = new Date(time)
		var month = time.getMonth() + 1;
		var day = time.getDate();
		var min = time.getMinutes()
		var hour = time.getHours()
		return month + "月" + day + "日" + hour + "时" + min + "分";
	}
	const gsbytes = (limit) => {
		var size;
		if (limit < 1024) {
			size = limit + "B"
		} else if (1024 <= limit && limit < 1024 * 1024) {
			size = (limit / 1024).toFixed(2) + "KB"
		} else if (1024 * 1024 <= limit && limit < 1024 * 1024 * 1024) {
			size = (limit / (1024 * 1024)).toFixed(2) + "MB"
		} else if (1024 * 1024 * 1024 <= limit) {
			size = (limit / (1024 * 1024 * 1024)).toFixed(2) + "GB"
		}
		return size;
	}
	const copy = (url) => {
		uni.setClipboardData({
			data: url
		});
	}
</script>
```

---

## 📤 返回参数说明

| 参数名         | 类型     | 说明                  |
|-------------|--------|---------------------|
| `icon`      | String | 应用图标（Base64 格式）     |
| `name`      | String | 应用名称                |
| `version`   | String | 应用版本                |
| `package`   | String | 应用包名                |
| `firstDate` | Long   | 安装时间（时间戳）           |
| `lastDate`  | Long   | 更新时间（时间戳）           |
| `size`      | Long   | 应用大小                |
| `path`      | String | 安装包路径（file:// 格式）   |
| `flags`     | Int    | 应用类型（0：用户应用；1：系统应用） |

---

## 🔗 插件市场地址

👉 [uni-app 插件市场 · 获取手机应用列表](https://ext.dcloud.net.cn/plugin?id=12003)
