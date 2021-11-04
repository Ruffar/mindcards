package com.raffier.mindcards.service;

import com.raffier.mindcards.model.table.CardTable;
import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.repository.AppDatabase;
import com.raffier.mindcards.repository.table.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    /*private final ImageRepository imageRepository;

    @Autowired
    private ImageService(AppDatabase appDatabase) {
        imageRepository = new ImageRepository(appDatabase);
    }

    public <T extends CardTable> Image getCardImage(T card) {
        return card.getImageId() != 0 ? imageRepository.getFromCard(card) : null;
    }

    public <T extends CardTable> Image setCardImage(T card, MultipartFile file) {

        Image image = getCardImage(card);
        if (image == null) {
            imageRepository.add()
        }

        Path path = Paths.get("./images/"+);
        Files.copy(file.getInputStream(),)

    }*/

}
