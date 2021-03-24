package ipvc.estg.cityhelper


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast


class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var title: EditText
    private lateinit var description: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        title = findViewById(R.id.title)
        description = findViewById(R.id.description)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        when (intent.hasExtra(EXTRA_ID)) {
            true -> {
                setTitle(R.string.edit_note)
                title.setText(intent.getStringExtra(EXTRA_TITLE))
                description.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            }
            else -> {
                setTitle(R.string.add_note)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
                saveNote()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun saveNote() {
        val titleText = title.text.toString()
        val descriptionText = description.text.toString()
        if (titleText.trim().isEmpty() || descriptionText.trim().isEmpty()) {
            Toast.makeText(this, R.string.insuccess, Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent()
        intent.putExtra(EXTRA_TITLE, titleText)
        intent.putExtra(EXTRA_DESCRIPTION, descriptionText)

        val id = getIntent().getIntExtra(EXTRA_ID, -1)
        if (id != -1) intent.putExtra(EXTRA_ID, id)

        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}