package ie.wit.jobx.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.jobx.firebase.FirebaseDBManager
import ie.wit.jobx.models.JobManager
import ie.wit.jobx.models.JobModel
import timber.log.Timber

class JobDetailViewModel : ViewModel() {
    private val job = MutableLiveData<JobModel>()

    var  observableJob: LiveData<JobModel>
        get() = job
        set(value) {job.value = value.value}

    fun getJob(userid:String, id: String){
        try {
            FirebaseDBManager.findById(userid, id, job)
            Timber.i("Detail getJob() Success : ${
                job.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getDonation() Error : $e.message")
        }
    }

    fun updateJob(userid:String, id: String,job: JobModel) {
        try {
            FirebaseDBManager.update(userid, id, job)
            Timber.i("Detail update() Success : $job")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }

}