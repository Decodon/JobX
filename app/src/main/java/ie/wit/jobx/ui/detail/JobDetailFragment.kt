package ie.wit.jobx.ui.detail

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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
    lateinit var camera_open_id: Button
    lateinit var click_image_id: ImageView

    companion object {
        fun newInstance() = JobDetailFragment()
        private const val pic_id = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentJobDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this).get(JobDetailViewModel::class.java)
        detailViewModel.observableJob.observe(viewLifecycleOwner, Observer { render() })

        fragBinding.buttonEmail.setOnClickListener{
            email(requireContext(),
                listOf(fragBinding.jobvm?.observableJob!!.value!!.email, "decodon88@gmail.com") as List<String>,
                (fragBinding.jobvm?.observableJob!!.value!!.title),
                "Work completed on : " + (fragBinding.jobvm?.observableJob!!.value!!.date) + "\n"
                        + "Description of work : " + (fragBinding.jobvm?.observableJob!!.value!!.description) + "\n"
                        + "Gross amount of work : " + (fragBinding.jobvm?.observableJob!!.value!!.gross))
        }

        fragBinding.editJobButton.setOnClickListener {
            detailViewModel.updateJob(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.jobid, fragBinding.jobvm?.observableJob!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteJobButton.setOnClickListener {
            jobListViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                detailViewModel.observableJob.value?.uid!!)
            findNavController().navigateUp()
        }

        camera_open_id = fragBinding.cameraButton
        click_image_id = fragBinding.clickImage

        camera_open_id.setOnClickListener {
            val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(camera_intent, pic_id)
        }

        return root
    }

    private fun email(context: Context, emailReceiver: List<String>, title: String, message: String){
        val email = Intent(Intent.ACTION_SEND)
        email.type = "plain/text"
        email.putExtra(Intent.EXTRA_EMAIL, emailReceiver.toTypedArray())
        email.putExtra(Intent.EXTRA_SUBJECT, title)
        email.putExtra(Intent.EXTRA_TEXT, message)
        context.startActivity(Intent.createChooser(email,"Select email app"))
    }


    private fun render() {
//        fragBinding.editMessage.setText("A Message")
//        fragBinding.editUpvotes.setText("0")
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pic_id) {
            val photo = data!!.extras!!["data"] as Bitmap?
            click_image_id.setImageBitmap(photo)
        }
    }
}