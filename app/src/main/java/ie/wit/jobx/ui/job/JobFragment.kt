package ie.wit.jobx.ui.job

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import ie.wit.jobx.R
import ie.wit.jobx.databinding.FragmentJobBinding
import ie.wit.jobx.main.MainXApp
import ie.wit.jobx.models.JobModel
import java.util.*


class JobFragment : Fragment() {

    lateinit var app: MainXApp
    private var _fragBinding: FragmentJobBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var jobViewModel: JobViewModel
    val cal = Calendar.getInstance()
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val day = cal.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainXApp
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentJobBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_donate)
        //setupMenu()

        jobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)
        jobViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })
        setButtonListener(fragBinding)
        setOnClickListener(fragBinding)
        setupMenu()
        return root;
    }


    private fun setButtonListener(layout: FragmentJobBinding) {
        layout.btnAdd.setOnClickListener {
            val title = layout.jobTitle.text.toString()
            val description = layout.description.text.toString()
            val netString = layout.paymentAmount.text.toString()
            val net = netString.toDouble()

            var vatRate: Double = 0.00

            if (layout.paymentMethod.checkedRadioButtonId == R.id.Zero) {
                vatRate = 0.00
            } else if (layout.paymentMethod.checkedRadioButtonId == R.id.Reduced) {
                vatRate = 0.135
            } else {
                vatRate = 0.23
            }

            val vat = (vatRate * net).round(2)
            val gross = (net + vat).round(2)

            val date = layout.dateView.text.toString()

            jobViewModel.addJob(JobModel(title = title, description = description, net = net, vat = vat, gross = gross, date = date))
            }
        }


    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.donationError),Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
    }

    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return kotlin.math.round(this * multiplier) / multiplier
    }


    private fun setOnClickListener(layout: FragmentJobBinding) {
        layout.dateButton.setOnClickListener{
            val datePicked = DatePickerDialog(
                requireContext(),
                { _, Year, Month, Day ->
                    val Month = Month + 1
                    layout.dateView.setText("$Day/$Month/$Year")
                }, year, month, day
            )
            datePicked.show()
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_donate, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }       }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}


