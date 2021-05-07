package ipvc.estg.cityhelper

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.cityhelper.api.EndPoints
import ipvc.estg.cityhelper.api.Marker
import ipvc.estg.cityhelper.api.OutputPost
import ipvc.estg.cityhelper.api.ServiceBuilder
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateMarkerActivity : AppCompatActivity() {

    private var selectedType: Int = 0
    private lateinit var updateProblem: Call<OutputPost>
    private var isChanged: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_marker)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)


        //GET DATA FROM MAPS ACTIVITY
        val intent: Bundle? = intent.extras
        val markerID = intent?.getInt(InfoDeleteMarkerActivity.EXTRA_IDPROBLEM)


        //TEXTFIELDS
        val buttonSave = findViewById<FloatingActionButton>(R.id.button_editProblem)
        val buttonCancel = findViewById<FloatingActionButton>(R.id.button_cancelEditProblem)
        val txtProblem = findViewById<EditText>(R.id.addProblemReportEdit)

        //EXPOSED MENU
        val textField = findViewById<AutoCompleteTextView>(R.id.txtCategoryEdit)
        val problemTypes = resources.getStringArray(R.array.problemTypes)

        //GET DATA FROM PROBLEM BY ID FROM SERVER
        val requestProblem = ServiceBuilder.buildService(EndPoints::class.java)
        val call = requestProblem.getMarkerById(markerID)

        call.enqueue(object : Callback<Marker> {
            override fun onResponse(call: Call<Marker>, response: Response<Marker>) {
                if (response.isSuccessful) {

                    val op: Marker = response.body()!!

                    selectedType = op.problemType

                    val items = resources.getStringArray(R.array.problemTypes)
                    var problemCategory = ""

                    when (op.problemType) {
                        1 -> {
                            problemCategory = items[0].toString()
                        }
                        2 -> {
                            problemCategory = items[1].toString()
                        }
                        3 -> {
                            problemCategory = items[2].toString()
                        }
                    }

                    val adapter =
                        ArrayAdapter(
                            this@UpdateMarkerActivity,
                            R.layout.exposed_menu_item,
                            problemTypes
                        )
                    textField.setText(problemCategory)
                    textField.setAdapter(adapter)
                    textField.setOnItemClickListener { _, _, position, _ ->
                        selectedType = position + 1
                    }

                    txtProblem.setText(op.descricao)
                }
            }

            override fun onFailure(call: Call<Marker>, t: Throwable) {
                Toast.makeText(
                    this@UpdateMarkerActivity,
                    R.string.errorwebservice,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

        buttonSave.setOnClickListener {
            if (txtProblem.text.isNullOrEmpty()) {
                Toast.makeText(
                    this@UpdateMarkerActivity,
                    R.string.marker_required,
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                val request = ServiceBuilder.buildService(EndPoints::class.java)

                val descricao = txtProblem.text.toString()


                val problemType = selectedType


                txtProblem.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {
                        Log.d("TAG", "beforeTextChanged")
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                        if (txtProblem.isFocused) {
                            isChanged = true;
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                        Log.d("TAG", "afterTextChanged")
                    }

                })

                if (isChanged) {
                    updateProblem = request.postEditMarker(
                        markerID, descricao, problemType
                    )
                    updateMarker()
                } else {
                    Toast.makeText(
                        this@UpdateMarkerActivity,
                        R.string.nothing_changed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun updateMarker() {
        //WEBSERVICE TO UPDATE PROBLEM
        updateProblem.enqueue(object : Callback<OutputPost> {
            override fun onResponse(
                call: Call<OutputPost>,
                response: Response<OutputPost>,
            ) {
                if (response.isSuccessful) {

                    val op: OutputPost = response.body()!!

                    if (!op.status) {
                        when (op.error) {
                            "data" -> Toast.makeText(
                                this@UpdateMarkerActivity,
                                R.string.error_updating_data,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            "404" -> Toast.makeText(
                                this@UpdateMarkerActivity,
                                R.string.problemNotFound,
                                Toast.LENGTH_SHORT
                            ).show()
                            "empty" -> Toast.makeText(
                                this@UpdateMarkerActivity,
                                R.string.empty_fields,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@UpdateMarkerActivity,
                            R.string.success,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                Toast.makeText(
                    this@UpdateMarkerActivity,
                    t.toString()/*R.string.error_register*/,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }
}