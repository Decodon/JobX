package ie.wit.jobx.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface JobStore {
    fun findAll(jobsList:
                MutableLiveData<List<JobModel>>
    )
    fun findAll(userid:String,
                jobsList:
                MutableLiveData<List<JobModel>>
    )
    fun findById(userid:String, jobid: String,
                 job: MutableLiveData<JobModel>
    )
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, job: JobModel)
    fun delete(userid:String, jobid: String)
    fun update(userid:String, jobid: String, job: JobModel)
}
