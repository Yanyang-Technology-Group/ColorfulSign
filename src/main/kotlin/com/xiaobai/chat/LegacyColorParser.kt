package com.xiaobai.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration

class LegacyColorParser {
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

        val rawText = component.content()
        val matches = FORMATTING_CODE.findAll(rawText).toList()

        if (matches.isEmpty()) {
            return ParsedComponent(
                component.children(children).colorIfAbsent(NamedTextColor.WHITE),
                hasFormattedChild,
            )
        }

        val segments = buildSegments(rawText, matches)
        var currentStyle = Style.empty()
        val builtChildren = mutableListOf<Component>()

        for (segment in segments) {
            if (segment.code != null) {
                currentStyle = applyCode(currentStyle, segment.code)
            } else if (segment.text.isNotEmpty()) {
                builtChildren.add(Component.text(segment.text, currentStyle))
            }
        }

        val result = Component.empty()
            .style(component.style().color(currentStyle.color()))
            .children(builtChildren + children)

        return ParsedComponent(result, true)
    }

    private fun buildSegments(text: String, matches: List<MatchResult>): List<Segment> {
        val segments = mutableListOf<Segment>()
        var lastIndex = 0

        for (match in matches) {
            val matchStart = match.range.first
            val matchEnd = match.range.last + 1
            val code = match.groupValues[1].lowercase()[0]

            if (matchStart > lastIndex) {
                segments.add(Segment(text.substring(lastIndex, matchStart), null))
            }
            segments.add(Segment("", code))
            lastIndex = matchEnd
        }

        if (lastIndex < text.length) {
            segments.add(Segment(text.substring(lastIndex), null))
        }

        return segments
    }

    private fun applyCode(style: Style, code: Char): Style {
        val color = COLOR_MAP[code]
        if (color != null) {
            return Style.style(color)
        }

        if (code == 'r') {
            return Style.empty()
        }

        val decoration = FORMAT_MAP[code] ?: return style
        return style.decorate(decoration)
    }

    data class ParsedMessage(
        val component: Component,
        val hasFormatting: Boolean,
    )

    private data class ParsedComponent(
        val component: Component,
        val hasFormatting: Boolean,
    )

    private data class Segment(
        val text: String,
        val code: Char?,
    )

    private companion object {
        val FORMATTING_CODE = Regex("&([0-9a-fk-or])", RegexOption.IGNORE_CASE)

        val COLOR_MAP = mapOf(
            '0' to NamedTextColor.BLACK,
            '1' to NamedTextColor.DARK_BLUE,
            '2' to NamedTextColor.DARK_GREEN,
            '3' to NamedTextColor.DARK_AQUA,
            '4' to NamedTextColor.DARK_RED,
            '5' to NamedTextColor.DARK_PURPLE,
            '6' to NamedTextColor.GOLD,
            '7' to NamedTextColor.GRAY,
            '8' to NamedTextColor.DARK_GRAY,
            '9' to NamedTextColor.BLUE,
            'a' to NamedTextColor.GREEN,
            'b' to NamedTextColor.AQUA,
            'c' to NamedTextColor.RED,
            'd' to NamedTextColor.LIGHT_PURPLE,
            'e' to NamedTextColor.YELLOW,
            'f' to NamedTextColor.WHITE,
        )

        val FORMAT_MAP = mapOf(
            'k' to TextDecoration.OBFUSCATED,
            'l' to TextDecoration.BOLD,
            'm' to TextDecoration.STRIKETHROUGH,
            'n' to TextDecoration.UNDERLINED,
            'o' to TextDecoration.ITALIC,
        )
    }
}
