@file:Suppress("ClassName")

package xyz.tf.gradle.plugin.envLoader.internal

import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.maps.shouldNotContainKey
import io.kotest.matchers.shouldBe

/**
 * test [parseEnvContent]
 */
class String_parseEnvContent : FreeSpec({
    "take the receiver as input and return the parsed result" - {
        // given
        val envFileContent = javaClass.getResource("/internal/parseEnvContent.env")!!.readText()

        // when
        val actual = envFileContent.parseEnvContent()

        // then
        "check map size" {
            actual shouldHaveSize 36
        }
        "sets basic environment variable" {
            actual["BASIC"] shouldBe "basic"
        }
        "reads after a skipped line" {
            actual["AFTER_LINE"] shouldBe "after_line"
        }
        "defaults empty values to empty string" {
            actual["EMPTY"] shouldBe ""
        }
        "defaults empty values to empty string (single quoted)" {
            actual["EMPTY_SINGLE_QUOTES"] shouldBe ""
        }
        "defaults empty values to empty string (double quoted)" {
            actual["EMPTY_DOUBLE_QUOTES"] shouldBe ""
        }
        "defaults empty values to empty string (back quoted)" {
            actual["EMPTY_BACKTICKS"] shouldBe ""
        }
        "escapes single quoted values" {
            actual["SINGLE_QUOTES"] shouldBe "single_quotes"
        }
        "respects surrounding spaces in single quotes" {
            actual["SINGLE_QUOTES_SPACED"] shouldBe "    single quotes    "
        }
        "escapes double quoted values" {
            actual["DOUBLE_QUOTES"] shouldBe "double_quotes"
        }
        "respects surrounding spaces in double quotes" {
            actual["DOUBLE_QUOTES_SPACED"] shouldBe "    double quotes    "
        }
        "respects double quotes inside single quotes" {
            actual["DOUBLE_QUOTES_INSIDE_SINGLE"] shouldBe "double \"quotes\" work inside single quotes"
        }
        "respects spacing for badly formed brackets" {
            actual["DOUBLE_QUOTES_WITH_NO_SPACE_BRACKET"] shouldBe "{ port: \$MONGOLAB_PORT}"
        }
        "respects single quotes inside double quotes" {
            actual["SINGLE_QUOTES_INSIDE_DOUBLE"] shouldBe "single 'quotes' work inside double quotes"
        }
        "respects backticks inside single quotes" {
            actual["BACKTICKS_INSIDE_SINGLE"] shouldBe "`backticks` work inside single quotes"
        }
        "respects backticks inside double quotes" {
            actual["BACKTICKS_INSIDE_DOUBLE"] shouldBe "`backticks` work inside double quotes"
        }
        "escapes back quoted values" {
            actual["BACKTICKS"] shouldBe "backticks"
        }
        "respects surrounding spaces in back quotes" {
            actual["BACKTICKS_SPACED"] shouldBe "    backticks    "
        }
        "respects double quotes inside backticks" {
            actual["DOUBLE_QUOTES_INSIDE_BACKTICKS"] shouldBe "double \"quotes\" work inside backticks"
        }
        "respects single quotes inside backticks" {
            actual["SINGLE_QUOTES_INSIDE_BACKTICKS"] shouldBe "single 'quotes' work inside backticks"
        }
        "respects double quotes and single quotes inside backticks" {
            actual["DOUBLE_AND_SINGLE_QUOTES_INSIDE_BACKTICKS"] shouldBe "double \"quotes\" and single 'quotes' work inside backticks"
        }
        "expands newlines but only if double quoted" {
            actual["EXPAND_NEWLINES"] shouldBe "expand\nnew\rlines"
        }
        "expands newlines but only if double quoted (not quoted)" {
            actual["DONT_EXPAND_UNQUOTED"] shouldBe "dontexpand\\nnewlines"
        }
        "expands newlines but only if double quoted (single quoted)" {
            actual["DONT_EXPAND_SINGLE_QUOTED"] shouldBe "dontexpand\\nnewlines"
        }
        "expands newlines but only if double quoted (back quoted)" {
            actual["DONT_EXPAND_BACK_QUOTED"] shouldBe "dontexpand\\nnewlines"
        }
        "ignores commented lines" {
            actual shouldNotContainKey "COMMENTS"
        }
        "ignores inline comments" {
            actual["INLINE_COMMENTS"] shouldBe "inline comments"
        }
        "ignores inline comments and respects # character inside of single quotes" {
            actual["INLINE_COMMENTS_SINGLE_QUOTES"] shouldBe "inline comments outside of #singlequotes"
        }
        "ignores inline comments and respects # character inside of double quotes" {
            actual["INLINE_COMMENTS_DOUBLE_QUOTES"] shouldBe "inline comments outside of #doublequotes"
        }
        "ignores inline comments and respects # character inside of backticks" {
            actual["INLINE_COMMENTS_BACKTICKS"] shouldBe "inline comments outside of #backticks"
        }
        "treats # character as start of comment" {
            actual["INLINE_COMMENTS_SPACE"] shouldBe "inline comments start with a"
        }
        "respects equals signs in values" {
            actual["EQUAL_SIGNS"] shouldBe "equals=="
        }
        "retains inner quotes" {
            actual["RETAIN_INNER_QUOTES"] shouldBe "{\"foo\": \"bar\"}"
        }
        "retains inner quotes (single quoted)" {
            actual["RETAIN_INNER_QUOTES_AS_STRING"] shouldBe "{\"foo\": \"bar\"}"
        }
        "retains inner quotes (back quoted)" {
            actual["RETAIN_INNER_QUOTES_AS_BACKTICKS"] shouldBe "{\"foo\": \"bar's\"}"
        }
        "retains spaces in string" {
            actual["TRIM_SPACE_FROM_UNQUOTED"] shouldBe "some spaced out string"
        }
        "parses email addresses completely" {
            actual["USERNAME"] shouldBe "therealnerdybeast@example.tld"
        }
        "parses keys and values surrounded by spaces" {
            actual["SPACED_KEY"] shouldBe "parsed"
        }
    }

    "can parse multiple types of line endings" - {
        val expected = mapOf(
            "SERVER" to "localhost",
            "PASSWORD" to "password",
            "DB" to "tests"
        )

        withData(
            mapOf(
                "CR" to "SERVER=localhost\rPASSWORD=password\rDB=tests\r",
                "LF" to "SERVER=localhost\nPASSWORD=password\nDB=tests\n",
                "CRLF" to "SERVER=localhost\r\nPASSWORD=password\r\nDB=tests\r\n"
            )
        ) { input ->
            val actual = input.parseEnvContent()

            actual shouldContainExactly expected
        }
    }
})
