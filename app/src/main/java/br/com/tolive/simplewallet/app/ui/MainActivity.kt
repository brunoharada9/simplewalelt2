package br.com.tolive.simplewallet.app.ui

import android.graphics.Paint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import br.com.tolive.simplewallet.app.R
import br.com.tolive.simplewallet.app.data.Transaction
import br.com.tolive.simplewallet.app.databinding.ActivityMainBinding
import br.com.tolive.simplewallet.app.utils.Utils
import java.util.*


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

        // TODO filter to show month or year
        binding.summaryTitle.text = Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())?.uppercase()

        binding.fab.setOnClickListener {
            val transactionListFragment : TransactionListFragment? = navHostFragment.childFragmentManager.fragments[0] as? TransactionListFragment
            if (transactionListFragment != null) {
                addTransactionListener = transactionListFragment
                AddTransactionDialog(addTransactionListener as TransactionListFragment).show(supportFragmentManager, AddTransactionDialog.TAG)
            }
        }
    }

    // Create Listener to handle the database insertion on TransactionListFragment
    interface OnAddTransactionListener {
        fun onAddTransaction(transaction: Transaction)
    }

    private var addTransactionListener: OnAddTransactionListener? = null

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
            R.id.action_filter -> {
                // TODO Open filter dialog to select by month or year
                Toast.makeText(this, "Open filter dialog", Toast.LENGTH_SHORT).show()
                true
            }
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
            binding.summaryValue.setTextAppearance(R.style.SummaryTextStyleRed)
        } else {
            binding.summaryValue.setTextAppearance(R.style.SummaryTextStyleGreen)
        }

        // Create a white stroke for text outline
        binding.summaryStroke.text = Utils.getValueFormatted(sum)
        binding.summaryStroke.paint.strokeWidth = SUM_VALUE_STROKE_SIZE
        binding.summaryStroke.paint.style = Paint.Style.STROKE
    }

    fun showSummaryAndFab() {
        binding.fab.show()

        val animate = TranslateAnimation(ANIMATION_0F,ANIMATION_0F,
            binding.summaryCard.height.toFloat(), ANIMATION_0F)
        animate.duration = SUMMARY_ANIMATION_TIME
        animate.fillAfter = true
        binding.summaryCard.startAnimation(animate)
    }

    fun hideSummaryAndFab() {
        binding.fab.hide()

        val animate = TranslateAnimation(ANIMATION_0F,ANIMATION_0F,
            ANIMATION_0F, binding.summaryCard.height.toFloat())
        animate.duration = SUMMARY_ANIMATION_TIME
        animate.fillAfter = true
        binding.summaryCard.startAnimation(animate)
    }

    companion object {
        const val SUMMARY_ANIMATION_TIME: Long = 300

        const val ANIMATION_0F = 0F
        const val SUM_VALUE_STROKE_SIZE = 2F

        //const val THEME_GREEN: Int = 0
        //const val THEME_RED: Int = 1
    }
}