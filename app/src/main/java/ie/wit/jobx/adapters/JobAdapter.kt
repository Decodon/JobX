package ie.wit.jobx.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.jobx.R
import ie.wit.jobx.databinding.CardJobBinding
import ie.wit.jobx.models.JobModel

interface JobClickListener {
    fun onJobClick(job: JobModel)
}

class JobAdapter constructor(private var jobs: List<JobModel>,
                             private var listener: JobClickListener)
    : RecyclerView.Adapter<JobAdapter .MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardJobBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val job = jobs[holder.adapterPosition]
        holder.bind(job)
    }

    override fun getItemCount(): Int = jobs.size

    inner class MainHolder(val binding : CardJobBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(job: JobModel) {
            //binding.paymentamount.text = donation.amount.toString()
            //binding.paymentmethod.text = donation.paymentmethod

            binding.job = job
            //binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
            //Include this call to force the bindings to happen immediately
            binding.root.setOnClickListener{ listener.onJobClick(job)}
            binding.executePendingBindings()
        }
    }



}