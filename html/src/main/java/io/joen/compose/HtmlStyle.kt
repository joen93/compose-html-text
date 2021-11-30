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

import android.graphics.Typeface
import android.text.ParcelableSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.ScaleXSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.em
import io.joen.compose.ext.disabled

private val DefaultLinkColor = Color.Cyan
private val DefaultBackgroundColor = Color.Yellow
private val DefaultForegroundColor = Color.Green

/**
 * Styling configuration for a [HtmlText].
 *
 * This class basically groups multiple [SpanStyle]s for every by the
 * [HtmlText] composable supported [ParcelableSpan].
 *
 * For each supported [ParcelableSpan] there's a [SpanStyle] parameter.
 *
 * @param normalSpanStyle The [SpanStyle] for [StyleSpan] with style [Typeface.NORMAL]
 * @param boldSpanStyle The [SpanStyle] for [StyleSpan] with style [Typeface.BOLD]
 * @param italicSpanStyle The [SpanStyle] for [StyleSpan] with style [Typeface.ITALIC]
 * @param boldItalicSpanStyle The [SpanStyle] for [StyleSpan] with style [Typeface.BOLD_ITALIC]
 * @param underlineSpanStyle The [SpanStyle] for [UnderlineSpan]
 * @param strikethroughSpanStyle The [SpanStyle] for [StrikethroughSpan]
 * @param urlSpanStyle the [SpanStyle] for [URLSpan]
 * @param subscriptSpanStyle The [SpanStyle] for [SubscriptSpan]
 * @param superscriptSpanStyle The [SpanStyle] for [SuperscriptSpan]
 * @param foregroundColorSpanStyle The [SpanStyle] for [ForegroundColorSpan]
 * @param backgroundColorSpanStyle The [SpanStyle] for [BackgroundColorSpan]
 * @param scaleXSpanStyle The [SpanStyle] for [ScaleXSpan]
 * @param relativeSizeSpanStyle The [SpanStyle] for [RelativeSizeSpan]
 *
 * @see SpanStyle
 * @see ParcelableSpan
 * @see AnnotatedString
 * @see HtmlText
 */
class HtmlStyle(
    val normalSpanStyle: SpanStyle = SpanStyle(
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
        textDecoration = TextDecoration.None,
    ),
    val boldSpanStyle: SpanStyle = SpanStyle(
        fontWeight = FontWeight.Bold,
    ),
    val italicSpanStyle: SpanStyle = SpanStyle(
        fontStyle = FontStyle.Italic,
    ),
    val boldItalicSpanStyle: SpanStyle = SpanStyle(
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
    ),
    val underlineSpanStyle: SpanStyle = SpanStyle(
        textDecoration = TextDecoration.Underline,
    ),
    val strikethroughSpanStyle: SpanStyle = SpanStyle(
        textDecoration = TextDecoration.LineThrough,
    ),
    val urlSpanStyle: SpanStyle = SpanStyle(
        color = DefaultLinkColor,
        textDecoration = TextDecoration.Underline,
    ),
    val subscriptSpanStyle: SpanStyle = SpanStyle(
        baselineShift = BaselineShift.Subscript,
    ),
    val superscriptSpanStyle: SpanStyle = SpanStyle(
        baselineShift = BaselineShift.Superscript,
    ),
    val foregroundColorSpanStyle: SpanStyle = SpanStyle(
        color = DefaultForegroundColor,
    ),
    val backgroundColorSpanStyle: SpanStyle = SpanStyle(
        background = DefaultBackgroundColor,
    ),
    val scaleXSpanStyle: SpanStyle = SpanStyle(
        textGeometricTransform = TextGeometricTransform(scaleX = 2f),
    ),
    val relativeSizeSpanStyle: SpanStyle = SpanStyle(
        fontSize = 2.em,
    ),
) {

    /**
     * Get the matching [SpanStyle] for the given [ParcelableSpan]
     * @param parcelableSpan The span for which a [SpanStyle] is needed
     * @return a matching [SpanStyle] that was defined upon creation of the [HtmlStyle], or null if
     * there's no [SpanStyle] defined for the given [ParcelableSpan]
     */
    fun getStyleFor(parcelableSpan: ParcelableSpan): SpanStyle? =
        when (parcelableSpan) {
            is StyleSpan -> {
                when (parcelableSpan.style) {
                    Typeface.NORMAL -> normalSpanStyle
                    Typeface.BOLD -> boldSpanStyle
                    Typeface.ITALIC -> italicSpanStyle
                    Typeface.BOLD_ITALIC -> boldItalicSpanStyle
                    else -> normalSpanStyle
                }
            }
            is UnderlineSpan -> underlineSpanStyle
            is StrikethroughSpan -> strikethroughSpanStyle
            is URLSpan -> urlSpanStyle
            is SubscriptSpan -> subscriptSpanStyle
            is SuperscriptSpan -> superscriptSpanStyle
            is ForegroundColorSpan -> foregroundColorSpanStyle
            is BackgroundColorSpan -> backgroundColorSpanStyle
            is ScaleXSpan -> scaleXSpanStyle
            is RelativeSizeSpan -> relativeSizeSpanStyle
            else -> null
        }

    /**
     * Returns a new html style that is a combination of this style and the given [other] style.
     *
     * [other] html style's null or inherit properties are replaced with the non-null properties of
     * this html style. Another way to think of it is that the "missing" properties of the [other]
     * style are _filled_ by the properties of this style.
     *
     * If the given html style is null, returns this html style.
     */
    fun merge(other: HtmlStyle? = null): HtmlStyle {
        if (other == null) return this

        return HtmlStyle(
            normalSpanStyle = other.normalSpanStyle.merge(this.normalSpanStyle),
            boldSpanStyle = other.boldSpanStyle.merge(this.boldSpanStyle),
            italicSpanStyle = other.italicSpanStyle.merge(this.italicSpanStyle),
            boldItalicSpanStyle = other.boldItalicSpanStyle.merge(this.boldItalicSpanStyle),
            underlineSpanStyle = other.underlineSpanStyle.merge(this.underlineSpanStyle),
            strikethroughSpanStyle = other.strikethroughSpanStyle.merge(this.strikethroughSpanStyle),
            urlSpanStyle = other.urlSpanStyle.merge(this.urlSpanStyle),
            subscriptSpanStyle = other.subscriptSpanStyle.merge(this.subscriptSpanStyle),
            superscriptSpanStyle = other.superscriptSpanStyle.merge(this.superscriptSpanStyle),
            foregroundColorSpanStyle = other.foregroundColorSpanStyle.merge(this.foregroundColorSpanStyle),
            backgroundColorSpanStyle = other.backgroundColorSpanStyle.merge(this.backgroundColorSpanStyle),
            scaleXSpanStyle = other.scaleXSpanStyle.merge(this.scaleXSpanStyle),
            relativeSizeSpanStyle = other.relativeSizeSpanStyle.merge(this.relativeSizeSpanStyle),
        )
    }

    /**
     * Plus operator overload that applies a [merge].
     */
    operator fun plus(other: HtmlStyle): HtmlStyle = this.merge(other)

    fun copy(
        normalSpanStyle: SpanStyle = this.normalSpanStyle,
        boldSpanStyle: SpanStyle = this.boldSpanStyle,
        italicSpanStyle: SpanStyle = this.italicSpanStyle,
        boldItalicSpanStyle: SpanStyle = this.boldItalicSpanStyle,
        underlineSpanStyle: SpanStyle = this.underlineSpanStyle,
        strikethroughSpanStyle: SpanStyle = this.strikethroughSpanStyle,
        urlSpanStyle: SpanStyle = this.urlSpanStyle,
        subscriptSpanStyle: SpanStyle = this.subscriptSpanStyle,
        superscriptSpanStyle: SpanStyle = this.superscriptSpanStyle,
        foregroundColorSpanStyle: SpanStyle = this.foregroundColorSpanStyle,
        backgroundColorSpanStyle: SpanStyle = this.backgroundColorSpanStyle,
        scaleXSpanStyle: SpanStyle = this.scaleXSpanStyle,
        relativeSizeSpanStyle: SpanStyle = this.relativeSizeSpanStyle,
    ): HtmlStyle {
        return HtmlStyle(
            normalSpanStyle = normalSpanStyle,
            boldSpanStyle = boldSpanStyle,
            italicSpanStyle = italicSpanStyle,
            boldItalicSpanStyle = boldItalicSpanStyle,
            underlineSpanStyle = underlineSpanStyle,
            strikethroughSpanStyle = strikethroughSpanStyle,
            urlSpanStyle = urlSpanStyle,
            subscriptSpanStyle = subscriptSpanStyle,
            superscriptSpanStyle = superscriptSpanStyle,
            foregroundColorSpanStyle = foregroundColorSpanStyle,
            backgroundColorSpanStyle = backgroundColorSpanStyle,
            scaleXSpanStyle = scaleXSpanStyle,
            relativeSizeSpanStyle = relativeSizeSpanStyle,
        )
    }

    @Composable
    fun disabled(): HtmlStyle = this.copy(
        normalSpanStyle = this.normalSpanStyle.disabled(),
        boldSpanStyle = this.boldSpanStyle.disabled(),
        italicSpanStyle = this.italicSpanStyle.disabled(),
        boldItalicSpanStyle = this.boldItalicSpanStyle.disabled(),
        underlineSpanStyle = this.underlineSpanStyle.disabled(),
        strikethroughSpanStyle = this.strikethroughSpanStyle.disabled(),
        urlSpanStyle = this.urlSpanStyle.disabled(),
        subscriptSpanStyle = this.subscriptSpanStyle.disabled(),
        superscriptSpanStyle = this.superscriptSpanStyle.disabled(),
        foregroundColorSpanStyle = this.foregroundColorSpanStyle.disabled(),
        backgroundColorSpanStyle = this.backgroundColorSpanStyle.disabled(),
        scaleXSpanStyle = this.scaleXSpanStyle.disabled(),
        relativeSizeSpanStyle = this.relativeSizeSpanStyle.disabled(),
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HtmlStyle) return false

        if (normalSpanStyle != other.normalSpanStyle) return false
        if (boldSpanStyle != other.boldSpanStyle) return false
        if (italicSpanStyle != other.italicSpanStyle) return false
        if (boldItalicSpanStyle != other.boldItalicSpanStyle) return false
        if (underlineSpanStyle != other.underlineSpanStyle) return false
        if (strikethroughSpanStyle != other.strikethroughSpanStyle) return false
        if (urlSpanStyle != other.urlSpanStyle) return false
        if (subscriptSpanStyle != other.subscriptSpanStyle) return false
        if (superscriptSpanStyle != other.superscriptSpanStyle) return false
        if (foregroundColorSpanStyle != other.foregroundColorSpanStyle) return false
        if (backgroundColorSpanStyle != other.backgroundColorSpanStyle) return false
        if (scaleXSpanStyle != other.scaleXSpanStyle) return false
        if (relativeSizeSpanStyle != other.relativeSizeSpanStyle) return false

        return true
    }

    override fun hashCode(): Int {
        var result = normalSpanStyle.hashCode()
        result = 31 * result + boldSpanStyle.hashCode()
        result = 31 * result + italicSpanStyle.hashCode()
        result = 31 * result + boldItalicSpanStyle.hashCode()
        result = 31 * result + underlineSpanStyle.hashCode()
        result = 31 * result + strikethroughSpanStyle.hashCode()
        result = 31 * result + urlSpanStyle.hashCode()
        result = 31 * result + subscriptSpanStyle.hashCode()
        result = 31 * result + superscriptSpanStyle.hashCode()
        result = 31 * result + foregroundColorSpanStyle.hashCode()
        result = 31 * result + backgroundColorSpanStyle.hashCode()
        result = 31 * result + scaleXSpanStyle.hashCode()
        result = 31 * result + relativeSizeSpanStyle.hashCode()
        return result
    }

    override fun toString(): String {
        return "HtmlStyle(" +
            "normalSpanStyle=$normalSpanStyle, " +
            "boldSpanStyle=$boldSpanStyle, " +
            "italicSpanStyle=$italicSpanStyle, " +
            "boldItalicSpanStyle=$boldItalicSpanStyle, " +
            "underlineSpanStyle=$underlineSpanStyle, " +
            "strikethroughSpanStyle=$strikethroughSpanStyle, " +
            "urlSpanStyle=$urlSpanStyle, " +
            "subscriptSpanStyle=$subscriptSpanStyle, " +
            "superscriptSpanStyle=$superscriptSpanStyle, " +
            "foregroundColorSpanStyle=$foregroundColorSpanStyle, " +
            "backgroundColorSpanStyle=$backgroundColorSpanStyle, " +
            "scaleXSpanStyle=$scaleXSpanStyle, " +
            "relativeSizeSpanStyle=$relativeSizeSpanStyle, " +
            ")"
    }
}
