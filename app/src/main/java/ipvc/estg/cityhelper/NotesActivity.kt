package ipvc.estg.cityhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class NotesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        Toast.makeText(this, R.string.notes, Toast.LENGTH_SHORT).show()
    }
}