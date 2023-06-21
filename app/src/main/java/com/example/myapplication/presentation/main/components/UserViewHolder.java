package com.example.myapplication.presentation.main.components;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.UserItemBinding;
import com.example.myapplication.domain.model.User;
import com.example.myapplication.presentation.main.adapter.UserAdapter;


public class UserViewHolder extends RecyclerView.ViewHolder {
    private final UserItemBinding binding;

    public UserViewHolder(UserItemBinding binding, UserAdapter.OnItemClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;

        itemView.setOnClickListener(v -> {
            int position = getBindingAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                User user = binding.getUser();
                if (user != null) {
                    listener.onItemClick(user);
                }
            }
        });
    }

    public void bind(User user) {
        if (user != null) {
            binding.setUser(user);
            binding.setImageUrl(user.getProfile());
        }
        binding.executePendingBindings();
    }
}