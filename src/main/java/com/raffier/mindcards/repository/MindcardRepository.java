package com.raffier.mindcards.repository;

import com.raffier.mindcards.model.table.Mindcard;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MindcardRepository extends EntityRepository<Mindcard,Integer> {

    public MindcardRepository(AppDatabase database) {
        super(database);
    }

    public <S extends Mindcard> void save(S entity) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE Mindcard SET packId=?,title=?,imageId=?,description=? WHERE mindcardId=?")) {
            statement.setInt(2, entity.getPackId());
            statement.setString(1, entity.getTitle());
            statement.setInt(3,entity.getImageId());
            statement.setString(4,entity.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Mindcard getById(Integer id) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT packId, title, imageId, description FROM Mindcard WHERE mindcardId=?")) {
            stmnt.setInt(1,id);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new Mindcard(id,results.getInt("packId"),results.getString("title"),results.getInt("imageId"),results.getString("description"));
            } else {
                //throw new RuntimeException("Mindcard with ID "+mindcardId+" cannot be found...");
                System.out.println("Mindcard with ID "+id+" cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <S extends Mindcard> Mindcard add(S entity) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("INSERT INTO Mindcard (packId, title, imageId, description) VALUES (?,?,?,?)")) {
            stmnt.setInt(1,entity.getPack().getPackId());
            stmnt.setString(2,entity.getTitle());
            stmnt.setInt(3,entity.getImage().getImageId());
            stmnt.setString(4,entity.getDescription());
            stmnt.executeUpdate();
            ResultSet generatedIds = stmnt.getGeneratedKeys();
            if (generatedIds.next()) {
                int newId = generatedIds.getInt(1);
                System.out.println("Mindcard with ID "+newId+" successfully created.");
                return getById(newId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <S extends Mindcard> void delete(S entity) {
        deleteById(entity.getMindcardId());
    }

    public void deleteById(Integer id) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("DELETE FROM Mindcard WHERE mindcardId=?")) {
            stmnt.setInt(1,id);
            stmnt.executeUpdate();
            System.out.println("Mindcard with ID "+id+" successfully deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
