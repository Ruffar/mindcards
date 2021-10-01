package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.model.table.CardGroup;
import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.repository.AppDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardGroupRepository extends EntityRepository<CardGroup,Integer> {

    public CardGroupRepository(AppDatabase database) {
        super(database);
    }

    public <S extends CardGroup> void save(S entity) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE CardGroup SET packId=?, title=?, imageId=?, description=? WHERE cardGroupId=?")) {
            statement.setInt(2, entity.getPackId());
            statement.setString(1, entity.getTitle());
            statement.setInt(3,entity.getImageId());
            statement.setString(4,entity.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CardGroup getById(Integer id) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT ownerId, title, imageId, description FROM CardGroup WHERE cardGroupId=?")) {
            stmnt.setInt(1,id);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new CardGroup(id,results.getInt("ownerId"),results.getString("title"),results.getInt("imageId"),results.getString("description"));
            } else {
                System.out.println("Card Pack with ID "+id+" cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <S extends CardGroup> CardGroup add(S entity) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("INSERT INTO CardGroup (ownerId, title, imageId, description) VALUES (?,?,?,?)")) {
            stmnt.setInt(1,entity.getOwnerId());
            stmnt.setString(2,entity.getTitle());
            stmnt.setInt(3,entity.getImageId());
            stmnt.setString(4,entity.getDescription());
            stmnt.executeUpdate();
            ResultSet generatedIds = stmnt.getGeneratedKeys();
            if (generatedIds.next()) {
                int newId = generatedIds.getInt(1);
                System.out.println("Card Pack with ID "+newId+" successfully created.");
                return getById(newId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <S extends CardGroup> void delete(S entity) {
        deleteById(entity.getCardGroupId());
    }

    public void deleteById(Integer id) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("DELETE FROM CardGroup WHERE cardGroupId=?")) {
            stmnt.setInt(1,id);
            stmnt.executeUpdate();
            System.out.println("Card Group with ID "+id+" successfully deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Image getImage(int packId) {
        try {
            PreparedStatement statement = database.getConnection().prepareStatement("SELECT Image.imageId, Image.imagePath FROM CardPack, Image WHERE packId = ? AND CardPack.imageId = Image.imageId");
            statement.setInt(1,packId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Image(result.getInt("imageId"),result.getString("imagePath"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
