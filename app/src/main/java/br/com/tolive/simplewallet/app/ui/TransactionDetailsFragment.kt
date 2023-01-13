package br.com.tolive.simplewallet.app.ui

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import br.com.tolive.simplewallet.app.R
import br.com.tolive.simplewallet.app.data.Transaction
import br.com.tolive.simplewallet.app.databinding.FragmentTransactionDetailsBinding
import br.com.tolive.simplewallet.app.utils.Utils
import br.com.tolive.simplewallet.app.viewmodel.TransactionDetailsViewModel
import br.com.tolive.simplewallet.app.viewmodel.TransactionDetailsViewModelFactory
import java.time.LocalDate
import kotlin.math.abs


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TransactionDetailsFragment : BaseMenuAnimFragment(),
    RemoveTransactionDialog.OnRemoveTransactionListener {

    private var _binding: FragmentTransactionDetailsBinding? = null
    private var transaction: Transaction? = null
    private var tempTransaction: Transaction? = null

    private val viewModel: TransactionDetailsViewModel by viewModels {
        TransactionDetailsViewModelFactory((activity?.application as SimpleWalletApplication).repository)
    }

    private val binding get() = _binding!!

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private var sEditing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuRes = R.menu.menu_details

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        // TODO: handle unsaved changes (also need to check when this fragment is closed another way)
        /*val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    Toast.makeText(mainActivity, "handleOnBackPressed", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTransactionDetailsBinding.inflate(inflater, container, false)

        val transitionName = arguments?.getString(NAME_KEY)

        ViewCompat.setTransitionName(binding.transactionCard, transitionName)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transaction = getParcelable()

        tempTransaction = transaction!!.description?.let {
            Transaction(
                transaction!!.value, transaction!!.type,
                it, transaction!!.transactionDate
            )
        }

        binding.changeType.setOnClickListener {
            changeTransactionType()
        }

        if (transaction != null) {
            if (transaction!!.type == Transaction.TYPE_GAIN) {
                binding.transactionBackground.setBackgroundResource(R.color.green)
            } else if (transaction!!.type == Transaction.TYPE_EXPENSE) {
                binding.transactionBackground.setBackgroundResource(R.color.red)
            }

            binding.transactionDescription.text = transaction!!.description.toString()
            binding.transactionDescription.movementMethod = ScrollingMovementMethod()
            binding.transactionValue.text = Utils.getTransactionValueFormatted(transaction!!)
            binding.transactionDate.text = transaction!!.transactionDate.toString()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_edit -> {
                showUpdateMode()
                true
            }
            R.id.action_delete -> {
                if (transaction != null) {
                    RemoveTransactionDialog(
                        transaction!!,
                        this
                    ).show(
                        parentFragmentManager,
                        RemoveTransactionDialog.TAG
                    )
                }
                true
            }
            R.id.action_save -> {
                updateTransaction()
                showDetailsMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDetailsMode() {
        sEditing = false

        binding.transactionEditDescription.visibility = View.INVISIBLE
        binding.transactionDate.setOnClickListener {}
        binding.transactionEditValue.visibility = View.INVISIBLE
        binding.iconsLayout.visibility = View.GONE

        mainActivity.supportActionBar!!.title =
            resources.getString(R.string.transaction_details_fragment_label)

        binding.transactionCard.startAnimation(
            AnimationUtils.loadAnimation(
                mainActivity,
                R.anim.exit_edit_mode
            )
        )

        binding.transactionDescription.visibility = View.VISIBLE
        binding.transactionValue.visibility = View.VISIBLE

        menu.getItem(0).isVisible = false
        menu.getItem(1).isVisible = true
        menu.getItem(2).isVisible = true


        animateAllVisibleMenuOptions()
    }

    private fun showUpdateMode() {
        sEditing = true

        binding.transactionEditDescription.visibility = View.VISIBLE
        binding.transactionDate.setOnClickListener { showDatePickerDialog() }
        binding.transactionEditValue.visibility = View.VISIBLE
        binding.iconsLayout.visibility = View.VISIBLE

        mainActivity.supportActionBar!!.title =
            resources.getString(R.string.transaction_details_fragment_label_update)

        binding.transactionCard.startAnimation(
            AnimationUtils.loadAnimation(
                mainActivity,
                R.anim.enter_edit_mode
            )
        )

        binding.transactionEditDescription.text = transaction!!.description!!.toEditable()
        binding.transactionEditValue.text = abs(transaction!!.value).toString().toEditable()

        binding.transactionDescription.visibility = View.INVISIBLE
        binding.transactionValue.visibility = View.INVISIBLE

        menu.getItem(0).isVisible = true
        menu.getItem(1).isVisible = false
        menu.getItem(2).isVisible = false

        animateMenuOption(
            menu,
            mainActivity,
            menu.getItem(0).icon,
            MENU_PADDING,
            0
        )
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            mainActivity,
            { _, year, monthOfYear, dayOfMonth ->
                binding.transactionDate.text =
                    LocalDate.of(year, (monthOfYear + 1), dayOfMonth).toString()

                tempTransaction!!.year = year
                tempTransaction!!.month = monthOfYear
                tempTransaction!!.day = dayOfMonth
            }, transaction!!.year, transaction!!.month, transaction!!.day
        )
        datePickerDialog.show()
    }

    private fun changeTransactionType() {
        val red = ContextCompat.getColor(mainActivity, R.color.red)
        val green = ContextCompat.getColor(mainActivity, R.color.green)
        val colorAnimation: ValueAnimator

        if (tempTransaction!!.type == Transaction.TYPE_GAIN) {
            tempTransaction!!.type = Transaction.TYPE_EXPENSE
            colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), green, red)
        } else {
            tempTransaction!!.type = Transaction.TYPE_GAIN
            colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), red, green)
        }

        colorAnimation.duration = COLOR_CHANGE_ANIM_DURATION
        colorAnimation.addUpdateListener { animator ->
            binding.transactionBackground.setBackgroundColor(
                animator.animatedValue as Int
            )
        }
        colorAnimation.start()
    }

    private fun updateTransaction() {
        // Get new values
        //Transaction date is already set by binding.transactionDate click listener
        //Transaction type is already set by binding.changeType click listener
        tempTransaction!!.description = binding.transactionEditDescription.text.toString()
        tempTransaction!!.value =
            Utils.convertEditTextToDouble(binding.transactionEditValue, tempTransaction!!.type)

        // Copy temp values e push to database
        transaction!!.copyValues(tempTransaction!!)
        viewModel.update(transaction!!)

        // Update Details UI
        binding.transactionDescription.text = binding.transactionEditDescription.text.toString()
        binding.transactionValue.text = Utils.getTransactionValueFormatted(tempTransaction!!)
    }

    override fun onRemoveTransaction(transaction: Transaction) {
        viewModel.delete(transaction)
        findNavController().popBackStack()
    }

    private fun getParcelable(): Transaction? =
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                arguments?.getParcelable(
                    TransactionListFragment.KEY_ENTRY_DETAILS,
                    Transaction::class.java
                )
            else -> @Suppress("DEPRECATION")
            arguments?.getParcelable(TransactionListFragment.KEY_ENTRY_DETAILS) as? Transaction
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val COLOR_CHANGE_ANIM_DURATION: Long = 300

        private const val NAME_KEY = "key"
        fun getBundle(transitionName: String?): Bundle {
            val args = Bundle()
            args.putString(NAME_KEY, transitionName)
            return args
        }
    }
}