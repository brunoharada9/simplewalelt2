package br.com.tolive.simplewallet.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import br.com.tolive.simplewallet.app.data.Entry
import br.com.tolive.simplewallet.app.databinding.ActivityMainBinding
import br.com.tolive.simplewallet.app.utils.Utils

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener {
            val entryListFragment : EntryListFragment? = navHostFragment.childFragmentManager.fragments[0] as? EntryListFragment
            if (entryListFragment != null) {
                addEntryListener = entryListFragment
                createAddEntryDialog()
            }
        }
    }

    // Create Listener to handle the database insertion on EntryListFragment
    interface OnAddEntryListener {
        fun onAddEntry(entry: Entry)
    }

    private var addEntryListener: OnAddEntryListener? = null

    private fun createAddEntryDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle(R.string.add_entry_dialog_title)

        val dialogLayout = inflater.inflate(R.layout.add_entry_dialog, null)
        val editTextDescription  = dialogLayout.findViewById<EditText>(R.id.edit_text_description)
        val editTextValue  = dialogLayout.findViewById<EditText>(R.id.edit_text_value)
        val datePicker  = dialogLayout.findViewById<DatePicker>(R.id.date_picker)

        val radioGroup  = dialogLayout.findViewById<RadioGroup>(R.id.radio_group)
        // TODO: change Dialog color based on entry type
        val radioButtonGain  = dialogLayout.findViewById<RadioButton>(R.id.radio_gain)
        val radioButtonExpense  = dialogLayout.findViewById<RadioButton>(R.id.radio_expense)

        builder.setView(dialogLayout)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val entryType = getEntryType(radioGroup)
            val value = Utils.convertEditTextToDouble(editTextValue)
            val entryDate =
                Entry.Date(datePicker.dayOfMonth, datePicker.month, datePicker.year)

            val entry = Entry(value, entryType, editTextDescription.text.toString(), entryDate)
            addEntryListener?.onAddEntry(entry)
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> }
        builder.show()


    }

    private fun getEntryType(radioGroup: RadioGroup): Int {
        var entryType = Entry.TYPE_GAIN
        when (radioGroup.checkedRadioButtonId) {
            R.id.radio_gain -> entryType = Entry.TYPE_GAIN
            R.id.radio_expense -> entryType = Entry.TYPE_EXPENSE
        }
        return entryType
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}