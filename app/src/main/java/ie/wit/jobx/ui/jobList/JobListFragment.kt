package ie.wit.jobx.ui.jobList

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.jobx.R
import ie.wit.jobx.adapters.JobAdapter
import ie.wit.jobx.adapters.JobClickListener
import ie.wit.jobx.databinding.FragmentJobListBinding
import ie.wit.jobx.main.MainXApp
import ie.wit.jobx.models.JobModel
import ie.wit.jobx.ui.auth.LoggedInViewModel
import ie.wit.jobx.utils.*

class JobListFragment : Fragment(), JobClickListener {

    lateinit var app: MainXApp
    private var _fragBinding: FragmentJobListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val jobListViewModel: JobListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    lateinit var loader : AlertDialog

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

        loader = createLoader(requireActivity())

        fragBinding.fab.setOnClickListener {
            val action = JobListFragmentDirections.actionJobListFragmentToJobFragment()
            findNavController().navigate(action)
        }

        showLoader(loader,"Downloading Jobs")

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        jobListViewModel.observableJobsList.observe(viewLifecycleOwner, Observer {
                jobs ->
            jobs?.let {
                render(jobs as ArrayList<JobModel>)
                hideLoader(loader)
                checkSwipeRefresh()
            }
        })

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader,"Deleting Donation")
                val adapter = fragBinding.recyclerView.adapter as JobAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                jobListViewModel.delete(jobListViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as JobModel).uid!!)

                hideLoader(loader)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onJobClick(viewHolder.itemView.tag as JobModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)

        return root
    }


    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_job_list, menu)

                val item = menu.findItem(R.id.toggleDonations) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleDonations: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                toggleDonations.isChecked = false

                toggleDonations.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) jobListViewModel.loadAll()
                    else jobListViewModel.load()
                }
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
             fragBinding.jobsNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
             fragBinding.jobsNotFound.visibility = View.GONE
        }
    }


    override fun onJobClick(job: JobModel) {
        val action = JobListFragmentDirections.actionJobListFragmentToJobDetailFragment(job.uid!!)
        findNavController().navigate(action)
    }



    override fun onResume() {
        super.onResume()
        showLoader(loader,"Downloading Jobs")
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


    private fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader,"Downloading Donations")
            jobListViewModel.load()
        }
    }

    private fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }
}