# xmeta-support

![Build](https://github.com/marioplus/xmeta-support/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

## 待办事项

- [x] 手动触发标记 xmeta_gen 文件夹为 生成的源
- [ ] 自动触发标记 xmeta_gen 文件夹为 生成的源
- [ ] 拓展 yaml 语言，识别 xmeta 框架的特殊 .xmeta.yaml
- [ ] 类似 markdown 插件拥有窗口预览 .xmeta.yaml 生成的 Java 文件
- [ ] 预览的 Java 支持 IDE 的语法高亮
- [ ] 可以手动生成 Java 文件
- [ ] 支持编辑 Java 同步生成 yaml
- [ ] 支持 GitHub Actions

<!-- Plugin description -->

## Xmeta Support

用于将每个模块下的 xmeta 生成的目录标记为不同类型的目录，支持5.3.0之前和之后的两种模式

- 5.3.0之前 <kbd>tools</kbd> > <kbd>Xmeta Support</kbd> </kbd> > <kbd> </kbd> 5.3.0之前 <kbd>
- 5.3.0之后 <kbd>tools</kbd> > <kbd>Xmeta Support</kbd> </kbd> > <kbd> </kbd> 5.3.0之后 <kbd>

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "
  xmeta-support"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/marioplus/xmeta-support/releases/latest) and install it manually
  using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
