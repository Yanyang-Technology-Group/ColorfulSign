# ColorfulSign Kotlin 重写实现计划

> **面向 AI 代理的工作者：** 必需子技能：使用 superpowers:subagent-driven-development（推荐）或 superpowers:executing-plans 逐任务实现此计划。步骤使用复选框（`- [ ]`）语法来跟踪进度。

**目标：** 用 Kotlin 和 Paper Adventure 聊天 API 重写插件，并保留既有聊天格式功能。

**架构：** 插件入口只负责生命周期和监听器注册，监听器只负责 Paper 事件接入，纯 Kotlin 渲染器和颜色解析器分别处理格式组合与旧式颜色码。队伍信息在监听器中转换为不可变样式快照，避免渲染器依赖 Bukkit。

**技术栈：** Kotlin JVM、Gradle Kotlin DSL、Paper API 1.20.1、Adventure、Kotlin Test、JUnit Platform。

---

## 文件职责

- `settings.gradle.kts`：插件解析仓库和项目名称。
- `build.gradle.kts`：Kotlin、Paper、测试和资源处理构建配置。
- `src/main/kotlin/com/xiaobai/ColorfulSignPlugin.kt`：插件生命周期入口。
- `src/main/kotlin/com/xiaobai/chat/ChatListener.kt`：`AsyncChatEvent` 集成、主线程 Team 快照和降级日志。
- `src/main/kotlin/com/xiaobai/chat/ChatMessageRenderer.kt`：最终聊天组件渲染。
- `src/main/kotlin/com/xiaobai/chat/LegacyColorParser.kt`：有效 `&` 颜色和格式代码识别、反序列化。
- `src/main/kotlin/com/xiaobai/chat/TeamChatStyle.kt`：不可变队伍样式数据。
- `src/test/kotlin/com/xiaobai/chat/LegacyColorParserTest.kt`：颜色码识别与默认颜色测试。
- `src/test/kotlin/com/xiaobai/chat/ChatMessageRendererTest.kt`：格式顺序、无 Team 与颜色样式测试。
- `src/main/resources/plugin.yml`：Kotlin 主类引用。

### 任务 1：转换 Kotlin DSL 构建配置

- [ ] 删除 `build.gradle` 和 `settings.gradle`。
- [ ] 新建 `settings.gradle.kts`，使用腾讯云 Maven 镜像、`gradlePluginPortal()` 和项目名 `ColorfulSign`。
- [ ] 新建 `build.gradle.kts`，应用 `kotlin("jvm")`，配置 JVM 21、Paper API、Kotlin Test、JUnit Platform 和 `plugin.yml` 版本展开。
- [ ] 运行 `./gradlew tasks`，预期 Kotlin 任务可被 Gradle 发现。

### 任务 2：以失败测试定义颜色解析规则

- [ ] 新建 `LegacyColorParserTest`，断言 `&aHello` 被识别且包含绿色样式。
- [ ] 增加断言：`A & B` 未被识别且输出完全白色。
- [ ] 运行 `./gradlew test --tests com.xiaobai.chat.LegacyColorParserTest`，预期因 `LegacyColorParser` 缺失失败。
- [ ] 实现 `LegacyColorParser`，用 `LegacyComponentSerializer.legacyAmpersand()` 解析，使用正则仅识别 `&[0-9a-fk-or]`（忽略大小写）。
- [ ] 重跑对应测试，预期通过。

### 任务 3：以失败测试定义聊天组件顺序

- [ ] 新建 `ChatMessageRendererTest`，断言最终纯文本为 `[P] Alex [S]: Hello`。
- [ ] 增加断言：无 Team 时玩家名和消息均为白色。
- [ ] 运行 `./gradlew test --tests com.xiaobai.chat.ChatMessageRendererTest`，预期因渲染器类型缺失失败。
- [ ] 实现 `TeamChatStyle` 和 `ChatMessageRenderer`，按前缀、玩家名、重置、后缀、分隔符、消息的固定顺序组合 `Component`。
- [ ] 重跑对应测试，预期通过。

### 任务 4：接入 Paper 插件生命周期

- [ ] 实现 `ColorfulSignPlugin`，在 `onEnable` 注册 `ChatListener` 并记录启用日志。
- [ ] 实现 `ChatListener`，监听 `AsyncChatEvent`，从玩家 Scoreboard 获取 Team，将 Team 转换为 `TeamChatStyle` 后委托渲染器。
- [ ] 更新 `plugin.yml` 的 `main` 为 `com.xiaobai.ColorfulSignPlugin`，保留插件标识和 API 版本。
- [ ] 删除 `src/main/java` 下旧 Java 实现。
- [ ] 运行 `./gradlew clean build`，预期构建和全部测试通过。

### 任务 5：检查产物

- [ ] 运行 `jar tf build/libs/ColorfulSign-1.4.jar`。
- [ ] 确认存在 `com/xiaobai/ColorfulSignPlugin.class`、聊天包 class 与 `plugin.yml`。
- [ ] 确认产物不包含 `org/bukkit/`、`net/kyori/` 或 Kotlin 标准库 class。
