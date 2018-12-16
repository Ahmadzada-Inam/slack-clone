package com.example.ahmadzada.slackclone.Utilities

import android.graphics.Color
import java.util.*

object Utilities {

    fun parsedColor(color: String) : Int {
        val strippedColor = color.replace("[", "")
                .replace("]", "").replace(",", "")

        var r = 0
        var g = 0
        var b = 0

        val scanner = Scanner(strippedColor)
        if (scanner.hasNext()) {
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }

        return Color.rgb(r, g, b)
    }
}