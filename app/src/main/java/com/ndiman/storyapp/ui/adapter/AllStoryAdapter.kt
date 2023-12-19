package com.ndiman.storyapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ndiman.storyapp.R
import com.ndiman.storyapp.data.pref.ListStory
import com.ndiman.storyapp.data.remote.response.ListStoryItem
import com.ndiman.storyapp.databinding.ItemCardBinding
import com.ndiman.storyapp.ui.detailstory.DetailStoryActivity
import com.ndiman.storyapp.utils.DateFormatter
import java.util.TimeZone

class AllStoryAdapter: PagingDataAdapter<ListStoryItem, AllStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private val sharedElementsMap = mutableMapOf<Int, List<Pair<View, String>>>()

    class MyViewHolder(private val binding: ItemCardBinding): RecyclerView.ViewHolder(binding.root) {
        private fun ImageView.loadImage(photoUrl: String?){
            Glide.with(this.context)
                .load(photoUrl)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error)
                )
                .into(this)
        }

        fun bind(story: ListStoryItem){
            binding.apply {
                imageStory.loadImage(story.photoUrl)
                titleCard.text = story.name
                desCard.text = story.description
                date.text = DateFormatter.formatDate(story.createdAt, TimeZone.getDefault().id)
            }
        }

        fun getSharedElements(): List<Pair<View, String>> {
            return listOf(
                Pair(binding.imageStory, "image"),
                Pair(binding.titleCard, "name"),
                Pair(binding.desCard, "description")
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null){
            val setData = ListStory(
                story.id,
                story.name,
                story.photoUrl,
                story.description,
                story.createdAt
            )
            holder.bind(story)

            holder.itemView.setOnClickListener {

                Toast.makeText(holder.itemView.context, "Storynya ${story.name}", Toast.LENGTH_SHORT).show()
                val intent = Intent(holder.itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_ID, setData)


                val currentItemId = getItemId(position).toInt()
                val sharedElements = sharedElementsMap[currentItemId]
                sharedElements?.let {
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        holder.itemView.context as Activity,
                        *it.toTypedArray()
                    )
                    holder.itemView.context.startActivity(intent, options.toBundle())
                }
            }

        }


    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)

        val currentItemId = getItemId(position).toInt()
        val sharedElements = holder.getSharedElements()

        sharedElementsMap[currentItemId] = sharedElements
    }

    companion object{
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}