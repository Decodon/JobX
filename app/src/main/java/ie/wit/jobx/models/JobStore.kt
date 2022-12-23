package ie.wit.jobx.models

interface JobStore {
    fun findAll(): List<JobModel>
    fun create(job: JobModel)
    fun update(job: JobModel)
    fun delete(job: JobModel)
    fun findById(id: Long) : JobModel?
}
