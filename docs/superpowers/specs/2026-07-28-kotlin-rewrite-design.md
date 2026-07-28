# ColorfulSign Kotlin 重写设计

## 目标

以 Kotlin 重写 ColorfulSign，同时保持 Paper 1.20.1、Java 21 和现有聊天显示功能兼容。实现使用 Paper 的 `AsyncChatEvent` 与 Adventure `Component` API，不再使用已过时的字符串聊天格式事件。

## 保留行为

- 聊天格式为：队伍前缀、队伍颜色的玩家名、队伍后缀、英文冒号、聊天内容。
- 玩家没有队伍时，不显示前缀或后缀，玩家名使用白色。
- 玩家发送有效的 `&` 传统颜色或格式代码时，转换为对应的 Adventure 样式。
- 消息没有有效颜色代码时，以白色渲染。
- 普通 `&` 字符不是颜色代码，不改变默认白色行为。

## 架构

- `ColorfulSignPlugin` 继承 `JavaPlugin`，只处理监听器注册与启动日志。
- `ChatListener` 监听 `AsyncChatEvent`，在主线程取得不可变 Team 样式快照后，将渲染职责委托给 `ChatMessageRenderer`。
- `ChatMessageRenderer` 是无状态组件，负责读取队伍数据并组合最终聊天 `Component`。
- `LegacyColorParser` 将旧式 `&` 代码反序列化为 Adventure `Component`，并向调用者报告是否检测到有效代码。
- `TeamChatStyle` 是不可变数据对象，承载前缀、玩家名颜色和后缀，避免渲染层依赖 Bukkit Team 对象。

## 并发边界

`AsyncChatEvent` 可以异步触发。监听器通过 Paper 调度器在主线程读取玩家记分板和 Team 数据，并在异步线程使用其不可变快照。若服务器关闭或调度失败，插件记录警告并降级为无 Team 样式，保证聊天不会中断。渲染器自身无状态且线程安全。

## 构建与依赖

- 构建脚本使用 Kotlin DSL：`settings.gradle.kts` 和 `build.gradle.kts`。
- 使用 Kotlin JVM 插件，Kotlin 与 Java 编译目标均为 JVM 21。
- Paper API `1.20.1-R0.1-SNAPSHOT` 使用 `compileOnly`，不打包进插件。
- 使用 `kotlin("test")` 和 JUnit Platform 测试纯 Kotlin 逻辑。
- Gradle Wrapper 下载和 Maven Central 依赖解析使用腾讯云镜像；Paper API 仍从 PaperMC 官方仓库获取。

## 不在范围内

- 不增加配置文件、命令、权限或告示牌功能。
- Kotlin 标准库打入产物，确保服务器无需额外安装 Kotlin 运行时；Paper API 和 Adventure 由服务器提供，不打入产物。
- 不改变插件名、版本、主类包名或 `plugin.yml` 的 API 版本。
