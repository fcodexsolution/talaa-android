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

import java.util.List;

public class FaqRecyclerViewAdapter extends RecyclerView.Adapter<faq> {

    private Context context;
    private List<Modal> faqModal;

    public FaqRecyclerViewAdapter(Context context, List<Modal> faqModal) {
        this.context = context;
        this.faqModal = faqModal;
    }

    @NonNull
    @Override
    public faq onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_faq_view,  parent, false);
        return new faq(view);
    }

    @Override
    public void onBindViewHolder(@NonNull faq holder, int position) {
        holder.faqQuestion.setText(faqModal.get(position).getFaqQuestion());
        holder.faqAnswer.setText(faqModal.get(position).getFaqAnswer());
    }

    @Override
    public int getItemCount() {
        return faqModal.size();
    }
}
class faq extends RecyclerView.ViewHolder {

    TextView faqQuestion;
    TextView faqAnswer;

    public faq(@NonNull View itemView) {
        super(itemView);

        faqQuestion = itemView.findViewById(R.id.faqQuestion);
        faqAnswer = itemView.findViewById(R.id.faqAnswer);

    }
}