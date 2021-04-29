package ipvc.estg.cityhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import ipvc.estg.cityhelper.api.EndPoints
import ipvc.estg.cityhelper.api.LoginResponse
import ipvc.estg.cityhelper.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isEmpty()) {
                editTextEmail.error = getString(R.string.emailerror)
                editTextEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()){
                editTextPassword.error = getString(R.string.passworderror)
            editTextEmail.requestFocus()
            return@setOnClickListener
            }

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.checkLogin(email, password)
            call.enqueue(object: Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful){
                        val c: LoginResponse = response.body()!!
                        if(!c.status){
                            Toast.makeText(this@LoginActivity, R.string.loginerror, Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@LoginActivity, R.string.welcome_alert, Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@LoginActivity, MapActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, R.string.errorwebservice, Toast.LENGTH_SHORT).show()
                }
            })

        }
    }

    fun openNotes(view: View) {
        startActivity(Intent(this, NotesActivity::class.java))
    }

}
