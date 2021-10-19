package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
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

    private void throwEntityNotFound(Integer id) { throw new EntityNotFoundException("Card Group", id); }

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
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT packId, title, imageId, description FROM CardGroup WHERE cardGroupId=?")) {
            stmnt.setInt(1,id);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new CardGroup(id,results.getInt("packId"),results.getString("title"),results.getInt("imageId"),results.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throwEntityNotFound(id);
        return null;
    }

    public <S extends CardGroup> CardGroup add(S entity) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("INSERT INTO CardGroup (packId, title, imageId, description) VALUES (?,?,?,?)")) {
            stmnt.setInt(1,entity.getPackId());
            stmnt.setString(2,entity.getTitle());
            stmnt.setInt(3,entity.getImageId());
            stmnt.setString(4,entity.getDescription());
            stmnt.executeUpdate();
            ResultSet generatedIds = stmnt.getGeneratedKeys();
            if (generatedIds.next()) {
                int newId = generatedIds.getInt(1);
                System.out.println("Card Group with ID "+newId+" successfully created.");
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

}
