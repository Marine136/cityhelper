package ipvc.estg.cityhelper.ui

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import ipvc.estg.cityhelper.R
import kotlinx.android.synthetic.main.activity_add_marker.*

class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {

        val spinner: Spinner = findViewById(R.id.spinnerProblemType)
        spinner.onItemSelectedListener = this
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.problemTypes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        // An item was selected. You can retrieve the selected item using
        parent.getItemAtPosition(pos)

    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        spinnerProblemType
    }
}