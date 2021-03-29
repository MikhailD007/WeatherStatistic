package org.vimteam.weatherstatistic.ui.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.vimteam.weatherstatistic.R
import java.io.IOException

class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private lateinit var marker: Marker

    companion object {
        const val REQUEST_KEY = "MAP_REQUEST_KEY"
    }

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        val initialPlace = LatLng(52.52000659, 46.4049539)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initialPlace))
        activateMyLocation(googleMap)
        getLocation()
        googleMap.setOnMapLongClickListener { latLng ->
            getAddressAsync(latLng) {
                marker = setMarker(latLng, it, R.drawable.ic_pin)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        view.findViewById<MaterialButton>(R.id.selectPlaceMaterialButton).setOnClickListener {
            setFragmentResult(REQUEST_KEY, bundleOf(REQUEST_KEY to "${marker.position.latitude},${marker.position.longitude}"))
            findNavController().navigateUp()
        }
    }

    private fun activateMyLocation(googleMap: GoogleMap) {
        val isPermissionGranted =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
        googleMap.isMyLocationEnabled = isPermissionGranted
        googleMap.uiSettings.isMyLocationButtonEnabled = isPermissionGranted
        if (!isPermissionGranted) checkPositionPermission()
    }

    private fun checkPositionPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    activateMyLocation(map)
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle(getString(R.string.position_permission_dialog_title))
                        .setMessage(getString(R.string.position_permission_dialog_description))
                        .setPositiveButton(getString(R.string.give_permission)) { _, _ ->
                            requestPositionPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                        .setNegativeButton(getString(R.string.no_permission)) { dialog, _ ->
                            showDialog(
                                getString(R.string.position_permission_dialog_title),
                                getString(R.string.position_permission_dialog_denied_description)
                            )
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
                else -> {
                    requestPositionPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    private val requestPositionPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                activateMyLocation(map)
            } else {
                showDialog(
                    getString(R.string.position_permission_dialog_title),
                    getString(R.string.position_permission_dialog_denied_description)
                )
            }
        }

    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private val onLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            context?.let {
                getAddressAsync(LatLng(location.latitude, location.longitude), {})
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun getLocation() {
        activity?.let { context ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            60000L,
                            100.0F,
                            onLocationListener
                        )
                    }
                } else {
                    val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null) {
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_location_unknown)
                        )
                    } else {
                        getAddressAsync(LatLng(location.latitude, location.longitude), {})
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_known_location)
                        )
                    }
                }
            } else {
                checkPositionPermission()
            }
        }
    }

    private fun getAddressAsync(location: LatLng, func: (String) -> Unit) {
        context?.let {
            val geoCoder = Geocoder(it)
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                    activity?.runOnUiThread {
                        func.invoke(addresses[0].getAddressLine(0))
                        Snackbar.make(requireView(), addresses[0].getAddressLine(0), Snackbar.LENGTH_LONG).show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setMarker(location: LatLng, searchText: String, resourceId: Int): Marker {
        map.clear()
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourceId))
        )
    }


}