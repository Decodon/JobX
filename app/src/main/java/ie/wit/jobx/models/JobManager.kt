package ie.wit.jobx.models

import timber.log.Timber.i

var lastIda = 0L

internal fun getIda(): Long {
    return lastIda++
}

object JobManager : JobStore {

    val jobs = ArrayList<JobModel>()

    override fun findAll(): List<JobModel> {
        return jobs
    }

    override fun create(job: JobModel) {
        job.id = getIda()
        jobs.add(job)
        logAll()
    }

    override fun update(job: JobModel) {
        var foundJob: JobModel? = jobs.find { j -> j.id == job.id }
        if (foundJob != null) {
            foundJob.title = job.title
            foundJob.description = job.description
            foundJob.net = job.net
            foundJob.vat = job.vat
            foundJob.gross = job.gross
            foundJob.date = job.date
            foundJob.image = job.image
            foundJob.lat = job.lat
            foundJob.lng = job.lng
            foundJob.zoom = job.zoom
            logAll()
        }
    }

    override fun delete(job: JobModel) {
        jobs.remove(job)
    }

    fun logAll() {
        jobs.forEach{ i("${it}") }
    }
}