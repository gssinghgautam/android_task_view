package com.example.myapplication.presentation.main.fragments.user_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.FragmentUserDetailsBinding


class UserDetailsFragment : Fragment() {
    private var _binding: FragmentUserDetailsBinding? = null

    val args: UserDetailsFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = args.user
        activity?.title = "${user.firstName} ${user.lastName}"

        binding.user = user;
        binding.detailProfileImage = user.profile

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}