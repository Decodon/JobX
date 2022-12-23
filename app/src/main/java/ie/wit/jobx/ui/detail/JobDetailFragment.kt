package ie.wit.jobx.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import ie.wit.jobx.R
import ie.wit.jobx.databinding.FragmentJobDetailBinding
import ie.wit.jobx.databinding.FragmentJobListBinding

class JobDetailFragment : Fragment() {

    private val args by navArgs<JobDetailFragmentArgs>()
    private lateinit var  detailViewModel: JobDetailViewModel
    private var _fragBinding: FragmentJobDetailBinding? = null
    private val fragBinding get() = _fragBinding!!

    companion object {
        fun newInstance() = JobDetailFragment()
    }

    private lateinit var viewModel: JobDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentJobDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this).get(JobDetailViewModel::class.java)
        detailViewModel.observableJob.observe(viewLifecycleOwner, Observer { render() })
        return root
    }


    private fun render() {
        fragBinding.editMessage.setText("A Message")
        fragBinding.editUpvotes.setText("0")
        fragBinding.jobvm = detailViewModel
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getJob(args.jobid)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}