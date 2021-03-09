package ipvc.estg.cityhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Toast.makeText(this, R.string.welcome_alert, Toast.LENGTH_SHORT).show()
    }

    fun openNotes(view: View) {
        startActivity(Intent(this, NotesActivity::class.java))
    }
}
