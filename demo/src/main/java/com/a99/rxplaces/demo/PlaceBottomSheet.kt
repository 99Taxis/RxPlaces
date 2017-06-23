package com.taxis99.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import com.a99.rxplaces.GeocodeResult
import com.a99.rxplaces.demo.R
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class PlaceBottomSheet : BottomSheetDialogFragment() {

  private lateinit var geocode: GeocodeResult

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    arguments?.let {
      geocode = it.getSerializable(ARG_GEOCODE) as GeocodeResult
    }

    return super.onCreateDialog(savedInstanceState)
  }

  override fun setupDialog(dialog: Dialog?, style: Int) {
    super.setupDialog(dialog, style)
    val contentView = View.inflate(context, R.layout.bottom_sheet_place, null)
    dialog?.setContentView(contentView)

    val mapView = contentView.findViewById(R.id.place_location) as MapView
    mapView.getMapAsync {
      with(geocode) {
        it.addMarker(MarkerOptions().position(LatLng(geometry.location.lat, geometry.location.lng)))
      }

    }

  }

  companion object {
    val ARG_GEOCODE = "arg_geocode"

    fun newInstance(geocodeResult: GeocodeResult?): PlaceBottomSheet {
      return PlaceBottomSheet().apply {
        arguments = Bundle().apply {
          putSerializable(ARG_GEOCODE, geocodeResult)
        }
      }
    }
  }
}
