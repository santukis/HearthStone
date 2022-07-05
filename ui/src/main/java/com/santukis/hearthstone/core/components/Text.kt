package com.santukis.hearthstone.core.components

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat

@Composable
fun AutoSizeText(
    text: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    var scaledTextStyle by remember { mutableStateOf(textStyle) }
    var readyToDraw by remember { mutableStateOf(false) }

    Text(
        text,
        modifier.drawWithContent {
            if (readyToDraw) {
                drawContent()
            }
        },
        color = color,
        style = scaledTextStyle,
        softWrap = false,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowWidth) {
                scaledTextStyle =
                    scaledTextStyle.copy(fontSize = scaledTextStyle.fontSize * 0.9)

            } else {
                readyToDraw = true
            }
        }
    )
}

@Composable
fun HtmlText(
    html: String,
    modifier: Modifier = Modifier,
    textSize: Float = 20f,
    textAlignment: Int = View.TEXT_ALIGNMENT_CENTER,
    typeface: Typeface = Typeface.DEFAULT_BOLD,
    textColor: Int = android.graphics.Color.BLACK,
    font: Int = 0
) {
    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context) },
        update = { textView ->
            textView.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
            textView.textSize = textSize
            textView.textAlignment = textAlignment
            textView.typeface = if (font != 0) {
                ResourcesCompat.getFont(textView.context, font)
            } else {
                typeface
            }
            textView.setTextColor(textColor)
        }
    )
}