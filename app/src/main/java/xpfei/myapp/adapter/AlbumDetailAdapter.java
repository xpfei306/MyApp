package xpfei.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.model.AlbumDeatilInfo;
import xpfei.myapp.model.AlbumInfo;
import xpfei.myapp.model.Song;
import xpfei.myapp.util.ContentValue;
import xpfei.myapp.util.GlideUtils;
import xpfei.mylibrary.utils.CommonUtil;

/**
 * Description: 专辑详情
 * Author: xpfei
 * Date:   2017/09/13
 */
public class AlbumDetailAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private AlbumDeatilInfo deatilInfo;
    private LayoutInflater inflater;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public AlbumDetailAdapter(Context mContext, AlbumDeatilInfo deatilInfo) {
        this.mContext = mContext;
        this.deatilInfo = deatilInfo;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (ContentValue.ViewType.ViewDetail == viewType) {
            View view = inflater.inflate(R.layout.item_detail_info, parent, false);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            int[] metrics = CommonUtil.getScreenSize(mContext);
            params.height = metrics[0];
            view.setLayoutParams(params);
            return new DetailHoder(view);
        } else if (ContentValue.ViewType.ViewButton == viewType) {
            View view = inflater.inflate(R.layout.item_detail_button, parent, false);
            return new ButtonHoder(view);
        } else {
            View view = inflater.inflate(R.layout.item_recyclerview, parent, false);
            return new ListHoder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DetailHoder) {
            initDetail((DetailHoder) holder, deatilInfo.getAlbumInfo());
        } else if (holder instanceof ButtonHoder) {
            initButton((ButtonHoder) holder);
        } else if (holder instanceof ListHoder) {
            initList((ListHoder) holder, deatilInfo.getSonglist());
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ContentValue.ViewType.ViewDetail;
            case 1:
                return ContentValue.ViewType.ViewButton;
            default:
                return ContentValue.ViewType.ViewList;
        }
    }

    @Override
    public int getItemCount() {
        return deatilInfo != null ? 3 : 0;
    }

    public void setData(AlbumDeatilInfo deatilInfo) {
        if (deatilInfo != null) {
            this.deatilInfo = deatilInfo;
            notifyDataSetChanged();
        }
    }

    private class DetailHoder extends RecyclerView.ViewHolder {
        private ImageView imgAlbum;
        private TextView txtAlbumName, txtTime, btnShare, btnPlay, btnCollection;

        public DetailHoder(View itemView) {
            super(itemView);
            imgAlbum = (ImageView) itemView.findViewById(R.id.imgAlbum);
            txtAlbumName = (TextView) itemView.findViewById(R.id.txtAlbumName);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            btnShare = (TextView) itemView.findViewById(R.id.btnShare);
            btnPlay = (TextView) itemView.findViewById(R.id.btnPlay);
            btnCollection = (TextView) itemView.findViewById(R.id.btnCollection);
        }
    }

    private class ButtonHoder extends RecyclerView.ViewHolder {
        private LinearLayout llPlay;

        public ButtonHoder(View itemView) {
            super(itemView);
            llPlay = (LinearLayout) itemView.findViewById(R.id.llPlay);
        }
    }

    private class ListHoder extends RecyclerView.ViewHolder {
        private RecyclerView itemRv;

        public ListHoder(View itemView) {
            super(itemView);
            itemRv = (RecyclerView) itemView.findViewById(R.id.itemRv);
        }
    }

    private void initDetail(DetailHoder hoder, AlbumInfo info) {
        if (info != null) {
            GlideUtils.loadImage(mContext, info.getPic_radio(), R.mipmap.nopic, hoder.imgAlbum);
            hoder.txtAlbumName.setText(info.getTitle());
            hoder.txtTime.setText(info.getPublishtime() + " 发行|" + info.getAuthor());
            hoder.btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            hoder.btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            hoder.btnCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    private void initButton(ButtonHoder hoder) {
        hoder.llPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initList(ListHoder hoder, List<Song> list) {
        SongAdapter adapter = new SongAdapter(mContext, list);
        hoder.itemRv.setLayoutManager(new LinearLayoutManager(mContext));
        hoder.itemRv.setAdapter(adapter);
    }
}
