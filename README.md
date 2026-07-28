# ColorfulSign

一个 Paper 服务器聊天美化插件，自动为玩家聊天消息添加 Team 前缀、后缀和队伍颜色，并支持 `&` 旧式格式码。

## 功能

- 自动显示玩家所在 Scoreboard Team 的前缀和后缀
- 玩家名使用其 Team 颜色
- 支持 `&0` 到 `&f` 颜色码和 `&k` 到 `&o`、`&r` 格式码
- 普通 `&` 不会误触发颜色逻辑

## 兼容性

- Paper 1.20.1+
- Java 21+

## 构建

```bash
./gradlew clean build
```

产物位于 `build/libs/ColorfulSign-*.jar`。

## 许可证

[GPLv3](LICENSE)
