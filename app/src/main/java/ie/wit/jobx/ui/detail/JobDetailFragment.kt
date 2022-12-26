package ie.wit.jobx.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.wit.jobx.databinding.FragmentJobDetailBinding
import ie.wit.jobx.ui.auth.LoggedInViewModel
import ie.wit.jobx.ui.jobList.JobListViewModel

class JobDetailFragment : Fragment() {

    private val args by navArgs<JobDetailFragmentArgs>()
    private lateinit var  detailViewModel: JobDetailViewModel
    private var _fragBinding: FragmentJobDetailBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val jobListViewModel : JobListViewModel by activityViewModels()

//    companion object {
//        fun newInstance() = JobDetailFragment()
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentJobDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this).get(JobDetailViewModel::class.java)
        detailViewModel.observableJob.observe(viewLifecycleOwner, Observer { render() })

        fragBinding.editJobButton.setOnClickListener {
            detailViewModel.updateJob(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.jobid, fragBinding.jobvm?.observableJob!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteJobButton.setOnClickListener {
            jobListViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                detailViewModel.observableJob.value?.uid!!)
            findNavController().navigateUp()
        }

        return root
    }


    private fun render() {
        fragBinding.editMessage.setText("A Message")
        fragBinding.editUpvotes.setText("0")
        fragBinding.jobvm = detailViewModel
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getJob(loggedInViewModel.liveFirebaseUser.value?.uid!!,
        args.jobid)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}