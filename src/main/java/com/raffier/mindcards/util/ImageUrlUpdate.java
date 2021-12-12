package com.raffier.mindcards.util;

public class ImageUrlUpdate extends ImageUpdate {

    private String url;

    public ImageUrlUpdate(String url) {
        super(ImageChangeType.URL);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
