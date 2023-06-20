package com.example.myapplication.presentation.main.fragments.users;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagingData;

import com.example.myapplication.domain.model.User;
import com.example.myapplication.domain.repository.UserRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserFragmentViewModel extends ViewModel {

    private final UserRepository repository;

    @Inject
    public UserFragmentViewModel(UserRepository repository) {
        this.repository = repository;
    }

    public LiveData<PagingData<User>> getUsers() {
        return repository.getUsers();
    }
}
