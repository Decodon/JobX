package ie.wit.jobx.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.jobx.models.JobModel
import ie.wit.jobx.models.JobStore
import timber.log.Timber

object FirebaseDBManager : JobStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference


    override fun findAll(jobsList: MutableLiveData<List<JobModel>>) {
        database.child("jobs")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Job error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<JobModel>()
                    val children = snapshot.children
                    children.forEach {
                        val job = it.getValue(JobModel::class.java)
                        localList.add(job!!)
                    }
                    database.child("jobs")
                        .removeEventListener(this)

                    jobsList.value = localList
                }
            })
    }

    override fun findAll(userid: String, jobsList: MutableLiveData<List<JobModel>>) {

        database.child("user-jobs").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Job error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<JobModel>()
                    val children = snapshot.children
                    children.forEach {
                        val job = it.getValue(JobModel::class.java)
                        localList.add(job!!)
                    }
                    database.child("user-jobs").child(userid)
                        .removeEventListener(this)

                    jobsList.value = localList
                }
            })
    }
    override fun findById(userid: String, jobid: String, job: MutableLiveData<JobModel>) {

        database.child("user-jobs").child(userid)
            .child(jobid).get().addOnSuccessListener {
                job.value = it.getValue(JobModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, job: JobModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("jobs").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        job.uid = key
        val jobValues = job.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/jobs/$key"] = jobValues
        childAdd["/user-jobs/$uid/$key"] = jobValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, jobid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/jobs/$jobid"] = null
        childDelete["/user-jobs/$userid/$jobid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, jobid: String, job: JobModel) {

        val jobValues = job.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["jobs/$jobid"] = jobValues
        childUpdate["user-jobs/$userid/$jobid"] = jobValues

        database.updateChildren(childUpdate)
    }

}

