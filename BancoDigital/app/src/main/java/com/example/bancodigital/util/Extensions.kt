package com.example.bancodigital.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.bancodigital.R
import com.example.bancodigital.databinding.LayoutBottomSheetSignOutBinding
import com.example.bancodigital.databinding.LayoutBottomSheetValidadeInputsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.initToolbar(
    toolbar: Toolbar,
    showIconNavigation: Boolean = true,
    icon: Int = R.drawable.ic_back_blue
) {
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).title = ""
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(showIconNavigation)
    (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(icon)
    toolbar.setNavigationOnClickListener {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }
}

fun Fragment.showBottomSheet(
    titleDialog: Int? = null,
    titleButton: Int? = null,
    message: String?,
    onClick: () -> Unit = {},
) {

    val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    val bottomSheetBinding: LayoutBottomSheetValidadeInputsBinding =
        LayoutBottomSheetValidadeInputsBinding.inflate(layoutInflater, null, false)

    bottomSheetBinding.textTitle.text = getString(titleDialog ?: R.string.text_title_bottom_sheet)
    bottomSheetBinding.textMessage.text = message ?: getText(R.string.error_generic)
    bottomSheetBinding.btnOk.text = getString(titleButton ?: R.string.text_button_bottom_sheet)
    bottomSheetBinding.btnOk.setOnClickListener {
        bottomSheetDialog.dismiss()
        onClick()
    }

    bottomSheetDialog.setContentView(bottomSheetBinding.root)
    bottomSheetDialog.show()
}

fun Fragment.showBottomSheetSignOut(
    onClick: () -> Unit = {},
) {

    val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    val bottomSheetBinding: LayoutBottomSheetSignOutBinding =
        LayoutBottomSheetSignOutBinding.inflate(layoutInflater, null, false)

    bottomSheetBinding.btnCancel.setOnClickListener {
        bottomSheetDialog.dismiss()
    }
    bottomSheetBinding.btnLogout.setOnClickListener {
        onClick()
        bottomSheetDialog.dismiss()
    }

    bottomSheetDialog.setContentView(bottomSheetBinding.root)
    bottomSheetDialog.show()
}