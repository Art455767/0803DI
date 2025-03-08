package ru.netology.nmedia.viewmodel

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class RotatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private var currentAngle = 0f

    init {
        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL

        startRotationAnimation()
    }

    private fun startRotationAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 360f)
        animator.duration = 2000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener { animation ->
            currentAngle = animation.animatedValue as Float
            invalidate()
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.rotate(currentAngle, width / 2f, height / 2f)
        canvas.drawCircle(width / 2f, height / 2f, 100f, paint)
        canvas.restore()

        paint.color = Color.BLACK
        paint.textSize = 50f
        val text = "Rotating Text"
        val textWidth = paint.measureText(text)
        canvas.drawText(text, (width - textWidth) / 2, (height / 2).toFloat(), paint)
    }
}