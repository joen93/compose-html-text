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

package io.joen.compose

import android.text.ParcelableSpan
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import io.joen.compose.ext.ANNOTATION_TAG_URL
import io.joen.compose.ext.disabled
import io.joen.compose.ext.openUri
import io.joen.compose.ext.toAnnotatedString

/**
 * A continent version of [ClickableText] component to be able to visualise HTML markup and handle click events on the anchored text(s).
 *
 * This is a shorthand of [ClickableText] with [AnnotatedString] to visualise text with HTML markup easily.
 *
 * It will replace all the supported HTML tags in the text with their matching [SpanStyle]d  text, as defined in [HtmlStyle]
 *
 * @param text The text to be displayed, including HTML tags
 * @param modifier Modifier to apply to this layout node.
 * @param style Style configuration for the text such as color, font, line height etc.
 * @param htmlStyle Style configuration for the HTML tags.
 * @param enabled Flag indicating wether this composable is enabled or not.
 * @param onError Callback that is executed when an error occurs upon opening an URI
 * @param altClick Callback that is executed when users click text that has no anchor.
 *
 * @see HtmlStyle
 * @see SpanStyle
 * @see ClickableText
 * @see AnnotatedString
 * @see ParcelableSpan
 */
@Composable
fun HtmlText(
    text: CharSequence,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    htmlStyle: HtmlStyle = HtmlStyle(),
    enabled: Boolean = true,
    onError: (Throwable) -> Unit = {},
    altClick: () -> Unit = {},
) {
    val annotatedString = text.toAnnotatedString(
        htmlStyle = if (enabled) htmlStyle else htmlStyle.disabled()
    )
    val uriHandler: UriHandler = LocalUriHandler.current
    val a11yActions = annotatedString.getCustomActions {
        return@getCustomActions if (!enabled) {
            false
        } else {
            uriHandler.openUri(uri = it, onError = onError)
        }
    }

    ClickableText(
        text = annotatedString,
        style = if (enabled) style else style.disabled(),
        modifier = modifier.semantics {
            try {
                customActions = a11yActions
            } catch (throwable: Throwable) {
                onError(throwable)
            }
        }
    ) { offset: Int ->
        if (!enabled) return@ClickableText
        annotatedString
            .getStringAnnotations(tag = ANNOTATION_TAG_URL, start = offset, end = offset)
            .firstOrNull()
            ?.let {
                uriHandler.openUri(uri = it.item, onError = onError)
            } ?: altClick.invoke()
    }
}

private fun AnnotatedString.getCustomActions(
    tag: String = ANNOTATION_TAG_URL,
    action: (String) -> Boolean,
): List<CustomAccessibilityAction> {
    return getStringAnnotations(tag = tag, start = 0, end = length)
        .map {
            CustomAccessibilityAction(
                label = substring(startIndex = it.start, endIndex = it.end),
                action = {
                    action(it.item)
                }
            )
        }
}
