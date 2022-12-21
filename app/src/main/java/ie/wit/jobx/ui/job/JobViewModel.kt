package ie.wit.jobx.ui.job

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.jobx.models.JobManager
import ie.wit.jobx.models.JobModel

class JobViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addJob(job: JobModel) {
        status.value = try {
            JobManager.create(job)
            true
        } catch (e: IllegalArgumentException){
            false
        }
    }
}

