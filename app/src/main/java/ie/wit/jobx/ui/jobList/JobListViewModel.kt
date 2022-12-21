package ie.wit.jobx.ui.jobList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.jobx.databinding.FragmentJobListBinding
import ie.wit.jobx.main.MainXApp
import ie.wit.jobx.models.JobManager
import ie.wit.jobx.models.JobModel

class JobListViewModel : ViewModel() {

    private val jobsList = MutableLiveData<List<JobModel>>()

    val observableJobsList: LiveData<List<JobModel>>
        get() = jobsList

    init{
        load()
    }

    fun load(){
        jobsList.value = JobManager.findAll()
    }
}