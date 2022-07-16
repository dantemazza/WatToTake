package com.example.wat2take.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

//private val DarkColorPalette = darkColors(
//    primary = Gold200,
//    primaryVariant = Gold700,
//    secondary = Magenta200,
//    onPrimary = VeryDarkGrey,
//    onSecondary = VeryDarkGrey,
//    background = VeryDarkGrey,
//    surface = VeryDarkGrey,
//    onBackground = SoftWhite,
//    onSurface = SoftWhite,
//)

private val LightColorPalette = lightColors(
    primary = Gold500,
    primaryVariant = Gold700,
    secondary = Magenta200,
    onPrimary = VeryDarkGrey,
    onSecondary = VeryDarkGrey,
    background = Color.White,
    surface = Color.White,
    onBackground = VeryDarkGrey,
    onSurface = VeryDarkGrey,
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun Wat2TakeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
//    val colors = if (darkTheme) {
//        DarkColorPalette
//    } else {
//        LightColorPalette
//    }
    val colors = LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = {
            val defaultTextStyle = LocalTextStyle.current.copy(
                color = colors.onBackground
            )
            CompositionLocalProvider(LocalTextStyle provides defaultTextStyle) {
                content()
            }
        },
        shapes = Shapes,
    )
}