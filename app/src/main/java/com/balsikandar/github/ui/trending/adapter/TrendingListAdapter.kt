package com.balsikandar.github.ui.trending.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.balsikandar.github.R
import com.balsikandar.github.data.TrendingRepo
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_trending_repo.view.*

class TrendingListAdapter(val context: Context?) :
    RecyclerView.Adapter<TrendingListAdapter.TrendingItemViewHolder>() {

    private var trendingRepos: MutableList<TrendingRepo>? = null

    fun updateList(trendingRepos: MutableList<TrendingRepo>) {
        this.trendingRepos = trendingRepos
        notifyDataSetChanged()
    }

    class TrendingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(trendingRepo: TrendingRepo) {
            itemView.authorName.text = trendingRepo.author
            itemView.repositoryName.text = trendingRepo.name

            itemView.repoitoryDescription.text = trendingRepo.description
            itemView.language_name.text = trendingRepo.language
            itemView.stars_count.text = trendingRepo.stars.toString()
            itemView.forks_count.text = trendingRepo.forks.toString()

            updateExpandedIconVisibilities(trendingRepo.isExpanded)

            Glide.with(itemView.context)
                .load(trendingRepo.avatar)
                .apply(RequestOptions().circleCrop())
                .centerCrop()
                .into(itemView.userIcon)
        }

        private fun updateExpandedIconVisibilities(isExpanded: Boolean) {
            itemView.repoitoryDescription.visibility =
                if (isExpanded && itemView.repoitoryDescription.text.isNullOrEmpty().not()) View.VISIBLE else View.GONE

            itemView.language_container.visibility =
                if (isExpanded) View.VISIBLE else View.GONE

            itemView.stars_container.visibility =
                if (isExpanded) View.VISIBLE else View.GONE

            itemView.forks_container.visibility =
                if (isExpanded) View.VISIBLE else View.GONE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_trending_repo, parent, false)
        return TrendingItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trendingRepos?.size ?: 0
    }

    override fun onBindViewHolder(holder: TrendingItemViewHolder, position: Int) {
        val trendingRepo = trendingRepos?.get(position) ?: return
        holder.bindItems(trendingRepo)

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val isExpanded = trendingRepo.isExpanded
                trendingRepo.isExpanded = !isExpanded
                notifyItemChanged(position)
            }

        })
    }

}