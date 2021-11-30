/*
 * Copyright (c) 2021-present Jeroen Debois
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.joen.compose.ext

import android.text.ParcelableSpan
import android.text.Spanned
import android.text.style.URLSpan
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.core.text.getSpans
import io.joen.compose.HtmlStyle

internal const val ANNOTATION_TAG_URL = "url"

internal fun Spanned.toAnnotatedString(htmlStyle: HtmlStyle): AnnotatedString {
    return buildAnnotatedString {
        append(this@toAnnotatedString.toString())
        this@toAnnotatedString.getSpanRanges<ParcelableSpan>(htmlStyle)
            .forEach { (span, style, start, end) ->
                when (span) {
                    is URLSpan -> {
                        addStyle(style, start, end)
                        addStringAnnotation(ANNOTATION_TAG_URL, span.url, start, end)
                    }
                    else -> addStyle(style, start, end)
                }
            }
    }
}

private inline fun <reified T : ParcelableSpan> Spanned.getSpanRanges(
    htmlStyle: HtmlStyle,
    start: Int = 0,
    end: Int = length,
): List<SpanRange<T>> {
    return getSpans<T>(start, end)
        .mapNotNull {
            SpanRange(
                span = it,
                style = htmlStyle.getStyleFor(it) ?: return@mapNotNull null,
                start = getSpanStart(it),
                end = getSpanEnd(it)
            )
        }
}

/**
 * Utility data class to combine the [ParcelableSpan], [SpanStyle], start and end of the Span.
 */
private data class SpanRange<T : ParcelableSpan>(
    val span: T,
    val style: SpanStyle,
    val start: Int,
    val end: Int,
) {
    init {
        require((start <= end)) { "Start can't be bigger than end: start=$start end=$end" }
    }
}
