package com.example.myapplication.presentation.main.fragments.users;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.FragmentUserBinding;
import com.example.myapplication.presentation.main.adapter.UserAdapter;


public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private UserFragmentViewModel viewModel;

    private UserAdapter userAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(UserFragmentViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        setupUI(view.getContext());

        userAdapter.setOnItemClickListener(user -> {
            NavDirections action = UserFragmentDirections.actionUserFragmentToUserDetailsFragment(user);
            navController.navigate(action);
        });

        viewModel.getUsers().observe(getViewLifecycleOwner(), userEntityPagingData -> {
            userAdapter.submitData(getLifecycle(), userEntityPagingData);
        });
    }

    public void setupUI(Context context) {
        RecyclerView userRecyclerView = binding.recyclerView;
        userAdapter = new UserAdapter();
        userRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        userRecyclerView.setAdapter(userAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
