package com.example.newsclient.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.newsclient.data.model.Article
import com.example.newsclient.databinding.NewsListItemBinding

class NewsRecyclerViewAdapter() : RecyclerView.Adapter<NewsRecyclerViewAdapter.JsonViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JsonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = NewsListItemBinding.inflate(layoutInflater, parent,false)
        return JsonViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: JsonViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class JsonViewHolder(private val view : NewsListItemBinding) : RecyclerView.ViewHolder(view.root) {

        private var externalLink: String? = null

        fun bind(article: Article) {
            externalLink = article.url
            view.apply {
                tvTitle.text = article.title
                tvDescription.text = article.description
                tvPublishedAt.text = article.publishedAt
                ivArticleImage.load(article.urlToImage)
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(article)
                    }
                }
            }
        }
    }

    private var onItemClickListener :((Article)->Unit)?=null

    fun setOnItemClickListener(listener : (Article)->Unit){
        onItemClickListener = listener
    }
}