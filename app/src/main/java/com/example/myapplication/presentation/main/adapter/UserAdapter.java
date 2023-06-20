package com.example.myapplication.presentation.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import com.example.myapplication.databinding.UserItemBinding;
import com.example.myapplication.domain.model.User;
import com.example.myapplication.presentation.main.components.UserViewHolder;

public class UserAdapter extends PagingDataAdapter<User, UserViewHolder> {
    public UserAdapter() {
        super(DIFF_CALLBACK);
    }

    private OnItemClickListener clickListener;

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        UserItemBinding binding = UserItemBinding.inflate(inflater, parent, false);
        return new UserViewHolder(binding, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = getItem(position);
        holder.bind(user);
    }

    private static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.equals(newItem);
        }
    };

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }
}
