package com.raffier.mindcards.util;

import org.springframework.web.multipart.MultipartFile;

public class ImageUpdate {

    private final ImageChangeType imageChangeType;

    public ImageUpdate(ImageChangeType type) {
        this.imageChangeType = type;
    }

    public ImageChangeType getChangeType() {
        return imageChangeType;
    }

}
