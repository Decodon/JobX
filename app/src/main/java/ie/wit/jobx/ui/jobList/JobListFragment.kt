package ie.wit.jobx.ui.jobList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
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

class JobListFragment : Fragment(), JobClickListener {

    lateinit var app: MainXApp
    private lateinit var jobListViewModel: JobListViewModel
    private var _fragBinding: FragmentJobListBinding? = null
    private val fragBinding get() = _fragBinding!!

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
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        jobListViewModel = ViewModelProvider(this).get(JobListViewModel::class.java)
        jobListViewModel.observableJobsList.observe(viewLifecycleOwner, Observer { jobs ->
            jobs?.let { render(jobs) }
        })
        return root
    }

    private fun render(jobsList: List<JobModel>) {
        fragBinding.recyclerView.adapter = JobAdapter(jobsList, this)
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

    override fun onResume() {
        super.onResume()
        jobListViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onJobClick(job: JobModel) {
        val action = JobListFragmentDirections.actionJobListFragmentToJobDetailFragment(job.id)
        findNavController().navigate(action)
    }
}