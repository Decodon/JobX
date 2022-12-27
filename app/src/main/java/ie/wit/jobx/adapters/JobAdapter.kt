package ie.wit.jobx.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.jobx.R
import ie.wit.jobx.databinding.CardJobBinding
import ie.wit.jobx.models.JobModel
import ie.wit.jobx.utils.customTransformation

interface JobClickListener {
    fun onJobClick(job: JobModel)
}

class JobAdapter constructor(private var jobs: ArrayList<JobModel>,
                                  private val listener: JobClickListener,
                                  private val readOnly: Boolean)
    : RecyclerView.Adapter<JobAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardJobBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding,readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val job = jobs[holder.adapterPosition]
        holder.bind(job,listener)
    }

    fun removeAt(position: Int) {
        jobs.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = jobs.size

    inner class MainHolder(val binding : CardJobBinding, private val readOnly : Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(job: JobModel, listener: JobClickListener) {
            binding.job = job
            binding.root.tag = job
            Picasso.get().load(job.profilepic.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.imageIcon)
            binding.root.setOnClickListener{ listener.onJobClick(job)}
            binding.executePendingBindings()
        }
    }



}