package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.index.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<MyItem> itemList;

    public MyAdapter(List<MyItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyItem item = itemList.get(position);
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewDescription.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewDescription;
        public ImageView moreButton;

        private View overlayView;
        private FrameLayout parentContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            moreButton = itemView.findViewById(R.id.more_ic);
            parentContainer = itemView.findViewById(R.id.container_lay); // Assumindo que o layout tenha um FrameLayout com este id

            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Toast.makeText(v.getContext(), "More button clicked at position: " + position, Toast.LENGTH_SHORT).show();

                        LayoutInflater inflater = LayoutInflater.from(v.getContext());
                        overlayView = inflater.inflate(R.layout.activity_more, parentContainer, false);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                        parentContainer.addView(overlayView, layoutParams);

                        int topMarginDp = 70;
                        int bottomMarginDp = 60;
                        float scale = v.getResources().getDisplayMetrics().density;
                        layoutParams.topMargin = (int) (topMarginDp * scale + 0.5f);
                        layoutParams.bottomMargin = (int) (bottomMarginDp * scale + 0.5f);

/*                        TextView skipButton = overlayView.findViewById(R.id.skip_tutorial);
                        ImageView prevButton = overlayView.findViewById(R.id.previous_button);
                        skipButton.setOnClickListener(view -> removeTutorial());
                        prevButton.setOnClickListener(view -> removeTutorial());*/
                    }
                }
            });
        }

        private void removeTutorial() {
            if (overlayView != null) {
                parentContainer.removeView(overlayView);
                overlayView = null;
            }
        }
    }
}
