package com.teknosols.a3orrsy.view.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import com.teknosols.a3orrsy.R
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_splash, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logoAnimation()
    }

    fun logoAnimation() {
        ivLogo.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    ivLogo.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    ivLogo.y = -500f
                    var height = ivLogoPlacer.bottom
                    ivLogo.animate()
                        .setInterpolator(BounceInterpolator())
                        .y(height / 2f)
                        .duration = 2000
                }
            });
    }

    companion object {
        @JvmStatic
        fun newInstance() = SplashFragment()
    }
}
