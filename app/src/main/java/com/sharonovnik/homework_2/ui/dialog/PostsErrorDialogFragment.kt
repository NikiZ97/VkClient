package com.sharonovnik.homework_2.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.sharonovnik.homework_2.R

class PostsErrorDialogFragment: DialogFragment() {

    companion object {
        private const val EXTRA_MESSAGE_NAME = "message_name"

        fun newInstance(message: String?): PostsErrorDialogFragment {
            val postsErrorDialogFragment = PostsErrorDialogFragment()
            val bundle = Bundle().apply {
                putString(EXTRA_MESSAGE_NAME, message)
            }
            postsErrorDialogFragment.arguments = bundle
            return postsErrorDialogFragment
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity,
            R.style.AlertDialogTheme
        )
        with(builder) {
            setTitle(getString(R.string.posts_dialog_error_title))
            setMessage(arguments?.getString(EXTRA_MESSAGE_NAME) ?: getString(R.string.posts_dialog_error_message))
            setPositiveButton(getString(R.string.posts_dialog_positive_button_text)) { _, _ ->
                dismiss()
            }
        }
        return builder.create()
    }
}