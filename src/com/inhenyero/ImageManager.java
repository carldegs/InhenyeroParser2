package com.inhenyero;

class ImageManager implements JSONKeys {
    private static ImageManager instance = null;

    private ImageManager(){}
    static ImageManager getInstance(){
        if(instance == null){
            instance = new ImageManager();
        }

        return instance;
    }
}
