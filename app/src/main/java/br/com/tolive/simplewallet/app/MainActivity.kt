package br.com.tolive.simplewallet.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.room.DatabaseView
import br.com.tolive.simplewallet.app.data.Entry
import br.com.tolive.simplewallet.app.databinding.ActivityMainBinding
import br.com.tolive.simplewallet.app.viewmodel.EntryListViewModel
import br.com.tolive.simplewallet.app.viewmodel.EntryViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    val viewModel: EntryListViewModel by viewModels{
        EntryViewModelFactory((application as SimpleWalletApplication).repository)
    }

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

    interface OnAddEntryListener {
        fun onAddEntry(entry: Entry)
    }

    private var addEntryListener: OnAddEntryListener? = null

    private fun createAddEntryDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle(R.string.add_entry_dialog_title)

        //val bind :DialogAlertCommonBinding = DialogAlertCommonBinding .inflate(inflater)
        val dialogLayout = inflater.inflate(R.layout.add_entry_dialog, null)
        val editTextDescription  = dialogLayout.findViewById<EditText>(R.id.edit_text_description)
        val editTextValue  = dialogLayout.findViewById<EditText>(R.id.edit_text_value)

        // TODO: enable date selection
        val editTextDate  = dialogLayout.findViewById<EditText>(R.id.edit_text_date)

        val radioGroup  = dialogLayout.findViewById<RadioGroup>(R.id.radio_group)
        // TODO: change Dialog color based on entry type
        val radioButtonGain  = dialogLayout.findViewById<RadioButton>(R.id.radio_gain)
        val radioButtonExpense  = dialogLayout.findViewById<RadioButton>(R.id.radio_expense)

        builder.setView(dialogLayout)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            var entryType = getEntryType(radioGroup)
            var value = getDoubleValue(editTextValue)
            var entry = Entry(value, entryType, editTextDescription.text.toString())
            addEntryListener?.onAddEntry(entry)
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> }
        builder.show()


    }

    private fun getDoubleValue(editTextValue: EditText): Double {
        var value = 0.0
        if (editTextValue.text.toString().isNotEmpty()) {
            value = editTextValue.text.toString().toDouble()
        }
        return value
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