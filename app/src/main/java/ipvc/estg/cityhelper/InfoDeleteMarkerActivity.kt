package ipvc.estg.cityhelper

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ipvc.estg.cityhelper.api.EndPoints
import ipvc.estg.cityhelper.api.OutputPost
import ipvc.estg.cityhelper.api.Marker
import ipvc.estg.cityhelper.api.ServiceBuilder

import retrofit2.Call
import retrofit2.Response

class InfoDeleteMarkerActivity : AppCompatActivity() {

    private lateinit var txtProblem: TextView
    private lateinit var txtCategory: TextView
    private lateinit var txtCoordinates: TextView
    private lateinit var btnEdit: Button
    private lateinit var btnRemove: Button
    private var idProblem: Int? = null
    private lateinit var problemDesc: String
    private lateinit var problemCategory: String
    private lateinit var problemLatLng: String
    private var userLogado: Int = 0
    private var userReport: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_delete_marker)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        //GET DATA FROM MAPS ACTIVITY
        val intentProblem: Bundle? = intent.extras
        idProblem = intentProblem?.getInt(MapActivity.EXTRA_PROBLEMID)
        problemDesc =
            intentProblem?.getString(MapActivity.EXTRA_PROBLEMDESC).toString().substringAfter("- ")
        problemCategory = intentProblem?.getString(MapActivity.EXTRA_PROBELMCATEGORY).toString()
        problemLatLng =
            intentProblem?.getString(MapActivity.EXTRA_LATLNG).toString().substringAfter("(")
                .substringBefore(")")
        userLogado = intentProblem?.getInt(MapActivity.EXTRA_IDUSERLOGIN)!!


        txtProblem = findViewById(R.id.txtProblem)
        txtCategory = findViewById(R.id.txtCategory)
        txtCoordinates = findViewById(R.id.txtLatLng)
        btnEdit = findViewById(R.id.btn_editProblem)
        btnRemove = findViewById(R.id.btn_deleteProblem)

        txtProblem.text = problemDesc
        txtCategory.text = problemCategory
        txtCoordinates.text = problemLatLng



        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getMarkerById(idProblem)


        call.enqueue(object : retrofit2.Callback<Marker> {
            override fun onResponse(call: Call<Marker>, response: Response<Marker>) {
                if (response.isSuccessful) {
                    val marker: Marker = response.body()!!
                    userReport = marker.users_id


                    //VERIFY IF USER CAN EDIT/REMOVE THE PROBLEM
                    if (userLogado != userReport) {
                        btnRemove.visibility = View.INVISIBLE
                        btnEdit.visibility = View.INVISIBLE
                    } else {
                        btnRemove.setOnClickListener {
                            val requestDelete = ServiceBuilder.buildService(EndPoints::class.java)

                            val builder = AlertDialog.Builder(this@InfoDeleteMarkerActivity)
                            builder.setPositiveButton(R.string.ok) { _, _ ->
                                val callDelete = requestDelete.deleteProblemById(idProblem)

                                callDelete.enqueue(object : retrofit2.Callback<OutputPost> {
                                    override fun onResponse(
                                        call: Call<OutputPost>,
                                        response: Response<OutputPost>,
                                    ) {
                                        if (response.isSuccessful) {
                                            val op = response.body()!!

                                            if (!op.status) {
                                                when (op.error) {
                                                    "delete" -> {
                                                        Toast.makeText(
                                                            this@InfoDeleteMarkerActivity,
                                                            R.string.errordeletingmarker,
                                                            Toast.LENGTH_SHORT
                                                        )
                                                            .show()
                                                    }
                                                    "404" -> {
                                                        Toast.makeText(
                                                            this@InfoDeleteMarkerActivity,
                                                            R.string.markernotfound,
                                                            Toast.LENGTH_SHORT
                                                        )
                                                            .show()
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(
                                                    this@InfoDeleteMarkerActivity,
                                                    R.string.markerdeleted,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                finish()
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                                        Toast.makeText(
                                            this@InfoDeleteMarkerActivity,
                                            R.string.errorwebservice,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                })


                            }
                            builder.setNegativeButton(R.string.cancel) { _, _ ->
                            }

                            builder.setMessage(R.string.delete_marker_confirm)
                            builder.create().show()
                        }

                        btnEdit.setOnClickListener {
                            val intentEdit =
                                Intent(this@InfoDeleteMarkerActivity, UpdateMarkerActivity::class.java)
                            intentEdit.putExtra(EXTRA_IDPROBLEM, idProblem)
                            intentEdit.putExtra(EXTRA_PROBLEMDESC, problemDesc)
                            intentEdit.putExtra(EXTRA_PROBLEMCATEGORY, problemCategory)
                            intentEdit.putExtra(EXTRA_USERID, userLogado)
                            intentEdit.putExtra(EXTRA_LATLNG, problemLatLng)
                            startActivity(intentEdit)
                            finish()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Marker>, t: Throwable) {
                Toast.makeText(this@InfoDeleteMarkerActivity, R.string.errorwebservice, Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    // this event will enable the back
// function to the button on press
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        const val EXTRA_IDPROBLEM = "com.estg.fixity.messages.IDPROBLEM"
        const val EXTRA_PROBLEMDESC = "com.estg.fixity.messages.PROBLEMDESC"
        const val EXTRA_PROBLEMCATEGORY = "com.estg.fixity.messages.PROBLEMCATEGORY"
        const val EXTRA_USERID = "com.estg.fixity.messages.USERID"
        const val EXTRA_LATLNG = "com.estg.fixity.messages.LATLNG"
    }
}