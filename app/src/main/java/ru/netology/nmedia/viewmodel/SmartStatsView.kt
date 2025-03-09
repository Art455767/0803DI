package ru.netology.nmedia.viewmodel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SmartStatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var percentages: List<Float> = emptyList()
    private val paint = Paint()

    var data: List<Float>
        get() = percentages
        set(value) {
            val total = value.sum()
            percentages = if (total > 0) {
                value.map { it / total }
            } else {
                List(value.size) { 0f }
            }
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val radius = Math.min(width, height) / 2

        var startAngle = -90f

        percentages.forEachIndexed { index, percentage ->
            val sweepAngle = percentage * 360
            paint.color = Color.HSVToColor(floatArrayOf(startAngle + sweepAngle / 2, 1f, 1f))
            canvas.drawArc(
                width / 2 - radius,
                height / 2 - radius,
                width / 2 + radius,
                height / 2 + radius,
                startAngle,
                sweepAngle,
                true,
                paint
            )
            startAngle += sweepAngle

            if (index == 0) {
                paint.color = Color.RED
                val x = (width / 2 + radius * Math.cos(Math.toRadians(startAngle - sweepAngle.toDouble()))).toFloat()
                val y = (height / 2 + radius * Math.sin(Math.toRadians(startAngle - sweepAngle.toDouble()))).toFloat()
                canvas.drawCircle(x, y, 10f, paint)
            }
        }
    }
}