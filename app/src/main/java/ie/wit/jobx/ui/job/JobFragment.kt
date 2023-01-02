package ie.wit.jobx.ui.job

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import ie.wit.jobx.MapActivity
import ie.wit.jobx.R
import ie.wit.jobx.databinding.FragmentJobBinding
import ie.wit.jobx.main.MainXApp
import ie.wit.jobx.models.JobModel
import ie.wit.jobx.models.Location
import ie.wit.jobx.ui.auth.LoggedInViewModel
import ie.wit.jobx.ui.jobList.JobListViewModel
import timber.log.Timber
import timber.log.Timber.i

import java.util.*


class JobFragment : Fragment() {

    lateinit var app: MainXApp
    private var _fragBinding: FragmentJobBinding? = null
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val fragBinding get() = _fragBinding!!
    private val jobListViewModel : JobListViewModel by activityViewModels()
    private lateinit var jobViewModel: JobViewModel
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    val cal = Calendar.getInstance()
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val day = cal.get(Calendar.DAY_OF_MONTH)
    var totalGross: Double = 0.0
    var job = JobModel()



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
        activity?.title = getString(R.string.action_job)
        //setupMenu()

        jobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)
        jobViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })

        registerLocationPickerCallback()
        setButtonListener(fragBinding)
        setOnClickListener(fragBinding)
        setLocationButtonListener(fragBinding)
        //setImageButtonListener(fragBinding)
        setupMenu()
        return root;
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


    private fun setButtonListener(layout: FragmentJobBinding) {
        layout.btnAdd.setOnClickListener {
            var title = layout.jobTitle.text.toString()
            var description = layout.description.text.toString()
            var netString = layout.paymentAmount.text.toString()
            var net = netString.toDouble()

            var vatRate: Double = 0.00

            if (layout.paymentMethod.checkedRadioButtonId == R.id.Zero) {
                vatRate = 0.00
            } else if (layout.paymentMethod.checkedRadioButtonId == R.id.Reduced) {
                vatRate = 0.135
            } else {
                vatRate = 0.23
            }

            var vat = (vatRate * net).round(2)
            var gross = (net + vat).round(2)

            var date = layout.dateView.text.toString()

            if(layout.paymentAmount.text.isEmpty())
                Toast.makeText(context,"Please enter a value for Net", Toast.LENGTH_LONG).show()
            else {
                jobViewModel.addJob(loggedInViewModel.liveFirebaseUser, JobModel(title = title, description = description, net = net, vat = vat, gross = gross, date = date, email = loggedInViewModel.liveFirebaseUser.value?.email!!, lat = job.lat, lng = job.lng, zoom = job.zoom))
            }
            }
        }

    private fun setLocationButtonListener(layout: FragmentJobBinding){
        layout.jobLocation.setOnClickListener{
            var location = Location(51.6203, -8.9055, 15f)
            if (job.zoom != 0f) {
                location.lat = job.lat
                location.lng = job.lng
                location.zoom = job.zoom
            }
            val launcherIntent = Intent(context, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
    }

//    private fun setImageButtonListener(layout: FragmentJobBinding){
//        layout.chooseImage.setOnClickListener {
//            i ("Set Location Pressed")
//        }
//    }


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
                menuInflater.inflate(R.menu.menu_job, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }       }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        //totalGross = jobListViewModel.observableJobsList.value!!.sumOf { it.gross }
        fragBinding.totalGrossSoFar.text = String.format(getString(R.string.totalSoFarJob),totalGross)
    }


    private fun registerLocationPickerCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            job.lat = location.lat
                            job.lng = location.lng
                            job.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}


