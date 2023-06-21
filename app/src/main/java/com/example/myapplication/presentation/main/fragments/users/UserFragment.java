package com.example.myapplication.presentation.main.fragments.users;

import static com.example.myapplication.domain.util.App_utilsKt.simplifyErrorMessage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ContentErrorBinding;
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
        ContentErrorBinding errorLayout = binding.errorLayout;

        userAdapter.addLoadStateListener(loadStates -> {
            binding.progressBar.setVisibility(loadStates.getRefresh() instanceof LoadState.Loading ? View.VISIBLE : View.GONE);
            errorLayout.errorView.setVisibility(loadStates.getRefresh() instanceof LoadState.Error ? View.VISIBLE : View.GONE);

            binding.recyclerView.setVisibility(loadStates.getRefresh() instanceof LoadState.Error ? View.GONE : View.VISIBLE);

            if (loadStates.getRefresh() instanceof LoadState.Error) {
                String errorMessage = ((LoadState.Error) loadStates.getRefresh()).getError().getLocalizedMessage();
                errorLayout.txtErrorMessage.setText(simplifyErrorMessage(errorMessage));
            }

            return null;
        });

        userAdapter.setOnItemClickListener(user -> {
            NavDirections action = UserFragmentDirections.actionUserFragmentToUserDetailsFragment(user);
            navController.navigate(action);
        });

        viewModel.getUsers().observe(getViewLifecycleOwner(), userEntityPagingData -> {
            userAdapter.submitData(getLifecycle(), userEntityPagingData);
        });

        binding.errorLayout.retryButton.setOnClickListener(v -> userAdapter.retry());

    }

    public void setupUI(Context context) {
        RecyclerView userRecyclerView = binding.recyclerView;
        userAdapter = new UserAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        userRecyclerView.setHasFixedSize(true);
        userRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(userRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(requireActivity(), R.drawable.horizontal_divier);
        if (horizontalDivider != null) {
            horizontalDecoration.setDrawable(horizontalDivider);
            userRecyclerView.addItemDecoration(horizontalDecoration);
        }
        userRecyclerView.setAdapter(userAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
