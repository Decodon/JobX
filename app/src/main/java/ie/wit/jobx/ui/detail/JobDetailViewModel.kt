package ie.wit.jobx.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.jobx.models.JobManager
import ie.wit.jobx.models.JobModel

class JobDetailViewModel : ViewModel() {

    private val job = MutableLiveData<JobModel>()

    val observableJob: LiveData<JobModel>
        get() = job

    fun getJob(id: Long){
        job.value = JobManager.findById(id)
    }
}