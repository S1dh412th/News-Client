package com.example.newsclient

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsclient.databinding.FragmentNewsBinding
import com.example.newsclient.presentation.adapter.NewsRecyclerViewAdapter
import com.example.newsclient.presentation.viewmodel.NewsViewModel

class NewsFragment : Fragment() {

    private var binding: FragmentNewsBinding? = null
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var newsViewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(layoutInflater)
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNews(1)
    }

    private fun getNews(noOfItem: Int) {

        manager = GridLayoutManager(this.context, noOfItem)

        lifecycleScope.launchWhenStarted {

            binding?.newsRecyclerView?.apply {
                newsViewModel.newsFlow.collect { news ->
                    if (news != null) {
                        val adapter2 = NewsRecyclerViewAdapter()
                        adapter2.differ.submitList(news.articles.toList())

                        adapter = adapter2

                        adapter2.setOnItemClickListener {
                            val bundle = Bundle().apply {
                                putSerializable("selected_article",it)
                            }
                            findNavController().navigate(
                                R.id.action_newsFragment_to_infoFragment,
                                bundle
                            )
                        }

                        layoutManager = manager
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = NewsFragment()
    }

}