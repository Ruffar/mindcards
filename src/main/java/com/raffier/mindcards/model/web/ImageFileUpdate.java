package com.raffier.mindcards.model.web;

import com.raffier.mindcards.util.ImageChangeType;
import org.springframework.web.multipart.MultipartFile;

public class ImageFileUpdate extends ImageUpdate {

    private final MultipartFile file;

    public ImageFileUpdate(MultipartFile file) {
        super(ImageChangeType.UPLOAD);
        this.file = file;
    }

    public MultipartFile getFile() {
        return this.file;
    }

}
