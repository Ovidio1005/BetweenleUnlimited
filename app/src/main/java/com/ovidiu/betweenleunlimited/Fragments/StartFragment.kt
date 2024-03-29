package com.ovidiu.betweenleunlimited.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.ovidiu.betweenleunlimited.MainActivity
import com.ovidiu.betweenleunlimited.R

class StartFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnPlay).setOnClickListener {
            with(activity as MainActivity){
                wordsLoader.join()
                findNavController().navigate(R.id.action_play)
            }
        }

        view.findViewById<Button>(R.id.btnDaily).setOnClickListener {
            with(activity as MainActivity){
                wordsLoader.join()
                findNavController().navigate(R.id.action_daily)
            }
        }
    }
}