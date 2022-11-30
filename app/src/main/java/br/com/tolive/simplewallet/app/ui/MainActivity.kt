package br.com.tolive.simplewallet.app.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import br.com.tolive.simplewallet.app.R
import br.com.tolive.simplewallet.app.data.Entry
import br.com.tolive.simplewallet.app.databinding.ActivityMainBinding
import br.com.tolive.simplewallet.app.utils.Utils


private const val SUMMARY_ANIMATION_TIME: Long = 300

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding

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
                AddEntryDialog(addEntryListener as EntryListFragment).show(supportFragmentManager, AddEntryDialog.TAG)
            }
        }
    }

    // Create Listener to handle the database insertion on EntryListFragment
    interface OnAddEntryListener {
        fun onAddEntry(entry: Entry)
    }

    private var addEntryListener: OnAddEntryListener? = null

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

    fun updateSummary(sum: Double) {
        binding.summaryValue.text = Utils.getValueFormatted(sum)
        if (sum < 0) {
            binding.layoutSummary.setBackgroundResource(android.R.color.holo_red_light)
        } else {
            binding.layoutSummary.setBackgroundResource(android.R.color.holo_green_light)
        }
    }

    fun showSummaryAndFab() {
        binding.fab.show()

        val animate = TranslateAnimation(0F,0F,
            binding.summaryCard.height.toFloat(), 0F)
        animate.duration = SUMMARY_ANIMATION_TIME
        animate.fillAfter = true
        binding.summaryCard.startAnimation(animate)
    }

    fun hideSummaryAndFab() {
        binding.fab.hide()

        val animate = TranslateAnimation(0F,0F,
            0F, binding.summaryCard.height.toFloat())
        animate.duration = SUMMARY_ANIMATION_TIME
        animate.fillAfter = true
        binding.summaryCard.startAnimation(animate)
    }
}