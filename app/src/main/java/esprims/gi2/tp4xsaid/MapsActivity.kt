package esprims.gi2.tp4xsaid

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*
import kotlin.math.roundToLong

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        fusedLocationClient = getFusedLocationProviderClient(this)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        // mMap.mapType=GoogleMap.MAP_TYPE_SATELLITE
        //  mMap.mapType=GoogleMap.MAP_TYPE_TERRAIN

        // Add a marker in Sydney and move the camera
        val monastir = LatLng(35.769260, 10.819970)
        val sousse = LatLng(35.82, 10.64)
        val tunis = LatLng(36.8, 10.17)
        mMap.addMarker(MarkerOptions().position(monastir).title("monastir"))
        mMap.addMarker(MarkerOptions().position(sousse).title("sousse"))
        mMap.addPolyline(PolylineOptions().add(monastir, sousse))
        mMap.addCircle(CircleOptions().center(tunis).radius(30000.00))
        val cameraPosition = CameraPosition.builder()
            .target(monastir)
            .zoom(10f)
            .bearing(45f)
            .tilt(90f)
            .build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mMap.setOnMapClickListener {
           /* Toast.makeText(
                this,
                "la latitude" + it.latitude + "la longitude" + it.longitude.toShort(),
                Toast.LENGTH_SHORT
            ).show()*/
            fun getAddress(lat: Double, lng: Double): String {
                var geocoder = Geocoder(applicationContext, Locale.getDefault())
                var location : List<Address> = geocoder.getFromLocation(lat, lng, 1)
                return location[0].getAddressLine(0)
            }
            Toast.makeText(
                this,
                "cette adress est :" + getAddress(it.latitude,it.longitude),
                Toast.LENGTH_SHORT
            ).show()
        }
        mMap.setOnMarkerClickListener {

            intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://fr.wikipedia.org/wiki/" + it.title)
            )
            startActivity(intent)
            true

        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            Toast.makeText(
                applicationContext, "Vous devez activer l'autorisation de GPS",
                Toast.LENGTH_LONG
            ).show()
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )

        } else
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    fun getAddress(lat: Double, lng: Double): String {
                        var geocoder = Geocoder(applicationContext, Locale.getDefault())
                        var location : List<Address> = geocoder.getFromLocation(lat, lng, 1)
                        return location[0].getAddressLine(0)
                    }
                    Toast.makeText(
                        this,
                        "votre adress est :" + getAddress(it.altitude,it.longitude) + "la longitude" + it.longitude,
                        Toast.LENGTH_SHORT
                    ).show()
                    /*Toast.makeText(
                        this,
                        "la latitude" + it.latitude + "la longitude" + it.longitude,
                        Toast.LENGTH_SHORT
                    ).show()*/
                    mMap.addMarker(
                        MarkerOptions().position(LatLng(it.altitude, it.longitude))
                            .title("Ma position")
                    )

                }
            }
        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            if (requestCode == 1) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                else Toast.makeText(
                    applicationContext,
                    "Acces non autorise aux GPS",
                    Toast.LENGTH_LONG
                ).show()

            }
        }



    }
}