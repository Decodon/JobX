package ie.wit.jobx.ui.job

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.jobx.firebase.FirebaseDBManager
import ie.wit.jobx.firebase.FirebaseImageManager
import ie.wit.jobx.models.JobManager
import ie.wit.jobx.models.JobModel

class JobViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addJob(firebaseUser: MutableLiveData<FirebaseUser>,
               job: JobModel) {
        status.value = try {
            job.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseDBManager.create(firebaseUser,job)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}

