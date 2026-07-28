package com.xiaobai.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

class LegacyColorParser(
    private val serializer: LegacyComponentSerializer = LegacyComponentSerializer.legacyAmpersand(),
) {
    fun parse(message: String): ParsedMessage {
        return parse(Component.text(message))
    }

    fun parse(message: Component): ParsedMessage {
        val (component, hasFormatting) = parseComponent(message)

        return ParsedMessage(component, hasFormatting)
    }

    private fun parseComponent(component: Component): ParsedComponent {
        val parsedChildren = component.children().map(::parseComponent)
        val hasFormattedChild = parsedChildren.any(ParsedComponent::hasFormatting)
        val children = parsedChildren.map(ParsedComponent::component)

        if (component !is TextComponent) {
            return ParsedComponent(component.children(children), hasFormattedChild)
        }

        val hasFormatting = LEGACY_FORMATTING_CODE.containsMatchIn(component.content())
        if (!hasFormatting) {
            val withDefaultColor = component
                .children(children)
                .colorIfAbsent(NamedTextColor.WHITE)
            return ParsedComponent(withDefaultColor, hasFormattedChild)
        }

        val serializedText = LEGACY_FORMATTING_CODE.replace(component.content()) { match ->
            "&${match.groupValues[1].lowercase()}"
        }
        val legacyComponent = serializer.deserialize(serializedText)
        val parsedText = legacyComponent
            .applyFallbackStyle(component.style())
            .children(legacyComponent.children() + children)

        return ParsedComponent(parsedText, true)
    }

    data class ParsedMessage(
        val component: Component,
        val hasFormatting: Boolean,
    )

    private data class ParsedComponent(
        val component: Component,
        val hasFormatting: Boolean,
    )

    private companion object {
        val LEGACY_FORMATTING_CODE = Regex("&([0-9a-fk-or])", RegexOption.IGNORE_CASE)
    }
}
