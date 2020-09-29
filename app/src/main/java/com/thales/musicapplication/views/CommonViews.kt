package com.thales.musicapplication.views

import android.view.View
import com.google.android.material.snackbar.Snackbar

object CommonViews {
    fun showSnackBar(view: View, message: String, action: String? = null,
                     actionListener: View.OnClickListener? = null ) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)

        if (action != null && actionListener!=null) {
            snackBar.setAction(action, actionListener)
        }
        snackBar.show()
    }

}