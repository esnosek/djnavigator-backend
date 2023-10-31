package dev.nos.djnavigator.collection.controller


import groovy.json.JsonSlurper
import net.javacrumbs.jsonunit.assertj.JsonAssertions
import org.hamcrest.text.MatchesPattern

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

import static groovy.json.JsonOutput.toJson
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson

class TestUtils {
    static String formatDate(LocalDateTime date) {
        final var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return date.format(formatter)
    }

    static String parsedJson(String json) {
        def parsedJson = new JsonSlurper().parseText(json)
        return toJson(parsedJson)
    }

    static def assertJson(String actual, String expected) {
        def dateMatcher = new MatchesPattern(Pattern.compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z\$"))
        def idMatcher = new MatchesPattern(Pattern.compile("^[0-9A-Za-z_-]{22}\$"))
        assertThatJson(actual)
                .withConfiguration(c -> c
                        .withMatcher("dateMatcher", dateMatcher)
                        .withMatcher("idMatcher", idMatcher)
                )
                .isEqualTo(JsonAssertions.json(expected))
    }
}
