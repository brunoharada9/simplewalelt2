package br.com.tolive.simplewallet.app.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import br.com.tolive.simplewallet.app.R
import br.com.tolive.simplewallet.app.utils.Utils

open class BaseMenuAnimFragment : Fragment() {

    protected lateinit var mainActivity: MainActivity
    protected var menuRes: Int = R.menu.menu_transaction_list
    protected lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(menuRes, menu)
        this.menu = menu

        animateAllVisibleMenuOptions()

        super.onCreateOptionsMenu(menu, inflater)
    }

    fun animateAllVisibleMenuOptions() {
        var position = 0
        for (menuItem in menu) {
            if (menuItem.isVisible) {
                animateMenuOption(
                    menu,
                    mainActivity,
                    menuItem.icon,
                    MENU_PADDING * (menu.size() - position),
                    position
                )
            }
            position++
        }
    }

    fun animateMenuOption(
        menu: Menu,
        activity: MainActivity,
        icon: Drawable?,
        padding: Int,
        position: Int
    ) {
        val image = ImageView(activity)
        image.setPadding(0, 0, Utils.dpToPx(padding, activity.resources), 0)
        image.setImageDrawable(icon)
        menu.getItem(position).actionView = image
        val anim = TranslateAnimation(
            0F, 0F,
            600F, 0F
        )
        anim.duration = MENU_ANIM_DURATION
        anim.fillAfter = true
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                menu.getItem(position).actionView = null
            }
        })
        menu.getItem(position).actionView!!.startAnimation(anim)
    }

    companion object {
        const val MENU_ANIM_DURATION: Long = 150
        const val MENU_PADDING: Int = 12
    }
}