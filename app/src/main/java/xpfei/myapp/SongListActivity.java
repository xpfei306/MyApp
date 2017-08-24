package xpfei.myapp;

import android.os.Bundle;

/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/08/21
 */
public class SongListActivity extends MyBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songlist);
        onSetTitle("歌曲列表");
        onSetLeft(true);
    }

    @Override
    public void onRequestData() {

    }
}
