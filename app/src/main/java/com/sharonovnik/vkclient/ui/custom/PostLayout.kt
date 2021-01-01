package com.sharonovnik.vkclient.ui.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import com.sharonovnik.vkclient.R
import com.sharonovnik.vkclient.domain.model.Photo
import com.sharonovnik.vkclient.dpToPx
import com.sharonovnik.vkclient.getDisplayHeight
import kotlinx.android.synthetic.main.view_post.view.*
import kotlin.math.max

class PostLayout constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr) {
    private var hasContentImage = true
    private var photo: Photo? = null
    private val maxImageHeight: Int

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_post, this, true)
        setWillNotDraw(true)
        setBackgroundResource(R.drawable.ripple_bg)
        maxImageHeight = context.getDisplayHeight()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PostLayout)
        hasContentImage = typedArray.getBoolean(R.styleable.PostLayout_hasContentImage, true)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildWithMargins(avatar, widthMeasureSpec, 0, heightMeasureSpec, 0)
        var layoutParams = avatar.layoutParams as MarginLayoutParams
        val width = avatar.measuredWidth + layoutParams.marginStart + layoutParams.marginEnd

        measureChildWithMargins(name, widthMeasureSpec, width, heightMeasureSpec, 0)
        measureChildWithMargins(date, widthMeasureSpec, width, heightMeasureSpec, name.measuredHeight)

        val imageHeight = avatar.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin

        layoutParams = name.layoutParams as MarginLayoutParams
        val nameHeight = name.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin

        layoutParams = date.layoutParams as MarginLayoutParams
        val dateHeight = date.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin

        val titleAndImageHeight = max(imageHeight, nameHeight + dateHeight)

        measureChildWithMargins(contentText, widthMeasureSpec, 0, heightMeasureSpec, titleAndImageHeight)
        layoutParams = contentText.layoutParams as MarginLayoutParams
        val contentTextHeight =
            contentText.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin

        val contentImageHeight: Int
        layoutParams = contentImage.layoutParams as MarginLayoutParams
        if (hasContentImage && photo != null) {
            layoutParams =
                getLayoutParamsForImage(photo!!, layoutParams, MeasureSpec.getSize(widthMeasureSpec))
            measureChildWithMargins(contentImage, widthMeasureSpec, 0, heightMeasureSpec, titleAndImageHeight + contentTextHeight)
            contentImageHeight =
                contentImage.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
        } else {
            contentImageHeight = layoutParams.topMargin + layoutParams.bottomMargin
        }

        val contentHeight = titleAndImageHeight + contentTextHeight + contentImageHeight

        measureChildWithMargins(like, widthMeasureSpec, 0, heightMeasureSpec, contentHeight)
        layoutParams = like.layoutParams as MarginLayoutParams
        val likeHeight = like.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin

        var statisticWidthUsed = like.measuredWidth + like.marginStart

        measureChildWithMargins(likeCount, widthMeasureSpec, statisticWidthUsed, heightMeasureSpec, contentHeight)

        val statisticsView = listOf<View>(likeCount, comment, commentCount)
        statisticsView.forEachIndexed { index, view ->
            if (index == (statisticsView.size - 1)) {
                return@forEachIndexed
            }
            statisticWidthUsed += view.measuredWidth + view.marginStart
            measureChildWithMargins(statisticsView[index + 1], widthMeasureSpec, statisticWidthUsed, heightMeasureSpec, contentHeight)
        }

        measureChildWithMargins(options, widthMeasureSpec, 0, heightMeasureSpec, 0)
        val totalHeight =
            paddingTop + paddingBottom + titleAndImageHeight + contentTextHeight + contentImageHeight + likeHeight
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), resolveSize(totalHeight, heightMeasureSpec))
    }

    private fun getLayoutParamsForImage(
        photo: Photo, params: MarginLayoutParams, maxWidth: Int
    ): MarginLayoutParams {
        val size = photo.sizes.find { it.type == "x" }
        val width = size?.width ?: 100
        val height = size?.height ?: 100
        val coefficient = width.toDouble() / height.toDouble()
        var measuredWidth = maxWidth
        var measuredHeight = (maxWidth / coefficient).toInt()
        if (maxImageHeight < measuredHeight) {
            measuredHeight = maxImageHeight
            measuredWidth = (measuredHeight * coefficient).toInt()
        }
        params.width = measuredWidth
        params.height = measuredHeight
        return params
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var layoutParams = avatar.layoutParams as MarginLayoutParams
        var currentStart = paddingStart + layoutParams.marginStart
        var currentTop = paddingTop + layoutParams.topMargin

        avatar.layout(currentStart, currentTop, currentStart + avatar.measuredWidth, currentTop + avatar.measuredHeight)
        currentStart += avatar.measuredWidth + layoutParams.marginEnd

        layoutParams = name.layoutParams as MarginLayoutParams
        currentStart += layoutParams.marginStart
        currentTop = paddingTop + layoutParams.topMargin
        name.layout(currentStart, currentTop, currentStart + name.measuredWidth, currentTop + name.measuredHeight)

        layoutParams = date.layoutParams as MarginLayoutParams
        currentTop += name.measuredHeight + layoutParams.topMargin
        date.layout(currentStart, currentTop, currentStart + date.measuredWidth, currentTop + date.measuredHeight)

        layoutParams = contentText.layoutParams as MarginLayoutParams
        currentStart = paddingStart + layoutParams.marginStart
        currentTop += date.measuredHeight + layoutParams.topMargin
        contentText.layout(currentStart, currentTop, contentText.measuredWidth, currentTop + contentText.measuredHeight)

        val statisticTop: Int
        if (hasContentImage && photo != null) {
            layoutParams = contentImage.layoutParams as MarginLayoutParams
            layoutParams = getLayoutParamsForImage(photo!!, layoutParams, measuredWidth)
            currentStart = paddingStart + layoutParams.marginStart
            currentTop += contentText.measuredHeight + layoutParams.topMargin
            contentImage.layout(currentStart, currentTop, contentImage.measuredWidth, currentTop + contentImage.measuredHeight)
        } else {
            currentTop += contentText.measuredHeight
        }
        statisticTop = currentTop
        val marginBetweenStatisticViews = context.dpToPx(30)
        layoutParams = like.layoutParams as MarginLayoutParams
        currentStart = paddingStart + layoutParams.marginStart
        currentTop += contentImage.measuredHeight + layoutParams.topMargin
        like.layout(currentStart, currentTop, currentStart + like.measuredWidth, currentTop + like.measuredHeight)

        layoutParams = likeCount.layoutParams as MarginLayoutParams
        currentStart += like.measuredWidth + like.marginStart + layoutParams.marginStart
        currentTop = statisticTop + contentImage.measuredHeight + layoutParams.topMargin
        likeCount.layout(currentStart, currentTop, currentStart + likeCount.measuredWidth, currentTop + likeCount.measuredHeight)

        currentStart += likeCount.measuredWidth + likeCount.marginStart + marginBetweenStatisticViews
        comment.layout(
            currentStart, currentTop, currentStart + comment.measuredWidth, currentTop + comment.measuredHeight
        )

        currentStart += comment.measuredWidth + comment.marginStart
        commentCount.layout(
            currentStart, currentTop, currentStart + commentCount.measuredWidth, currentTop + commentCount.measuredHeight
        )

        currentStart += commentCount.measuredWidth + commentCount.marginStart + marginBetweenStatisticViews

        // options dots
        layoutParams = options.layoutParams as MarginLayoutParams
        currentStart = measuredWidth - options.marginEnd - options.measuredWidth
        currentTop = paddingTop + layoutParams.topMargin
        options.layout(
            currentStart, currentTop, currentStart + options.measuredWidth, currentTop + options.measuredHeight
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return generateDefaultLayoutParams()
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is MarginLayoutParams
    }

    fun setPostName(name: String) {
        this.name.text = name
    }

    fun setPostPublicationDate(date: String) {
        this.date.text = date
    }

    fun setPostAvatarDrawable(drawable: Drawable?) {
        avatar.setImageDrawable(drawable)
    }

    fun setPostContentText(contextText: String?) {
        contextText?.let {
            this.contentText.text = contextText
        }
    }

    fun setContentImageDrawable(drawable: Drawable?) {
        this.contentImage.setImageDrawable(drawable)
    }

    fun setLikeCount(likeCount: Long?) {
        likeCount?.let {
            this.likeCount.text = likeCount.toString()
        }
    }

    fun setCommentCount(commentCount: Long?) {
        commentCount?.let {
            this.commentCount.text = commentCount.toString()
        }
    }

    fun setHasContentImage(hasContentImage: Boolean) {
        this.hasContentImage = hasContentImage
    }

    fun setImage(photo: Photo) {
        this.photo = photo
    }
}