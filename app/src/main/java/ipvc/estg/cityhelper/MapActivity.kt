package ipvc.estg.cityhelper

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.location.Location
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import ipvc.estg.cityhelper.api.*
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.info_popup_marker.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var markers: List<Marker>
    private var userID: Int? = null
    private var userLocation: LatLng? = null
    private lateinit var markerDescription: TextView
    private lateinit var latLng: TextView
    private lateinit var problemType: TextView
    private var markerID: String = ""

    //LOCATION
    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastLocation: Location


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //LOCATION
        createLocationRequest()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                userLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
            }
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.map

        buttonAddMarker2.setOnClickListener {
            val intentReport = Intent(
                this@MapActivity,
                AddMarkerActivity::class.java
            )
            intentReport.putExtra(EXTRA_IDUSERLOGIN, userID)
            startActivity(intentReport)
        }

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.map -> {
                    Toast.makeText(this, R.string.map, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.notes -> {
                    finish()
                    startActivity(Intent(applicationContext, NotesActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.settings -> {
                    finish()
                    startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
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

        //MOVE CAMERA TO VIANA DO CASTELO
        val viana = LatLng(41.6946, -8.83016)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(viana))
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this@MapActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MapActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000 //5 em 5 segundos
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
        getMarkersToMap(userLastLocation = null)
    }

    private fun getMarkersToMap(userLastLocation: LatLng?) {
        val intent: Bundle? = intent.extras
        userID = intent?.getInt(LoginActivity.EXTRA_USERID)
        val request = ServiceBuilder.buildService(EndPoints::class.java)

        val call = request.getMarkers()
        var position: LatLng
        call.enqueue(object : Callback<List<Marker>> {

            override fun onResponse(call: Call<List<Marker>>, response: Response<List<Marker>>) {
                if (response.isSuccessful) {
                    markers = response.body()!!

                    for (marker in markers) {
                        position = LatLng(
                            marker.lat.toString().toDouble(),
                            marker.lng.toString().toDouble()
                        )

                        val items = resources.getStringArray(R.array.problemTypes)

                        var problemTypes = ""

                        when (marker.problemType) {
                            1 -> {
                                problemTypes = items[0].toString()
                            }
                            2 -> {
                                problemTypes = items[1].toString()
                            }
                            3 -> {
                                problemTypes = items[2].toString()
                            }
                        }

                        // Setting a custom info window adapter for the google map
                        mMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                            override fun getInfoContents(p0: com.google.android.gms.maps.model.Marker?): View {
                                var v: View? = null

                                try {

                                    // Getting view from the layout file info_window_layout
                                    v = layoutInflater.inflate(R.layout.info_popup_marker, null)

                                    markerDescription =
                                        v!!.findViewById<View>(R.id.descricao) as TextView
                                    markerDescription.text = p0?.title

                                    problemType =
                                        v.findViewById<View>(R.id.problemType) as TextView
                                    problemType.text = p0?.snippet?.substringBefore(" -")

                                    latLng =
                                        v.findViewById<View>(R.id.latLngMarker) as TextView
                                    latLng.text = p0?.position.toString()

                                    markerID = p0?.title?.substringBefore(" -").toString()

                                } catch (ev: Exception) {
                                    print(ev.message)
                                }
                                return v!!
                            }

                            override fun getInfoWindow(p0: com.google.android.gms.maps.model.Marker?): View? {
                                return null
                            }
                        })

                        mMap.setOnInfoWindowClickListener {
                        val intentDetails = Intent(this@MapActivity, InfoDeleteMarkerActivity::class.java)
                        intentDetails.putExtra(EXTRA_IDUSERLOGIN, userID)
                        intentDetails.putExtra(EXTRA_PROBLEMID, markerID)
                        intentDetails.putExtra(EXTRA_IDUSERREPORT, marker.users_id)
                        intentDetails.putExtra(EXTRA_PROBLEMDESC, markerDescription.text.toString())
                        intentDetails.putExtra(
                            EXTRA_PROBELMCATEGORY,
                            problemType.text.toString()
                        )
                        intentDetails.putExtra(EXTRA_LATLNG, latLng.text.toString())
                        startActivity(intentDetails)
                    }

                    mMap.setOnMarkerClickListener { mark ->

                        mark.showInfoWindow()

                        val handler = Handler()
                        handler.postDelayed({ mark.showInfoWindow() }, 400)

                        true
                    }
                        if (marker.users_id == userID) {

                            mMap.addMarker(
                                MarkerOptions().position(position)
                                    .title("" + marker.id + " - " + marker.descricao)
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_AZURE
                                        )
                                    )
                            )
                        } else {
                            mMap.addMarker(
                                MarkerOptions().position(position)
                                    .title("" + marker.id + " - " + marker.descricao)
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Marker>>, t: Throwable) {
                Log.d("Error", t.toString())
            }
        })
    }


    companion object {
        const val EXTRA_IDUSERLOGIN = "com.estg.fixity.messages.USERIDLOGIN"
        const val EXTRA_PROBLEMID = "com.estg.fixity.messages.PROBLEMID"
        const val EXTRA_IDUSERREPORT = "com.estg.fixity.messages.IDUSERREPORT"
        const val EXTRA_LATLNG = "com.estg.fixity.messages.LATLNG"
        const val EXTRA_PROBLEMDESC = "com.estg.fixity.messages.PROBLEMDESC"
        const val EXTRA_PROBELMCATEGORY = "com.estg.fixity.messages.PROBLEMCATEGORY"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1


    }
}

