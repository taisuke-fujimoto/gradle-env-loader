package io.github.taisuke_fujimoto.gradle.plugin.envLoader.internal

/**
 * This logic is based on [npm dotenv](https://github.com/motdotla/dotenv)
 */

private val LINE_REGEX = Regex(
    """^\s*(?:export\s+)?([\w.-]+)(?:\s*=\s*?|:\s+?)(\s*'(?:\\'|[^'])*'|\s*"(?:\\"|[^"])*"|\s*`(?:\\`|[^`])*`|[^#\r\n]+)?\s*(?:#.*)?${'$'}""",
    RegexOption.MULTILINE
)

internal fun String.parseEnvContent(): Map<String, String> {
    val lines = replace(Regex("""\r\n?"""), "\n")

    return LINE_REGEX.findAll(lines).associate { match ->
        Pair(
            match.groupValues[1],
            match.groupValues[2]
                .trim()
                .let {
                    when (Quotes.detectEnclosed(it)) {
                        Quotes.SINGLE,
                        Quotes.BACK -> it.removeEnclosed()
                        Quotes.DOUBLE -> it.removeEnclosed().expandNewLines()
                        null -> it
                    }
                }
        )
    }
}

private enum class Quotes(val char: Char) {
    SINGLE('\''),
    DOUBLE('"'),
    BACK('`');

    fun enclose(value: String): Boolean =
        value.length > 1 && value.startsWith(char) && value.endsWith(char)

    companion object {
        fun detectEnclosed(value: String): Quotes? =
            enumValues<Quotes>().firstOrNull { it.enclose(value) }
    }
}

private fun String.removeEnclosed(): String =
    substring(1, length - 1)

private fun String.expandNewLines(): String =
    replace("\\n", "\n").replace("\\r", "\r")
