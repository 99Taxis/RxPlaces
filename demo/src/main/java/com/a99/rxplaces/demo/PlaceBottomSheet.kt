package com.taxis99.ui.bottomsheet

import android.app.Dialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import com.a99.rxplaces.demo.R


class PlaceBottomSheet : BottomSheetDialogFragment() {

  override fun setupDialog(dialog: Dialog?, style: Int) {
    super.setupDialog(dialog, style)
    val contentView = View.inflate(context, R.layout.bottom_sheet_place, null)
    dialog?.setContentView(contentView)

  }

}
