package com.raffier.mindcards.model.web;

import com.raffier.mindcards.util.ImageChangeType;

public class ImageUpdate {

    private final ImageChangeType imageChangeType;

    public ImageUpdate(ImageChangeType type) {
        this.imageChangeType = type;
    }

    public ImageChangeType getChangeType() {
        return imageChangeType;
    }

}
