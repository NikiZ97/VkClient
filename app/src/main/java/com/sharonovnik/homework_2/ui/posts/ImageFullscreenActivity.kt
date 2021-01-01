package com.sharonovnik.homework_2.ui.posts

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sharonovnik.homework_2.R
import com.sharonovnik.homework_2.ui.custom.CloseOnFlingListener
import kotlinx.android.synthetic.main.activity_image_fullscreen.*

class ImageFullscreenActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IMAGE_URL = "extra_image_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_fullscreen)
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .into(imageView)
        }
        arrowBack.setOnClickListener {
            finish()
        }
        imageView.setOnSingleFlingListener(object : CloseOnFlingListener(this@ImageFullscreenActivity) {
            override fun onVerticalFling(distanceByY: Float): Boolean {
                goBackWithAnimation(distanceByY)
                return true
            }
        })
    }

    private fun goBackWithAnimation(distanceByY: Float) {
        val objectAnimationPosition = ObjectAnimator.ofFloat(root, "translationY", -distanceByY)
        val objectAnimatorAlpha = ObjectAnimator.ofFloat(root, View.ALPHA, 1f, 0f)
        val animatorSet = AnimatorSet().apply {
            playTogether(objectAnimationPosition, objectAnimatorAlpha)
            duration = 200
        }
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                finish()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationRepeat(animation: Animator?) {}

        })
        animatorSet.start()
    }
}