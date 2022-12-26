package ie.wit.jobx.main

import android.app.Application
//import ie.wit.jobx.models.DonationMemStore
//import ie.wit.jobx.models.JobMemStore
import timber.log.Timber

class MainXApp : Application() {

    //lateinit var donationsStore: DonationStore
    //lateinit var jobsStore: JobStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //donationsStore = DonationMemStore()
        //jobsStore = JobMemStore()
        Timber.i("DonationX Application Started")
    }
}