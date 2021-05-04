package ipvc.estg.cityhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import ipvc.estg.cityhelper.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var markers: List<Marker>
    private var userID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.map

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

        //MOVE CAMERA TO VIANA DO CASTELO
        val viana = LatLng(41.6946, -8.83016)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(viana))
    }

    override fun onStart() {
        super.onStart()
        getMarkersToMap()
    }

    private fun getMarkersToMap() {
        val intent: Bundle? = intent.extras
        userID = intent?.getInt(LoginActivity.EXTRA_USERID)
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getMarkers()
        call.enqueue(object : Callback<List<Marker>> {

            override fun onResponse(call: Call<List<Marker>>, response: Response<List<Marker>>) {
                if (response.isSuccessful) {
                    markers = response.body()!!
                    for (marker in markers){
                        var position = LatLng(marker.lat.toString().toDouble(), marker.lng.toString().toDouble())
                        if (marker.users_id == userID) {

                            mMap.addMarker(
                                MarkerOptions().position(position)
                                    .title("" + marker.id + " - " + marker.address)
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_AZURE
                                        )
                                    )
                            )
                        } else {
                            mMap.addMarker(
                                MarkerOptions().position(position)
                                    .title("" + marker.id + " - " + marker.address)
                            )
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Marker>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}

