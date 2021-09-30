package com.raffier.mindcards.repository;

import com.raffier.mindcards.model.table.CardPack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardPackRepository extends EntityRepository<CardPack,Integer> {

    public CardPackRepository(AppDatabase database) {
        super(database);
    }

    public <S extends CardPack> void save(S entity) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE CardPack SET ownerId=?, title=?, imageId=?, description=? WHERE packId=?")) {
            statement.setInt(2, entity.getOwnerId());
            statement.setString(1, entity.getTitle());
            statement.setInt(3,entity.getImageId());
            statement.setString(4,entity.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CardPack getById(Integer id) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT ownerId, title, imageId, description FROM CardPack WHERE packId=?")) {
            stmnt.setInt(1,id);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new CardPack(id,results.getInt("ownerId"),results.getString("title"),results.getInt("imageId"),results.getString("description"));
            } else {
                System.out.println("Card Pack with ID "+id+" cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <S extends CardPack> CardPack add(S entity) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("INSERT INTO CardPack (ownerId, title, imageId, description) VALUES (?,?,?,?)")) {
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

    public <S extends CardPack> void delete(S entity) {
        deleteById(entity.getPackId());
    }

    public void deleteById(Integer id) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("DELETE FROM CardPack WHERE packId=?")) {
            stmnt.setInt(1,id);
            stmnt.executeUpdate();
            System.out.println("Card Pack with ID "+id+" successfully deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
