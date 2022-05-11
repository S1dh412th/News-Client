package com.example.newsclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.newsclient.databinding.FragmentInfoBinding
import com.example.newsclient.presentation.viewmodel.NewsViewModel

class InfoFragment : Fragment() {

    private var binding: FragmentInfoBinding? = null
    private lateinit var newsViewModel : NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(layoutInflater)
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args : InfoFragmentArgs by navArgs()
        val article = args.selectedArticle

        binding?.wvInfo?.apply {
            webViewClient = WebViewClient()
            if (article.url != null) {
                loadUrl(article.url)
            }
        }

        binding?.fabSave?.setOnClickListener {
            newsViewModel.saveArticle(article)
            Toast.makeText(context,"Saved Successfully!",Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = InfoFragment()
    }
}