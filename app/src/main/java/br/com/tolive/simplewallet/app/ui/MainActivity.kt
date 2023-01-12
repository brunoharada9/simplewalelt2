package br.com.tolive.simplewallet.app.ui

import android.graphics.Paint
import android.os.Bundle
import android.view.animation.TranslateAnimation
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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var mainActivityListener: OnMainActivityListener? = null

    private var summaryHidden: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}
        binding.adView.loadAd(AdRequest.Builder().build())

        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val cal: Calendar = Calendar.getInstance()
        binding.summaryTitle.text = getString(
            R.string.summary_date,
            cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())?.uppercase(),
            cal.get(Calendar.YEAR)
        )

        binding.fab.setOnClickListener {
            val transactionListFragment: TransactionListFragment? =
                navHostFragment.childFragmentManager.fragments[0] as? TransactionListFragment
            if (transactionListFragment != null) {
                mainActivityListener = transactionListFragment
                AddTransactionDialog(mainActivityListener as TransactionListFragment).show(
                    supportFragmentManager,
                    AddTransactionDialog.TAG
                )
            }
        }
    }

    // Create Listener to handle the database insertion on TransactionListFragment
    interface OnMainActivityListener {
        fun onAddTransaction(transaction: Transaction)
        fun onFilterApplied(month: Int, year: Int)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun updateSummarySum(sum: Double) {
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

    fun updateSummaryMonth(month: Int, year: Int) {
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.YEAR, year)
        binding.summaryTitle.text = getString(
            R.string.summary_date,
            cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())?.uppercase(),
            cal.get(Calendar.YEAR)
        )
    }

    fun showSummaryAndFab() {
        if (summaryHidden) {
            binding.fab.show()

            val animate = TranslateAnimation(
                ANIMATION_0F, ANIMATION_0F,
                binding.summaryCard.height.toFloat(), ANIMATION_0F
            )
            animate.duration = SUMMARY_ANIMATION_TIME
            animate.fillAfter = true
            binding.summaryCard.startAnimation(animate)
        }
        summaryHidden = false
    }

    fun hideSummaryAndFab() {
        binding.fab.hide()

        val animate = TranslateAnimation(
            ANIMATION_0F, ANIMATION_0F,
            ANIMATION_0F, binding.summaryCard.height.toFloat()
        )
        animate.duration = SUMMARY_ANIMATION_TIME
        animate.fillAfter = true
        binding.summaryCard.startAnimation(animate)
        summaryHidden = true
    }

    companion object {
        const val SUMMARY_ANIMATION_TIME: Long = 300

        const val ANIMATION_0F = 0F
        const val SUM_VALUE_STROKE_SIZE = 2F

        //const val THEME_GREEN: Int = 0
        //const val THEME_RED: Int = 1
    }
}