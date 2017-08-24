package xpfei.myapp.util;

/**
 * Description: 常量
 * Author: xpfei
 * Date:   2017/2/8
 */
public class ContentValue {

    public static final int NET_SUCCESS = 0x901;
    public static final int NET_LOAD = 0x902;
    public static final int NET_FAILURE = 0x903;
    public static final int NET_EMPTY = 0x904;
    public static final int NET_MSV_SUCCESS = 0x905;
    public static final int NET_MSV_LOAD = 0x906;
    public static final int NET_MSV_FAILURE = 0x907;
    public static final int NET_MSV_EMPTY = 0x98;

    public static final String IntentKeyList = "IntentKeyList";
    public static final String IntentKeySer = "IntentKeySer";
    public static final String IntentKeyStr = "IntentKeyStr";
    public static final String IntentKeyInt = "IntentKeyInt";

    public static final String ACACHEkEY_VERSION = "VERSIONCODE";
    public static final String ACACHEkEY_VIEWTHEME = "VIEWTHEME";
    public static final String ACACHE_USER = "ACache_USER";

    //首页布局分类
    public static final int ViewHeader = 1;
    public static final int ViewCategory = 2;
    public static final int ViewNewSong = 3;
    public static final int ViewAlbum = 4;

    public static final class Url {
        public static final String BaseUrl = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&format=json";
    }

    public static final class Json {
        public static final String List = "list";
        public static final String Banner = "pic";
        public static final String SongList = "song_list";
        public static final String ErrorCode = "error_code";
        public static final String Album = "plaze_album_list";
        public static final String RM = "RM";
        public static final String AlbumList = "album_list";
        public static final int Successcode = 22000;

    }
}
