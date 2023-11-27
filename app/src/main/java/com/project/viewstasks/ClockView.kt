package com.project.viewstasks

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.sin

class ClockView : View {
    private var height: Int = 0
    private var width: Int = 0
    private var padding: Int = 0
    private var fontSize: Int = 0
    private var numSpacing: Int = 0
    private var handCutting: Int = 0
    private var hourHandCutting: Int = 0
    private var radius: Int = 0
    private lateinit var paint: Paint
    private var isInit: Boolean = false
    private var numbers: Array<Int> = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private var rect: Rect = Rect()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun init() {
        height = getHeight()
        width = getWidth()
        padding = numSpacing + 50
        fontSize = 21
        val min = height.coerceAtMost(width)
        radius = min / 2 - padding
        handCutting = min / 20
        hourHandCutting = min / 7
        paint = Paint()
        isInit = true
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            init()
        }
        canvas.drawColor(Color.TRANSPARENT)
        drawDial(canvas)
        drawNumbers(canvas)
        drawHands(canvas)
        drawCenter(canvas)
        postInvalidateDelayed(500)
        invalidate()
    }

    private fun drawDial(canvas: Canvas) {
        paint.reset()
        paint.color = Color.parseColor("#FFFF9800")
        paint.strokeWidth = 4f
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (radius + padding - 10).toFloat(),
            paint
        )
    }

    private fun drawCenter(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), 13f, paint)
        paint.color = Color.parseColor("#3C6BAA")
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), 7f, paint)
        paint.color = Color.parseColor("#FFFF9800")
    }

    private fun drawNumbers(canvas: Canvas) {
        paint.strokeWidth = 2f
        paint.textSize = fontSize.toFloat()
        for (number in numbers) {
            val tmp = number.toString()
            paint.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (width / 2 + cos(angle) * radius - rect.width() / 2).toInt()
            val y = (height / 2 + sin(angle) * radius + rect.height() / 2).toInt()
            canvas.drawText(tmp, x.toFloat(), y.toFloat(), paint)
        }
        paint.strokeWidth = 4f
    }

    private fun drawHands(canvas: Canvas) {
        val c = Calendar.getInstance()
        var hour = c[Calendar.HOUR_OF_DAY].toFloat()
        hour = if (hour > 12) hour - 12 else hour
        drawHand(canvas, (hour + c[Calendar.MINUTE] / 60) * 5f, isHour = true, false)
        drawHand(canvas, c[Calendar.MINUTE].toFloat(), isHour = false, false)
        drawHand(canvas, c[Calendar.SECOND].toFloat(), isHour = false, true)
    }

    private fun drawHand(canvas: Canvas, loc: Float, isHour: Boolean, isSecond: Boolean) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        if (isSecond) paint.color = Color.parseColor("#FFB3144A")
        val handRadius =
            if (isHour) radius - handCutting - hourHandCutting else radius - handCutting
        canvas.drawLine(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 2 + cos(angle) * handRadius).toFloat(),
            (height / 2 + sin(angle) * handRadius).toFloat(),
            paint
        )
        if (!isSecond) {
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(
                (width / 2 + cos(angle) * handRadius / 1.3).toFloat(),
                (height / 2 + sin(angle) * handRadius / 1.3).toFloat(),
                8F,
                paint
            )
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#3C6BAA")
            canvas.drawCircle(
                (width / 2 + cos(angle) * handRadius / 1.3).toFloat(),
                (height / 2 + sin(angle) * handRadius / 1.3).toFloat(),
                6F,
                paint
            )
            paint.color = Color.parseColor("#FFFF9800")
        }

    }
}