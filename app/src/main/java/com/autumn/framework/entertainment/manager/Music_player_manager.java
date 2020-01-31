package com.autumn.framework.entertainment.manager;

public class Music_player_manager {

    private static String music_url = "";
    private static String music_service = "";
    private static String url = "";
    private static String hot_music_service = "";
    private static String search_music_service = "";
    private static String music_main_url = "";
    private static String response = "";

    public static String getMainMusicUrl(){ return music_main_url;}

    public static void initUrl(){
        url = "";
    }

    public static String getUrl(){
        return url;
    }

    public static void setMusicService(String service){music_service = service;}

    public static void setMusic(String key) {
        music_url = key;
    }

    public static String getMusic(){
        return music_url;
    }

    public static String getService() {
        return music_service;
    }

    public static void setMusicUrl(String url_302) {
        url = url_302;
    }

    public static void setHotMusicService(String s) {
        hot_music_service = s;
    }

    public static String getHot_music_service(){
        return hot_music_service;
    }

    public static void setSearchMusicService(String s){
        search_music_service = s;
    }

    public static String getSearchMusicService(){
        return search_music_service;
    }

    public static void changeService(String type) {
        switch (type){
            case "HotMusic":
                music_service = hot_music_service;
                break;
            case "SearchPage":
                music_service = search_music_service;
                break;
        }
    }

    public static void setMainMusicUrl(String key) {
        music_main_url = key;
    }

    public static void setMvCheckResponse(String s) {
        response = s;
    }

    public static String getMvCheckResponse(){
        return response;
    }
}
