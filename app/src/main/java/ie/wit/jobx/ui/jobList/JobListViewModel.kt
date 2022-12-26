package ie.wit.jobx.ui.jobList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.jobx.databinding.FragmentJobListBinding
import ie.wit.jobx.firebase.FirebaseDBManager
import ie.wit.jobx.main.MainXApp
import ie.wit.jobx.models.JobManager
import ie.wit.jobx.models.JobModel
import timber.log.Timber
import java.lang.Exception

class JobListViewModel : ViewModel() {

    private val jobsList = MutableLiveData<List<JobModel>>()

    val observableJobsList: LiveData<List<JobModel>>
        get() = jobsList


    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init {
        load()
    }

    fun load() {
        try {
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!, jobsList)
            Timber.i("Report Load Success : ${jobsList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid, id)
            Timber.i("Report Delete Success")
        } catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }
}