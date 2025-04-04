package com.practicum.playlistmaker.ui.media

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.practicum.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var isPlaying = false
    private var playIconRes: Int = R.drawable.ic_button_play
    private var pauseIconRes: Int = R.drawable.ic_button_stop
    private var buttonRect = RectF()
    private var drawable = ContextCompat.getDrawable(context, playIconRes)

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.PlaybackButtonView)
            playIconRes = typedArray.getResourceId(R.styleable.PlaybackButtonView_playIcon, R.drawable.ic_button_play)
            pauseIconRes = typedArray.getResourceId(R.styleable.PlaybackButtonView_pauseIcon, R.drawable.ic_button_stop)
            typedArray.recycle()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            performClick()
            toggleState()
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

     fun toggleState() {
         isPlaying = !isPlaying
         updateDrawable()
         invalidate()
         playbackListener?.invoke(isPlaying)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        buttonRect.set(0f, 0f, w.toFloat(), h.toFloat())
        updateDrawable()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawable?.setBounds(buttonRect.left.toInt(), buttonRect.top.toInt(), buttonRect.right.toInt(), buttonRect.bottom.toInt())
        drawable?.draw(canvas)
    }


    fun setPlaybackState(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        updateDrawable()
        invalidate()
    }

    private fun updateDrawable() {
        drawable = ContextCompat.getDrawable(context, if (isPlaying) pauseIconRes else playIconRes)
        invalidate()
    }

    var playbackListener: ((Boolean) -> Unit)? = null

}