package sjtu.edu.cn.magic_wardrobe.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sjtu.edu.cn.magic_wardrobe.R;
import sjtu.edu.cn.magic_wardrobe.utils.Config;

/**
 * Created by HgS_1217_ on 2017/11/24.
 */

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;

    private OnItemClickListener onItemClickListener;
    private List<String> pictures;

    public SearchRecyclerViewAdapter(Context context) {
        this.context = context;
        pictures = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
    }

    public SearchRecyclerViewAdapter(Context context, List<String> pics) {
        this.context = context;
        pictures = new ArrayList<>(pics);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_once_search_pic, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setView(pictures.get(position));
        holder.itemView.setOnClickListener((View v) -> {
            int pos = holder.getLayoutPosition();
            onItemClickListener.OnItemClick(pictures.get(pos));
        });
    }

    @Override
    public int getItemCount() {
        if (pictures == null) {
            return 0;
        }
        return pictures.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_search_pic)
        ImageView imgSearchPic;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        private void setView(String path) {
            String url = Config.IMAGE_GET_URL + path;
            Picasso.with(context).load(url).into(imgSearchPic);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(String path);
    }
}
