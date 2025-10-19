# Intellij Translation Subplugin Sample

[中文说明](/README_CN.md)

This project is a sample subplugin based on the [Intellij Translation Plugin](https://github.com/YiiGuxing/TranslationPlugin).  
**Intellij Translation Plugin** is a translation plugin for the IntelliJ IDE platform, providing multilingual translation capabilities within the IDE.

As a subplugin, this sample demonstrates how to extend the translation features provided by the main plugin.  
Developers can use this project as a reference to create their own sub-plugins, integrating and expanding the translation services to meet custom requirements.

## Features

- Depends on and extends the translation capabilities of **Intellij Translation Plugin**.
- Serves as a reference for developing custom translation-related features.

#### Sample Features

1. Documentation translation:  
   ![Documentation Translation](images/screenshot_1.png)

2. Ignore specific elements during documentation translation:  
   ![Documentation Element Filter](images/screenshot_2.png)

## Notes

1. The open API of **Intellij Translation Plugin** is still under development, so the extensible features for sub-plugins are limited.
2. This project is only a sample for subplugin development, you may need to adjust it according to your actual requirements.
3. This project is not a template project and is not recommended for direct secondary development. Please use the official plugin template project [intellij-platform-plugin-template](https://github.com/JetBrains/intellij-platform-plugin-template) as the base, and refer to this project for guidance.

## Open API

The open API of **Intellij Translation Plugin** is located in the [`cn.yiiguxing.plugin.translate.openapi`][openapi-package] package. Currently available services and extension points are as follows.

### Service List

- [`DocumentationTranslationService`][DocumentationTranslationService]: Provides documentation translation service

### Extension Point List

> Default extensions namespace (defaultExtensionNs): `cn.yiiguxing.plugin.translate`

| Extension Point               | Implementation                                             |
|-------------------------------|------------------------------------------------------------|
| `documentation.elementFilter` | [`DocumentationElementFilter`][DocumentationElementFilter] |

### Source Code

The source code of the open API for **Intellij Translation Plugin** is bundled in the plugin ZIP distribution.  
When you navigate to the relevant classes, if the IDE prompts that the source code cannot be found, you can manually add the source code:  
Click the `Choose Sources...` link in the editor banner notification, and select the source code jar package (`src/*-src.jar`) bundled in the plugin ZIP distribution.  
Note: At this point, the IDE has already automatically navigated to the plugin ZIP distribution.


[openapi-package]: https://github.com/YiiGuxing/TranslationPlugin/tree/master/src/main/kotlin/cn/yiiguxing/plugin/translate/openapi
[DocumentationTranslationService]: https://github.com/YiiGuxing/TranslationPlugin/blob/master/src/main/kotlin/cn/yiiguxing/plugin/translate/openapi/documentation/DocumentationTranslationService.kt
[DocumentationElementFilter]: https://github.com/YiiGuxing/TranslationPlugin/blob/master/src/main/kotlin/cn/yiiguxing/plugin/translate/openapi/documentation/DocumentationElementFilter.kt
