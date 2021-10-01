package com.raffier.mindcards.repository.table;

import com.raffier.mindcards.model.table.Image;
import com.raffier.mindcards.model.table.Infocard;
import com.raffier.mindcards.repository.AppDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InfocardRepository extends EntityRepository<Infocard,Integer> {

    public InfocardRepository(AppDatabase database) {
        super(database);
    }

    public <S extends Infocard> void save(S entity) {
        try (PreparedStatement statement = database.getConnection().prepareStatement("UPDATE Infocard SET mindcardId=?,imageId=?,description=? WHERE infocardId=?")) {
            statement.setInt(1, entity.getMindcardId());
            statement.setInt(2,entity.getImageId());
            statement.setString(3,entity.getDescription());
            statement.setInt(4,entity.getInfocardId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Infocard getById(Integer id) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("SELECT mindcardId, imageId, description FROM Infocard WHERE infocardId=?")) {
            stmnt.setInt(1,id);
            ResultSet results = stmnt.executeQuery();
            if (results.next()) {
                return new Infocard(id,results.getInt("mindcardId"),results.getInt("imageId"),results.getString("description"));
            } else {
                System.out.println("Infocard with ID "+id+" cannot be found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Infocard> getFromMindcard(int mindcardId) {
        List<Infocard> outList = new ArrayList<>();
        try {
            PreparedStatement statement = database.getConnection().prepareStatement("SELECT infocardId, imageId, description FROM Infocard WHERE mindcardId = ?");
            statement.setInt(1,mindcardId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                outList.add(new Infocard(result.getInt("infocardId"),mindcardId,result.getInt("imageId"),result.getString("description")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outList;
    }

    public <S extends Infocard> Infocard add(S entity) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("INSERT INTO Infocard (mindcardId, imageId, description) VALUES (?,?,?)")) {
            stmnt.setInt(1,entity.getMindcardId());
            stmnt.setInt(2,entity.getImageId());
            stmnt.setString(3,entity.getDescription());
            stmnt.executeUpdate();
            ResultSet generatedIds = stmnt.getGeneratedKeys();
            if (generatedIds.next()) {
                int newId = generatedIds.getInt(1);
                System.out.println("Infocard with ID "+newId+" successfully created.");
                return getById(newId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <S extends Infocard> void delete(S entity) {
        deleteById(entity.getMindcardId());
    }

    public void deleteById(Integer id) {
        try (PreparedStatement stmnt = database.getConnection().prepareStatement("DELETE FROM Infocard WHERE infocardId=?")) {
            stmnt.setInt(1,id);
            stmnt.executeUpdate();
            System.out.println("Infocard with ID "+id+" successfully deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Image getImage(int infocardId) {
        try {
            PreparedStatement statement = database.getConnection().prepareStatement("SELECT Image.imageId, Image.imagePath FROM Infocard, Image WHERE infocardId = ? AND Infocard.imageId = Image.imageId");
            statement.setInt(1,infocardId);
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
