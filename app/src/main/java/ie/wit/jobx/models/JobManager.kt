package ie.wit.jobx.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object JobManager : JobStore {

    val jobs = ArrayList<JobModel>()





    fun logAll() {
        jobs.forEach{ i("${it}") }
    }

    override fun findAll(jobsList: MutableLiveData<List<JobModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, jobsList: MutableLiveData<List<JobModel>>) {
        TODO("Not yet implemented")
    }

    override fun findById(userid: String, jobid: String, job: MutableLiveData<JobModel>) {
        TODO("Not yet implemented")
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, job: JobModel) {
        TODO("Not yet implemented")
    }

    override fun delete(userid: String, jobid: String) {
        TODO("Not yet implemented")
    }

    override fun update(userid: String, jobid: String, job: JobModel) {
        TODO("Not yet implemented")
    }


}