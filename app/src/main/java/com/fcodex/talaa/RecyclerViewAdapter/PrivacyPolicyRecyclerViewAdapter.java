package com.fcodex.talaa.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.R;
import com.fcodex.talaa.SettingActivities.PrivacyPolicyActivity;

import java.util.ArrayList;
import java.util.List;

public class PrivacyPolicyRecyclerViewAdapter extends RecyclerView.Adapter<privacyPolicy> {

    private Context context;
    private List<Modal> privacyPolicyModal;

    public PrivacyPolicyRecyclerViewAdapter(Context context, List<Modal> privacyPolicyModal) {
        this.context = context;
        this.privacyPolicyModal = privacyPolicyModal;
    }

    @NonNull
    @Override
    public privacyPolicy onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_privacy_policy_view,  parent, false);
        return new privacyPolicy(view);
    }

    @Override
    public void onBindViewHolder(@NonNull privacyPolicy holder, int position) {
        holder.privacyPolicyTextView.setText(privacyPolicyModal.get(position).getPrivacyPolicy());
    }

    @Override
    public int getItemCount() {
        return privacyPolicyModal.size();
    }
}
class privacyPolicy extends RecyclerView.ViewHolder {

    TextView privacyPolicyTextView;

    public privacyPolicy(@NonNull View itemView) {
        super(itemView);

        privacyPolicyTextView = itemView.findViewById(R.id.privacyPolicyTextView);

    }
}