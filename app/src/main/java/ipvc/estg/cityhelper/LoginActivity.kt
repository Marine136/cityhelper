package ipvc.estg.cityhelper

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import ipvc.estg.cityhelper.api.EndPoints
import ipvc.estg.cityhelper.api.LoginResponse
import ipvc.estg.cityhelper.api.ServiceBuilder
import ipvc.estg.cityhelper.api.User
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val sharedPrefs: SharedPreferences =
            getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE)

        val isLoggedIn = sharedPrefs.getBoolean(getString(R.string.pref_is_user_login), false)

        val emailSaved = sharedPrefs.getString(getString(R.string.pref_email), "")

        val emailToast = emailSaved?.capitalize()?.substringBefore("@")

        val userId = sharedPrefs.getInt(getString(R.string.pref_user_id), 0)

        val welcome = getString(R.string.welcome_alert)

        if (isLoggedIn) {
            Toast.makeText(this@LoginActivity, "$welcome, $emailToast", Toast.LENGTH_SHORT).show()
            val intent = Intent(
                this@LoginActivity,
                MapActivity::class.java
            )
            intent.putExtra(EXTRA_USERID, userId);
            startActivity(intent)
            finish()
        }

        buttonLogin.setOnClickListener {

            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            when {
                email.isNullOrEmpty() -> {
                    Toast.makeText(
                        applicationContext,
                        R.string.emailerror,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                password.isNullOrEmpty() -> {
                    Toast.makeText(
                        applicationContext,
                        R.string.passworderror,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                else -> {

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.checkLogin(email, password)

                    call.enqueue(object : retrofit2.Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            if (response.isSuccessful) {

                                val op: LoginResponse = response.body()!!

                                if (!op.status) {
                                    when (op.error) {
                                        "email" -> {
                                            Toast.makeText(
                                                this@LoginActivity,
                                                R.string.emailerror,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                        "password" -> {
                                            Toast.makeText(
                                                this@LoginActivity,
                                                R.string.passworderror,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                } else {

                                    val request2 =
                                        ServiceBuilder.buildService(EndPoints::class.java)
                                    val getUserId = request2.getUsersByEmail(
                                        editTextEmail.text.toString()
                                    )

                                    getUserId.enqueue(object : retrofit2.Callback<User> {
                                        override fun onResponse(
                                            call: Call<User>,
                                            response: Response<User>
                                        ) {
                                            if (response.isSuccessful) {

                                                val user: User = response.body()!!


                                                //SAVE DATA ON SHAREDPREFERENCES
                                                val editor = sharedPrefs.edit()
                                                editor.putBoolean(
                                                    getString(R.string.pref_is_user_login),
                                                    true
                                                )
                                                editor.putString(
                                                    getString(R.string.pref_email),
                                                    editTextEmail.text.toString()
                                                )
                                                editor.putInt(
                                                    getString(R.string.pref_user_id),
                                                    user.id
                                                )
                                                editor.apply()


                                                val intent = Intent(
                                                    this@LoginActivity,
                                                    MapActivity::class.java
                                                )
                                                intent.putExtra(EXTRA_USERID, user.id);
                                                startActivity(intent)
                                                finish()
                                            }
                                        }

                                        override fun onFailure(call: Call<User>, t: Throwable) {
                                            Toast.makeText(
                                                this@LoginActivity,
                                                R.string.errorwebservice,
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    })
                                }
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(
                                this@LoginActivity,
                                R.string.errorwebservice,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                    )
                }
            }
        }
    }

    fun openNotes(view: View) {
        startActivity(Intent(this, NotesActivity::class.java))
    }

    companion object {
        const val EXTRA_USERID = "ipvc.estg.cityhelper.messages.USERID"
    }
}
