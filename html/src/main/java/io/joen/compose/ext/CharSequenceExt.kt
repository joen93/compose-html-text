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

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import io.joen.compose.HtmlStyle

internal fun CharSequence.toAnnotatedString(
    htmlStyle: HtmlStyle = HtmlStyle(),
): AnnotatedString {
    return when (this) {
        is Spanned -> this.toAnnotatedString(htmlStyle = htmlStyle)
        is String -> {
            val htmlSpan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(this)
            }
            htmlSpan.toAnnotatedString(htmlStyle = htmlStyle)
        }
        else -> buildAnnotatedString { append(this@toAnnotatedString.toString()) }
    }
}
