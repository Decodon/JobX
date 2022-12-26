package ie.wit.jobx.ui.jobList

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.jobx.R
import ie.wit.jobx.adapters.JobAdapter
import ie.wit.jobx.adapters.JobClickListener
import ie.wit.jobx.databinding.FragmentJobListBinding
import ie.wit.jobx.main.MainXApp
import ie.wit.jobx.models.JobModel
import ie.wit.jobx.ui.auth.LoggedInViewModel

class JobListFragment : Fragment(), JobClickListener {

    lateinit var app: MainXApp
    private var _fragBinding: FragmentJobListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val jobListViewModel: JobListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    //lateinit var loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentJobListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()

        //loader = createLoader(requireActivity())

        fragBinding.fab.setOnClickListener {
            val action = JobListFragmentDirections.actionJobListFragmentToJobFragment()
            findNavController().navigate(action)
        }

        //showLoader(loader,"Downloading Jobs")

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        jobListViewModel.observableJobsList.observe(viewLifecycleOwner, Observer {
                jobs ->
            jobs?.let {
                render(jobs as ArrayList<JobModel>)
               // hideLoader(loader)
               // checkSwipeRefresh()
            }
        })

        return root
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
            }     }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    private fun render(jobsList: ArrayList<JobModel>) {
        fragBinding.recyclerView.adapter = JobAdapter(jobsList,this)
        if (jobsList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            // fragBinding.jobsNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            // fragBinding.jobsNotFound.visibility = View.GONE
        }
    }


    override fun onJobClick(job: JobModel) {
        val action = JobListFragmentDirections.actionJobListFragmentToJobDetailFragment(job.uid!!)
        findNavController().navigate(action)
    }



    override fun onResume() {
        super.onResume()
        //showLoader(loader,"Downloading Jobs")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                jobListViewModel.liveFirebaseUser.value = firebaseUser
                jobListViewModel.load()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}