package com.example.grozon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment

class Help : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_help, container, false)

        // Initialize the ListView
        val listView: ListView = view.findViewById(R.id.listView)

        // Create a list of items and their content
        val items = listOf("Version", "Privacy Policy", "Terms of Use", "Support")
        val contents = mapOf(
            "Version" to "App Version: 1.0.0",
            "Privacy Policy" to "Your privacy is important to us. Please read the full policy on our website.",
            "Terms of Use" to "By using this app, you agree to our terms and conditions.",
            "Support" to "Contact us at support@example.com for assistance."
        )

        // Create a custom adapter
        val adapter = HelpAdapter(requireContext(), items, contents)

        // Attach the adapter to the ListView
        listView.adapter = adapter

        // Handle item clicks
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                adapter.toggleContentVisibility(position)
            }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = Help()
    }
}
